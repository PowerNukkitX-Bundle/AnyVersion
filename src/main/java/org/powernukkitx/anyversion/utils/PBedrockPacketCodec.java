package org.powernukkitx.anyversion.utils;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.ShapelessRecipePayload;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.powernukkitx.network.NetworkConstants;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.common.util.VarInts;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.manager.ProtocolManager;
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

        ProtocolVersion target = protocolPlayer.getVersion();
        BedrockPacketDefinition<?> targetDefinition = target.codec().getPacketDefinition(packet.getClass());

        if (targetDefinition == null) {
            ReferenceCountUtil.release(msg);
            promise.setSuccess();
            log.debug("Dropped outbound {} because it is not available for protocol {}",
                    packet.getClass().getSimpleName(), protocolPlayer.protocol());
            return;
        }

        ByteBuf targetBuffer = ctx.alloc().buffer(128);
        try {
            Registries.PACKETHANDLER.handlePacket(resolveProtocolPlayer(), packet);

            wrapper.setPacketId(targetDefinition.getId());
            encodeHeader(targetBuffer, wrapper, target.codec());
            target.codec().tryEncode(target.helper(), targetBuffer, packet);

            ReferenceCountUtil.safeRelease(wrapper.getPacketBuffer());
            wrapper.setPacketBuffer(targetBuffer.retain());
            super.write(ctx, wrapper, promise);
        } catch (Throwable throwable) {
            log.error("Error protocolizing outbound packet {}", packet, throwable);
            ReferenceCountUtil.release(msg);
            promise.tryFailure(throwable);
        } finally {
            targetBuffer.release();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BedrockPacketWrapper wrapper) || wrapper.getPacket() == null) {
            super.channelRead(ctx, msg);
            return;
        }

        BedrockPacket packet = wrapper.getPacket();
        ProtocolVersion source = protocolPlayer.getVersion();
        ProtocolVersion current = ProtocolVersion.getCurrent();
        BedrockPacketDefinition<?> sourceDefinition =
                source.codec().getPacketDefinition(packet.getClass());
        BedrockPacketDefinition<?> currentDefinition =
                NetworkConstants.CODEC.getPacketDefinition(packet.getClass());

        if (sourceDefinition == null || currentDefinition == null) {
            super.channelRead(ctx, msg);
            return;
        }

        ByteBuf currentBuffer = ctx.alloc().buffer(128);
        try {
            Registries.PACKETHANDLER.handlePacket(resolveProtocolPlayer(), packet);

            NetworkConstants.CODEC.tryEncode(current.helper(), currentBuffer, packet);
            BedrockPacket protocolized = NetworkConstants.CODEC.tryDecode(
                    current.helper(), currentBuffer, currentDefinition.getId());

            wrapper.setPacketId(currentDefinition.getId());
            wrapper.setPacket(protocolized);
            super.channelRead(ctx, wrapper);
        } catch (Throwable throwable) {
            log.error("Error protocolizing inbound packet {}", packet, throwable);
            ReferenceCountUtil.release(msg);
            throw throwable;
        } finally {
            currentBuffer.release();
        }
    }

    private static void encodeHeader(ByteBuf buffer, BedrockPacketWrapper wrapper, BedrockCodec codec) {
        switch (codec.getRaknetProtocolVersion()) {
            case 9, 10, 11 -> {
                int header = wrapper.getPacketId() & 0x3ff;
                header |= (wrapper.getSenderSubClientId() & 3) << 10;
                header |= (wrapper.getTargetSubClientId() & 3) << 12;
                VarInts.writeUnsignedInt(buffer, header);
            }
            case 8 -> {
                buffer.writeByte(wrapper.getPacketId());
                buffer.writeByte(wrapper.getSenderSubClientId());
                buffer.writeByte(wrapper.getTargetSubClientId());
            }
            case 7 -> buffer.writeByte(wrapper.getPacketId());
            default -> throw new UnsupportedOperationException(
                    "Unsupported RakNet protocol version: " + codec.getRaknetProtocolVersion());
        }
    }

    private ProtocolPlayer resolveProtocolPlayer() {
        ProtocolPlayer registered = ProtocolManager.get(protocolPlayer.player());
        return registered == null ? protocolPlayer : registered;
    }
}
