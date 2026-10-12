package org.powernukkitx.anyversion.handler.handlers;

import it.unimi.dsi.fastutil.Pair;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionData;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiomeDefinitionListHandler extends PacketHandler<BiomeDefinitionListPacket> {

    @Override
    public void handle(ProtocolPlayer player, BiomeDefinitionListPacket packet) {
        if (!packet.getBiomes().isEmpty()) {
            List<Pair<Short, BiomeDefinitionData>> biomes = new ArrayList<>(packet.getBiomes().size());
            for (Pair<Short, BiomeDefinitionData> biome : packet.getBiomes()) {
                biomes.add(Pair.of(biome.left(), sanitizeBiome(biome.right())));
            }
            packet.setBiomes(biomes);
        }

        try (var stream = AnyVersion.getPlugin().getResource("biome_definitions.nbt")) {
            packet.setDefinitions((NbtMap) NbtUtils.createNetworkReader(stream).readTag());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static BiomeDefinitionData sanitizeBiome(BiomeDefinitionData data) {
        if (data.getChunkGenData() == null) {
            return data;
        }
        return new BiomeDefinitionData(
                data.getId(),
                data.getTemperature(),
                data.getDownfall(),
                data.getRedSporeDensity(),
                data.getBlueSporeDensity(),
                data.getAshDensity(),
                data.getWhiteAshDensity(),
                data.getDepth(),
                data.getScale(),
                data.getMapWaterColor(),
                data.isRain(),
                data.getTags(),
                // Generation metadata can reference PNX runtime block IDs
                // that are not registered in the legacy codec. Preserve
                // visual biome properties without those definitions.
                null,
                data.getFoliageSnow()
        );
    }
}
