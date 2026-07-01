package org.powernukkitx.anyversion.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketDefinition;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

public final class BedrockPacketDeepCopy {

    private BedrockPacketDeepCopy() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BedrockPacket copy(ByteBufAllocator allocator, BedrockCodec codec, BedrockPacket packet) {
        return copy(allocator, codec, codec.createHelper(), packet);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BedrockPacket copy(ByteBufAllocator allocator, BedrockCodec codec, BedrockCodecHelper templateHelper, BedrockPacket packet) {
        BedrockPacketDefinition definition = codec.getPacketDefinition(packet.getClass());
        if (definition == null) {
            return packet;
        }

        BedrockCodecHelper helper = codec.createHelper();
        configure(helper, templateHelper);
        ByteBuf buffer = allocator.buffer();
        try {
            codec.tryEncode(helper, buffer, packet);
            return codec.tryDecode(helper, buffer, definition.getId(), definition.getRecipient());
        } finally {
            buffer.release();
        }
    }

    private static void configure(BedrockCodecHelper helper, BedrockCodecHelper templateHelper) {
        helper.setEncodingSettings(templateHelper.getEncodingSettings());
        helper.setItemDefinitions(templateHelper.getItemDefinitions());
        helper.setBlockDefinitions(templateHelper.getBlockDefinitions());
        try {
            helper.setCameraPresetDefinitions(templateHelper.getCameraPresetDefinitions());
        } catch (UnsupportedOperationException ignored) {
        }
    }
}
