package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.*;
import cn.nukkit.level.format.palette.BlockPalette;
import cn.nukkit.level.format.palette.Palette;
import cn.nukkit.network.connection.util.HandleByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.BlockStateRuntimeDataDeserializer;
import org.powernukkitx.anyversion.utils.IntegerRuntimeDataDeserializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LevelChunkHandler extends PacketHandler<LevelChunkPacket> {

    @SneakyThrows
    @Override
    public void handle(ProtocolPlayer player, LevelChunkPacket packet) {
        ByteBuf data = packet.getData().copy();
        int total = packet.getSubChunksLength();
        HandleByteBuf modified = HandleByteBuf.of(Unpooled.buffer());
        Field field = Palette.class.getDeclaredField("palette");
        try {
            field.setAccessible(true);
            for(int i = 0; i < total; i++) {
                modified.writeByte(data.readByte()); //Version
                short layerCount = data.readByte();
                modified.writeByte(layerCount); //Layer Count
                modified.writeByte(data.readByte()); //Y coordinate
                for(int l = 0; l < layerCount; l++) {
                    BlockPalette palette1 = new BlockPalette(BlockAir.STATE);
                    palette1.readFromNetwork(data, new BlockStateRuntimeDataDeserializer());
                    List<BlockState> paletteList = (List<BlockState>) field.get(palette1);
                    List<BlockState> overwritten = new ArrayList<>();
                    for(BlockState state : paletteList) {
                        if(state != BlockAir.STATE) {
                            overwritten.add(Registries.BLOCKSTATE.downgrade(player.getVersion(), state));
                            continue;
                        }
                        overwritten.add(state);
                    }
                    field.set(palette1, overwritten);
                    palette1.writeToNetwork(modified, blockState -> Registries.BLOCKPALETTE.getRuntimeId(player.getVersion(), blockState));
                }
            }
            //Biomes and Block Entities
            modified.writeBytes(data, data.readableBytes());

            } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            field.setAccessible(false);
        }

        packet.setData(modified);
    }

}
