package org.powernukkitx.anyversion.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.netty.channel.raknet.config.RakServerCookieMode;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v3;
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer;
import org.powernukkitx.Server;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.config.category.NetworkSettings;
import org.powernukkitx.config.category.network.NetherNetSettings;
import org.powernukkitx.config.category.network.RakNetSettings;
import org.powernukkitx.network.NetworkInterface;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Locale;

/**
 * NetherNet only carries 1.26.50 and newer. When the server runs on it, this binds a RakNet
 * listener next to it so everything older still has a way in, and turns 1.26.50+ clients away
 * from that listener so they go through NetherNet instead.
 *
 * @author xRookieFight
 * @since 14/09/2026
 */
@Slf4j
public final class RakNetFallback {

    /**
     * Marks the sessions that arrived over the fallback listener rather than over NetherNet.
     */
    public static final AttributeKey<Boolean> FALLBACK_SESSION = AttributeKey.valueOf("anyversion:raknet-fallback");

    private static final int NETHERNET_PROTOCOL = ProtocolVersion.MINECRAFT_PE_1_26_50.protocol();
    private static final int ADVERTISEMENT_REFRESH_TICKS = 20;

    private static Channel channel;
    private static EventLoopGroup eventLoopGroup;

    private RakNetFallback() {
    }

    public static boolean isFallbackSession(BedrockServerSession session) {
        return Boolean.TRUE.equals(session.getPeer().getChannel().attr(FALLBACK_SESSION).get());
    }

    /**
     * Whether a client speaking this protocol belongs on NetherNet instead of the fallback.
     */
    public static boolean belongsOnNetherNet(int clientProtocol) {
        return clientProtocol >= NETHERNET_PROTOCOL;
    }

    public static void start(Server server) {
        if (channel != null) {
            return;
        }
        if (!AnyVersion.getPlugin().getConfiguration().isRaknetFallback()) {
            return;
        }
        final NetworkSettings net = server.getSettings().networkSettings();
        if (net.resolvedTransport() != NetworkSettings.TransportType.NETHERNET) {
            return;
        }

        final InetSocketAddress bindAddress = bindAddress(server, net);
        if (bindAddress == null) {
            return;
        }

        final Class<? extends DatagramChannel> datagramChannel = openEventLoopGroup();
        final NetworkInterface network = server.getNetwork();
        final RakNetSettings rak = net.rakNetSettings();
        try {
            channel = new ServerBootstrap()
                    .channelFactory(RakChannelFactory.server(datagramChannel))
                    .option(RakChannelOption.RAK_ADVERTISEMENT, network.getPong().toByteBuf())
                    .option(RakChannelOption.RAK_SUPPORTED_PROTOCOLS, supportedProtocols())
                    .option(RakChannelOption.RAK_PACKET_LIMIT, rak.packetLimit())
                    .option(RakChannelOption.RAK_SERVER_COOKIE_MODE, cookieMode(rak.cookieMode()))
                    .childOption(RakChannelOption.RAK_AUTO_FLUSH, rak.autoFlush())
                    .childOption(RakChannelOption.RAK_FLUSH_INTERVAL, rak.flushInterval())
                    .childOption(RakChannelOption.RAK_MAX_QUEUED_BYTES, rak.maxQueuedBytes())
                    .childAttr(FALLBACK_SESSION, Boolean.TRUE)
                    .group(eventLoopGroup)
                    .childHandler(sessionInitializer(network))
                    .bind(bindAddress)
                    .syncUninterruptibly()
                    .channel();
        } catch (Exception e) {
            log.error("AnyVersion could not bind the RakNet fallback on udp/{}", bindAddress.getPort(), e);
            shutdown();
            return;
        }

        server.getScheduler().scheduleRepeatingTask(AnyVersion.getPlugin(), RakNetFallback::refreshAdvertisement,
                ADVERTISEMENT_REFRESH_TICKS);
        log.info("AnyVersion RakNet fallback listening on udp/{}, clients below {} join through it",
                bindAddress.getPort(), ProtocolVersion.MINECRAFT_PE_1_26_50.version());
    }

