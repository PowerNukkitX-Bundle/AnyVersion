package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockState;
import cn.nukkit.level.format.palette.BlockPalette;
import cn.nukkit.level.format.palette.Palette;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.BlockStateRuntimeDataDeserializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LevelChunkHandler extends PacketHandler<LevelChunkPacket> {

    @SneakyThrows
    @Override
    public void handle(ProtocolPlayer player, LevelChunkPacket packet) {
        ByteBuf data = packet.getSerializedChunkData().copy();
        int total = packet.getSubChunksCount();
        ByteBuf modified = Unpooled.buffer();
        Field field = Palette.class.getDeclaredField("palette");
        try {
            field.setAccessible(true);
            for (int i = 0; i < total; i++) {
                modified.writeByte(data.readByte());
                short layerCount = data.readByte();
                modified.writeByte(layerCount);
                modified.writeByte(data.readByte());
                for (int l = 0; l < layerCount; l++) {
                    BlockPalette palette = new BlockPalette(BlockAir.STATE);
                    palette.readFromNetwork(data, new BlockStateRuntimeDataDeserializer());
                    List<BlockState> paletteList = (List<BlockState>) field.get(palette);
                    List<BlockState> overwritten = new ArrayList<>();
                    for (BlockState state : paletteList) {
                        overwritten.add(state != BlockAir.STATE ? Registries.BLOCKSTATE.downgrade(player.getVersion(), state) : state);
                    }
                    paletteList.clear();
                    paletteList.addAll(overwritten);
                    palette.writeToNetwork(modified, blockState -> Registries.BLOCKPALETTE.getRuntimeId(player.getVersion(), blockState));
                }
            }
            modified.writeBytes(data, data.readableBytes());
        } finally {
            field.setAccessible(false);
            data.release();
        }
        packet.setSerializedChunkData(modified);
    }
}
