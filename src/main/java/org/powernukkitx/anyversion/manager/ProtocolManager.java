package org.powernukkitx.anyversion.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.PacketReceiveEvent;
import cn.nukkit.network.NetworkConstants;
import cn.nukkit.network.process.PacketHandler;
import cn.nukkit.network.process.PacketHandlerRegistry;
import cn.nukkit.network.process.PlayerSessionHolder;
import cn.nukkit.network.process.SessionState;
import cn.nukkit.network.process.auth.ClientChainData;
import cn.nukkit.network.process.auth.ClientSkinData;
import cn.nukkit.network.process.handler.LoginHandler;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.data.auth.PlayerAuthenticationType;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;
import org.cloudburstmc.protocol.bedrock.data.DisconnectFailReason;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PlayStatus;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestNetworkSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerToClientHandshakePacket;
import org.cloudburstmc.protocol.bedrock.util.ChainValidationResult;
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.BedrockPacketDeepCopy;
import org.powernukkitx.anyversion.utils.PBedrockPacketCodec;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class ProtocolManager implements Listener {

    private static final Object2ObjectOpenHashMap<String, ProtocolPlayer> players = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<PlayerSessionHolder, ProtocolVersion> pendingVersions = new Object2ObjectOpenHashMap<>();
    private static final Field PACKET_RECEIVE_EVENT_PACKET_FIELD;

    static {
        try {
            PACKET_RECEIVE_EVENT_PACKET_FIELD = PacketReceiveEvent.class.getDeclaredField("packet");
            PACKET_RECEIVE_EVENT_PACKET_FIELD.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to access PacketReceiveEvent packet field", e);
        }
    }

    public ProtocolManager() {
        registerPreLoginHandlers();
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        ProtocolPlayer protocolPlayer = get(event.getPlayer());
        if (protocolPlayer != null) {
            BedrockPacket packet = BedrockPacketDeepCopy.copy(
                    ByteBufAllocator.DEFAULT,
                    protocolPlayer.getVersion().codec(),
                    protocolPlayer.getVersion().helper(),
                    event.getPacket());
            Registries.PACKETHANDLER.handlePacket(protocolPlayer.withPlayer(event.getPlayer()), packet);
            replacePacket(event, packet);
        }
    }

    private static void replacePacket(PacketReceiveEvent event, BedrockPacket packet) {
        try {
            PACKET_RECEIVE_EVENT_PACKET_FIELD.set(event, packet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to replace PacketReceiveEvent packet", e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ProtocolPlayer protocolPlayer = get(event.getPlayer());
        if (protocolPlayer != null) {
            ProtocolVersion version = protocolPlayer.getVersion();
            AnyVersion.getPlugin().getLogger().info(event.getPlayer().getName() + " joined with outdated Minecraft " + version.version() + " (" + version.protocol() + ")");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer().getXUID());
    }

    public static ProtocolPlayer get(String xuid) {
        return players.get(xuid);
    }

    public static ProtocolPlayer get(Player player) {
        return player == null ? null : get(player.getXUID());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerPreLoginHandlers() {
        try {
            Field mapField = PacketHandlerRegistry.class.getDeclaredField("MAP");
            mapField.setAccessible(true);
            Map<Class<? extends BedrockPacket>, PacketHandler> map = (Map<Class<? extends BedrockPacket>, PacketHandler>) mapField.get(null);
            map.put(RequestNetworkSettingsPacket.class, new AnyVersionRequestNetworkSettingsHandler());
            map.put(LoginPacket.class, new AnyVersionLoginHandler());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to register AnyVersion packet handlers", e);
        }
    }

    private static final class AnyVersionRequestNetworkSettingsHandler implements PacketHandler<RequestNetworkSettingsPacket> {

        @Override
        public void handle(RequestNetworkSettingsPacket packet, PlayerSessionHolder holder, Server server) {
            int clientProtocol = packet.getClientNetworkVersion();
            int serverProtocol = NetworkConstants.CODEC.getProtocolVersion();
            ProtocolVersion version = ProtocolVersion.has(clientProtocol) ? ProtocolVersion.get(clientProtocol) : null;

            if (clientProtocol > serverProtocol) {
                holder.sendPlayStatus(PlayStatus.LOGIN_FAILED_SERVER_OLD);
                holder.disconnect(DisconnectFailReason.OUTDATED_SERVER);
                return;
            }
            if (clientProtocol < serverProtocol && version == null) {
                holder.sendPlayStatus(PlayStatus.LOGIN_FAILED_CLIENT_OLD);
                holder.disconnect(DisconnectFailReason.OUTDATED_CLIENT);
                return;
            }
            if (holder.getState().equals(SessionState.REQUESTED_NETWORK_SETTINGS)) {
                holder.disconnect(DisconnectFailReason.UNEXPECTED_PACKET);
                return;
            }
            if (isAddressBanned(server, holder)) {
                return;
            }

            ProtocolVersion targetVersion = version == null ? ProtocolVersion.getCurrent() : version;
            pendingVersions.put(holder, targetVersion);
            holder.getSession().setCodec(targetVersion.codec());
            if (targetVersion.protocol() != serverProtocol) {
                installPacketTransformer(holder, targetVersion);
            }
            holder.setState(SessionState.REQUESTED_NETWORK_SETTINGS);

            PacketCompressionAlgorithm algorithm = Server.getInstance().getSettings().networkSettings().snappy()
                    ? PacketCompressionAlgorithm.SNAPPY
                    : PacketCompressionAlgorithm.ZLIB;
            NetworkSettingsPacket networkSettingsPacket = new NetworkSettingsPacket();
            networkSettingsPacket.setCompressionThreshold(1);
            networkSettingsPacket.setCompressionAlgorithm(algorithm);

            holder.getSession().sendPacketImmediately(networkSettingsPacket);
            holder.getSession().setCompression(algorithm);
            holder.setState(SessionState.LOGIN);
        }

        private boolean isAddressBanned(Server server, PlayerSessionHolder holder) {
            String address = ((InetSocketAddress) holder.getSession().getSocketAddress()).getAddress().getHostAddress();
            if (server.getIPBans().isBanned(address)) {
                String reason = server.getIPBans().getEntires().get(address).getReason();
                holder.getSession().close(!reason.isEmpty() ? "You are banned. Reason: " + reason : "You are banned");
                return true;
            }
            return false;
        }
    }

    private static final class AnyVersionLoginHandler implements PacketHandler<LoginPacket> {
        private final LoginHandler delegate = new LoginHandler();

        @Override
        public void handle(LoginPacket packet, PlayerSessionHolder holder, Server server) {
            int clientProtocol = packet.getClientNetworkVersion();
            ProtocolVersion pending = pendingVersions.remove(holder);
            ProtocolVersion version = ProtocolVersion.has(clientProtocol) ? ProtocolVersion.get(clientProtocol) : pending;
            if (version == null) {
                version = ProtocolVersion.getCurrent();
            }
            packet.setClientNetworkVersion(NetworkConstants.CODEC.getProtocolVersion());
            boolean downgraded = version.protocol() != NetworkConstants.CODEC.getProtocolVersion();
            if (downgraded) {
                handleLenient(packet, holder, server, version);
            } else {
                delegate.handle(packet, holder, server);
            }
            if (holder.getPlayerInfo() != null && downgraded) {
                String xuid = holder.getPlayerInfo().getIdentityClaims().extraData.xuid;
                players.put(xuid, new ProtocolPlayer(holder.getSession(), version.protocol()));
            }
        }

        private void handleLenient(LoginPacket packet, PlayerSessionHolder holder, Server server, ProtocolVersion version) {
            if (!holder.getState().equals(SessionState.LOGIN)) {
                holder.disconnect(DisconnectFailReason.UNEXPECTED_PACKET);
                return;
            }

            final PlayerAuthenticationType type = packet.getAuthenticationType();
            final DisconnectFailReason notAuthenticated = DisconnectFailReason.NOT_AUTHENTICATED;
            if (type.equals(PlayerAuthenticationType.UNKNOWN)) {
                holder.disconnect(notAuthenticated);
                return;
            }

            final boolean xboxAuthRequired = server.getSettings().baseSettings().xboxAuth();
            if (xboxAuthRequired && packet.getToken() == null || packet.getToken().isEmpty()) {
                holder.disconnect(notAuthenticated);
                return;
            }

            try {
                final ChainValidationResult result = EncryptionUtils.validateToken(type, packet.getToken());
                if (xboxAuthRequired && !result.signed() && !server.getSettings().baseSettings().waterdogpe()) {
                    holder.disconnect(notAuthenticated);
                    return;
                }

                final ChainValidationResult.IdentityClaims identityClaims = result.identityClaims();
                final PlayerPreLoginEvent event = new PlayerPreLoginEvent(identityClaims);
                server.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    holder.disconnect(DisconnectFailReason.UNKNOWN, event.getKickMessage());
                    return;
                }

                if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
                    holder.disconnect(DisconnectFailReason.SERVER_FULL);
                    return;
                }

                if (!server.isWhitelisted(identityClaims.extraData.displayName.toLowerCase(Locale.ENGLISH))) {
                    holder.disconnect(DisconnectFailReason.NOT_ALLOWED);
                    return;
                }

                var banEntry = server.getNameBans().getEntires().get(identityClaims.extraData.displayName.toLowerCase(Locale.ENGLISH));
                if (banEntry != null) {
                    String reason = banEntry.getReason();
                    holder.disconnect(DisconnectFailReason.UNKNOWN, !reason.isEmpty() ? "You are banned. Reason: " + reason : "You are banned");
                    return;
                }

                final LenientClientJwt lenient = validateClientJwtLenient(packet, identityClaims.parsedIdentityPublicKey());
                if (lenient == null) {
                    holder.disconnect(DisconnectFailReason.INVALID_PLATFORM_SKIN);
                    return;
                }

                if (lenient.chain.isEduMode()) {
                    holder.sendPlayStatus(PlayStatus.LOGIN_FAILED_EDITION_MISMATCH_EDU_TO_VANILLA);
                    holder.disconnect(DisconnectFailReason.EDITION_MISMATCH_EDU_TO_VANILLA);
                    return;
                }

                holder.setPlayerInfo(new Player.PlayerInfo(identityClaims, lenient.chain, lenient.skin, result.signed()));

                if (server.enabledNetworkEncryption) {
                    enableEncryption(identityClaims, holder);
                } else {
                    holder.sendPlayStatus(PlayStatus.LOGIN_SUCCESS);
                    holder.setState(SessionState.RESOURCE_PACK);
                    holder.sendResourcePacksInfo(server);
                }
            } catch (Exception e) {
                log.debug("Lenient login failed for protocol {}", version.protocol(), e);
                holder.disconnect(notAuthenticated);
            }
        }

        private LenientClientJwt validateClientJwtLenient(LoginPacket packet, PublicKey identityPublicKey) {
            final String clientJwt = packet.getClientJwt();
            if (clientJwt == null || clientJwt.isEmpty()) {
                return null;
            }
            try {
                final JwtContext ctx = new JwtConsumerBuilder()
                        .setVerificationKey(identityPublicKey)
                        .build()
                        .process(clientJwt);
                final JwtClaims claims = ctx.getJwtClaims();
                padChainClaims(claims);

                final ClientChainData chain = ClientChainData.from(claims);
                final SerializedSkin skin = ClientSkinData.readSkin(claims);
                if (chain == null || skin == null) {
                    return null;
                }
                return new LenientClientJwt(chain, skin);
            } catch (InvalidJwtException ignored) {
            }
            return null;
        }

        private void enableEncryption(ChainValidationResult.IdentityClaims claims, PlayerSessionHolder holder) {
            try {
                var session = holder.getSession();
                var clientKey = EncryptionUtils.parseKey(claims.identityPublicKey);
                var encryptionKeyPair = EncryptionUtils.createKeyPair();
                var encryptionToken = EncryptionUtils.generateRandomToken();
                var encryptionKey = EncryptionUtils.getSecretKey(encryptionKeyPair.getPrivate(), clientKey, encryptionToken);
                var handshakeWebToken = EncryptionUtils.createHandshakeJwt(encryptionKeyPair, encryptionToken);
                if (!holder.getSession().isConnected()) {
                    return;
                }
                var pk = new ServerToClientHandshakePacket();
                pk.setHandshakeWebToken(handshakeWebToken);
                session.sendPacketImmediately(pk);
                session.enableEncryption(encryptionKey);
                holder.setState(SessionState.ENCRYPTION);
            } catch (Exception e) {
                holder.disconnect(DisconnectFailReason.UNKNOWN, "encryption error");
            }
        }
    }

    private record LenientClientJwt(ClientChainData chain, SerializedSkin skin) { }

    private static void padChainClaims(JwtClaims claims) {
        putIfAbsent(claims, "CompatibleWithClientSideChunkGen", Boolean.FALSE);
        putIfAbsent(claims, "GraphicsMode", 0);
        putIfAbsent(claims, "MemoryTier", 0);
        putIfAbsent(claims, "ClientIsEditorCapable", Boolean.FALSE);
        putIfAbsent(claims, "ClientEditorConnectionIntent", 0);
    }

    private static void putIfAbsent(JwtClaims claims, String key, Object value) {
        if (!claims.hasClaim(key)) {
            claims.setClaim(key, value);
        }
    }

    private static void installPacketTransformer(PlayerSessionHolder holder, ProtocolVersion version) {
        ChannelPipeline pipeline = holder.getSession().getPeer().getChannel().pipeline();
        if (pipeline.get(PBedrockPacketCodec.NAME) != null) {
            pipeline.remove(PBedrockPacketCodec.NAME);
        }
        PBedrockPacketCodec transformer = new PBedrockPacketCodec(new ProtocolPlayer(holder.getSession(), version.protocol()));
        if (pipeline.get(BedrockPacketCodec.NAME) != null) {
            pipeline.addAfter(BedrockPacketCodec.NAME, PBedrockPacketCodec.NAME, transformer);
        } else {
            pipeline.addLast(PBedrockPacketCodec.NAME, transformer);
        }
    }
}