    public static void shutdown() {
        if (channel != null) {
            channel.close();
            channel = null;
        }
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully();
            eventLoopGroup = null;
        }
    }

    /**
     * The listener port, or null when something else already holds the UDP side of it. NetherNet
     * signalling is TCP, so the port is usually free, but query and the WebRTC media socket both
     * want the same UDP port when they are left on their defaults.
     */
    private static InetSocketAddress bindAddress(Server server, NetworkSettings net) {
        int port = AnyVersion.getPlugin().getConfiguration().getRaknetFallbackPort();
        if (port <= 0) {
            port = server.getPort();
        }
        final String host = server.getIp() == null || server.getIp().isEmpty() ? "0.0.0.0" : server.getIp();
        if (port != server.getPort()) {
            return new InetSocketAddress(host, port);
        }

        final NetherNetSettings nethernet = net.netherNetSettings();
        if (net.enableQuery()) {
            log.error("Query holds udp/{}, so the AnyVersion RakNet fallback cannot bind there. "
                    + "Set raknetFallbackPort to a free port or turn query off", port);
            return null;
        }
        if (nethernet.resolvedSignalingMode().binds()
                && (nethernet.udpPort() <= 0 || nethernet.udpPort() == port)) {
            log.error("NetherNet media gathers on udp/{}, so the AnyVersion RakNet fallback cannot bind there. "
                    + "Set network-settings.nethernet.udpPort to a port of its own, or raknetFallbackPort "
                    + "to a free port", port);
            return null;
        }
        return new InetSocketAddress(host, port);
    }

    private static Class<? extends DatagramChannel> openEventLoopGroup() {
        if (Epoll.isAvailable()) {
            eventLoopGroup = new EpollEventLoopGroup();
            return EpollDatagramChannel.class;
        }
        if (KQueue.isAvailable()) {
            eventLoopGroup = new KQueueEventLoopGroup();
            return KQueueDatagramChannel.class;
        }
        eventLoopGroup = new NioEventLoopGroup();
        return NioDatagramChannel.class;
    }

    /**
     * Every RakNet protocol version the codecs AnyVersion carries can speak, so an old client is
     * not turned away before it ever sends a Bedrock packet.
     */
    private static int[] supportedProtocols() {
        return Arrays.stream(ProtocolVersion.getVersions())
                .filter(version -> !belongsOnNetherNet(version.protocol()))
                .mapToInt(version -> version.codec().getRaknetProtocolVersion())
                .distinct()
                .toArray();
    }

    private static RakServerCookieMode cookieMode(String mode) {
        try {
            RakServerCookieMode parsed = RakServerCookieMode.valueOf(mode.trim().toUpperCase(Locale.ROOT));
            if (parsed != RakServerCookieMode.INVALID) {
                return parsed;
            }
        } catch (IllegalArgumentException ignored) {
        }
        return RakServerCookieMode.ACTIVE;
    }

    private static void refreshAdvertisement() {
        if (channel instanceof RakServerChannel rakChannel) {
            rakChannel.config().setAdvertisement(AnyVersion.getPlugin().getServer().getNetwork().getPong().toByteBuf());
        }
    }

    /**
     * The same pipeline the server builds for RakNet sessions. Handing the session back to the
     * server's own initSession keeps it in the session map and gives it the stock packet handler,
     * which is what the rest of the server expects to find.
     */
    private static BedrockServerInitializer sessionInitializer(NetworkInterface network) {
        final Method initSession = findInitSession(network);
        return new BedrockServerInitializer() {
            @Override
            protected void initSession(BedrockServerSession session) {
                session.getPeer().getChannel().pipeline().replace(
                        BedrockPacketCodec.NAME,
                        BedrockPacketCodec.NAME,
                        new BedrockPacketCodec_v3(log.isDebugEnabled())
                );
                try {
                    initSession.invoke(network, session);
                } catch (ReflectiveOperationException e) {
                    log.error("AnyVersion could not hand a fallback session to the server", e);
                    session.close("Internal server error");
                }
            }
        };
    }

    private static Method findInitSession(NetworkInterface network) {
        try {
            Method method = network.getClass().getDeclaredMethod("initSession", BedrockServerSession.class);
            method.setAccessible(true);
            return method;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to reach the server session initializer", e);
        }
    }
}
