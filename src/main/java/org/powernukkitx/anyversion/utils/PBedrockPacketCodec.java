package org.powernukkitx.anyversion.utils;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddActorPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddItemActorPacket;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetActorDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSubChunkBlocksPacket;
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

        BedrockPacket transformedPacket = shouldIsolate(packet) ? packet.clone() : packet;
        if (protocolPlayer.getVersion().codec().getPacketDefinition(transformedPacket.getClass()) == null) {
            ReferenceCountUtil.release(msg);
            promise.setSuccess();
            log.debug("Dropped outbound {} because it is not available for protocol {}",
                    transformedPacket.getClass().getSimpleName(), protocolPlayer.protocol());
            return;
        }

        Registries.PACKETHANDLER.handlePacket(protocolPlayer, transformedPacket);
        wrapper.setPacket(transformedPacket);
        super.write(ctx, msg, promise);
    }

    private boolean shouldIsolate(BedrockPacket packet) {
        return packet instanceof AddActorPacket
                || packet instanceof AddPlayerPacket
                || packet instanceof AddItemActorPacket
                || packet instanceof SetActorDataPacket
                || packet instanceof UpdateBlockPacket
                || packet instanceof UpdateSubChunkBlocksPacket;
    }

}
