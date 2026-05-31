package org.powernukkitx.anyversion.utils;

import cn.nukkit.network.NetworkConstants;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

@RequiredArgsConstructor
@Slf4j
public class PBedrockPacketCodec extends ChannelDuplexHandler {
    public static final String NAME = "bedrock-packet-codec-protocolized";

    @Getter
    private final ProtocolPlayer protocolPlayer;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof BedrockPacketWrapper wrapper)) {
            super.write(ctx, msg, promise);
            return;
        }

        BedrockPacket packet = wrapper.getPacket();
        if (packet == null) {
            super.write(ctx, msg, promise);
            return;
        }

        if (protocolPlayer.getVersion().codec().getPacketDefinition(packet.getClass()) == null) {
            ReferenceCountUtil.release(msg);
            promise.setSuccess();
            log.debug("Dropped outbound {} because it is not available for protocol {}",
                    packet.getClass().getSimpleName(), protocolPlayer.protocol());
            return;
        }

        BedrockPacket transformedPacket = BedrockPacketDeepCopy.copy(
                ctx.alloc(),
                NetworkConstants.CODEC,
                ProtocolVersion.getCurrent().helper(),
                packet);
        Registries.PACKETHANDLER.handlePacket(protocolPlayer, transformedPacket);
        wrapper.setPacket(transformedPacket);
        super.write(ctx, msg, promise);
    }
}
