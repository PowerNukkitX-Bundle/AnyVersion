package org.powernukkitx.anyversion.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.Network;
import cn.nukkit.network.connection.BedrockPeer;
import cn.nukkit.network.connection.BedrockSession;
import cn.nukkit.network.connection.netty.codec.compression.CompressionCodec;
import cn.nukkit.network.connection.netty.codec.packet.BedrockPacketCodec;
import cn.nukkit.network.connection.util.ChainValidationResult;
import cn.nukkit.network.process.SessionState;
import cn.nukkit.network.process.handler.InGamePacketHandler;
import cn.nukkit.network.protocol.*;
import cn.nukkit.network.protocol.types.PlayerInfo;
import cn.nukkit.utils.EncryptionUtils;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.google.gson.reflect.TypeToken;
import io.netty.channel.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.slf4j.Slf4j;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.config.AnyVersionConfig;
import org.powernukkitx.anyversion.processors.PEmoteProcessor;
import org.powernukkitx.anyversion.utils.PBedrockPacketCodec;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;

@Slf4j
public class ProtocolManager implements Listener {

    private static final Object2ObjectOpenHashMap<String, ProtocolPlayer> players = new Object2ObjectOpenHashMap<>();

    @EventHandler
    public void onRequestNetworkSettingsPacket(DataPacketReceiveEvent event) {
        if(event.getPacket() instanceof RequestNetworkSettingsPacket packet) {
            int client_protocol = packet.protocolVersion;
            int server_protocol = ProtocolInfo.CURRENT_PROTOCOL;

            AnyVersionConfig configuration = AnyVersion.getPlugin().getConfiguration();
            int lowest = configuration.getLowestVersion();

            if (client_protocol < lowest) {
                PlayStatusPacket status = new PlayStatusPacket();
                status.setStatus(PlayStatusPacket.LOGIN_FAILED_CLIENT);

                event.getPlayer().dataPacket(status);
                return;
            }

            if (client_protocol < server_protocol) {
                if(Arrays.stream(ProtocolVersion.getVersions()).anyMatch(p -> p.protocol() == client_protocol)) {
                    try {
                        Field fSessionMap = Network.class.getDeclaredField("sessionMap");
                        fSessionMap.setAccessible(true);
                        Map<InetSocketAddress, BedrockSession> sessionMap = (Map<InetSocketAddress, BedrockSession>) fSessionMap.get(Server.getInstance().getNetwork());
                        fSessionMap.setAccessible(false);
                        Field fMachine = BedrockSession.class.getDeclaredField("machine");
                        fMachine.setAccessible(true);
                        for (BedrockSession bedrockSession : sessionMap.values()) {
                            StateMachine<SessionState, SessionState> machine = (StateMachine<SessionState, SessionState>) fMachine.get(bedrockSession);
                            if(machine.getState() == SessionState.START) {
                                Field fConfig = StateMachine.class.getDeclaredField("config");
                                fConfig.setAccessible(true);
                                StateMachineConfig<SessionState, SessionState> config = (StateMachineConfig<SessionState, SessionState>) fConfig.get(machine);
                                fConfig.setAccessible(false);
                                ProtocolPlayer player = new ProtocolPlayer(bedrockSession, client_protocol);
                                PBedrockPacketCodec codec = new PBedrockPacketCodec(player);
                                ChannelPipeline pipeline = bedrockSession.getPeer().getChannel().pipeline();
                                pipeline.replace(BedrockPacketCodec.NAME, BedrockPacketCodec.NAME, codec);
                                config.configure(SessionState.START).onExit(() -> {
                                    if(client_protocol < ProtocolVersion.MINECRAFT_PE_1_20_60.protocol()) {
                                        Channel channel = bedrockSession.getPeer().getChannel();
                                        ChannelHandler handler = channel.pipeline().get(CompressionCodec.NAME);
                                        if(handler instanceof CompressionCodec compressionCodec) {
                                            try {
                                                Field field = compressionCodec.getClass().getDeclaredField("prefixed");
                                                field.setAccessible(true);
                                                field.set(compressionCodec, false);
                                                field.setAccessible(false);
                                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                        fMachine.setAccessible(false);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    packet.protocolVersion = server_protocol;
                } else AnyVersion.getPlugin().getLogger().info("Someone tried to join using protocol " + packet.protocolVersion);
            }
        }
    }


    @EventHandler
    public void onLoginPacket(DataPacketReceiveEvent event) {
        try {
            if(event.getPacket() instanceof LoginPacket packet) {
                if(packet.protocol == ProtocolInfo.CURRENT_PROTOCOL) return;
                Field fSessionMap = Network.class.getDeclaredField("sessionMap");
                fSessionMap.setAccessible(true);
                Map<InetSocketAddress, BedrockSession> sessionMap = (Map<InetSocketAddress, BedrockSession>) fSessionMap.get(Server.getInstance().getNetwork());
                fSessionMap.setAccessible(false);
                for(BedrockSession bedrockSession : sessionMap.values()) {
                    Field fMachine = BedrockSession.class.getDeclaredField("machine");
                    fMachine.setAccessible(true);
                    StateMachine<SessionState, SessionState> machine = (StateMachine<SessionState, SessionState>) fMachine.get(bedrockSession);
                    fMachine.setAccessible(false);
                    if(machine.getState() == SessionState.LOGIN) {
                        ProtocolPlayer player = new ProtocolPlayer(bedrockSession, packet.protocol);

                        ChainValidationResult result = EncryptionUtils.validatePayload(packet.authPayload);
                        String uuid = result.identityClaims().extraData.xuid;
                        players.put(uuid, player);
                        Field fConfig = StateMachine.class.getDeclaredField("config");
                        fConfig.setAccessible(true);
                        StateMachineConfig<SessionState, SessionState> config = (StateMachineConfig<SessionState, SessionState>) fConfig.get(machine);
                        fConfig.setAccessible(false);
                        Method onServerLoginSuccess = BedrockSession.class.getDeclaredMethod("onServerLoginSuccess");
                        Field fInfo = BedrockSession.class.getDeclaredField("info");
                        String finalUuid = uuid;

                        config.configure(SessionState.LOGIN).onExit(() -> {
                            try {
                                onServerLoginSuccess.setAccessible(true);
                                onServerLoginSuccess.invoke(bedrockSession);
                                onServerLoginSuccess.setAccessible(false);
                                fInfo.setAccessible(true);
                                PlayerInfo info = (PlayerInfo) fInfo.get(bedrockSession);
                                fInfo.setAccessible(false);
                                if(info.getUniqueId().toString().equals(finalUuid)) {
                                    PBedrockPacketCodec codec = new PBedrockPacketCodec(player);
                                    ChannelPipeline pipeline = bedrockSession.getPeer().getChannel().pipeline();
                                    pipeline.addBefore(BedrockPacketCodec.NAME, PBedrockPacketCodec.NAME, codec);
                                    pipeline.remove(BedrockPacketCodec.NAME);
                                    config.configure(SessionState.IN_GAME).onEntry(() -> {
                                        InGamePacketHandler handler = new InGamePacketHandler(bedrockSession);
                                        handler.getManager().registerProcessor(new PEmoteProcessor());
                                        bedrockSession.setPacketHandler(handler);
                                    });
                                }
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onDataPacketSend(DataPacketSendEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            BedrockSession session = player.getSession();
            BedrockPeer peer = session.getPeer();
            Channel channel = peer.getChannel();
            ChannelPipeline pipeline = channel.pipeline();
            PBedrockPacketCodec codec = pipeline.get(PBedrockPacketCodec.class);
            if(codec != null) {
                ProtocolPlayer protocolPlayer = codec.getProtocolPlayer();
                ProtocolVersion protocol = protocolPlayer.getVersion();
                if(protocol.codec().getPacketDefinition(event.getPacket().pid()) == null) {
                    event.setCancelled();
                    log.debug("Tried to send " + event.getPacket().getClass().getSimpleName() + " that is not available for that version! (" + protocol.protocol() + ")");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(players.containsKey(player.getXUID())) {
            ProtocolVersion version = get(player).getVersion();
            AnyVersion.getPlugin().getLogger().info("§e" + player.getName() + " joined with outdated Minecraft §c" + version.version() + " §e(" + version.protocol() + ")");
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
        return get(player.getXUID());
    }

    private static class MapTypeToken extends TypeToken<Map<String, Object>> {
    }

}
