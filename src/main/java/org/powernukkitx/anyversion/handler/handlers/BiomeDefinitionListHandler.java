package org.powernukkitx.anyversion.handler.handlers;

import it.unimi.dsi.fastutil.Pair;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionChunkGenData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeSurfaceBuilderData;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;
import org.powernukkitx.anyversion.AnyVersion;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiomeDefinitionListHandler extends PacketHandler<BiomeDefinitionListPacket> {

    private static final BiomeSurfaceBuilderData EMPTY_SURFACE_BUILDER = new BiomeSurfaceBuilderData(
            null,
            false,
            false,
            false,
            false,
            null,
            null,
            null
    );

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

    private BiomeDefinitionData sanitizeBiome(BiomeDefinitionData data) {
        BiomeDefinitionChunkGenData chunkGenData = data.getChunkGenData();
        if (chunkGenData == null || chunkGenData.getSurfaceBuilderData() != null) {
            return data;
        }

        BiomeDefinitionChunkGenData sanitizedChunkGenData = new BiomeDefinitionChunkGenData(
                chunkGenData.getClimate(),
                chunkGenData.getConsolidatedFeatures(),
                chunkGenData.getMountainParams(),
                chunkGenData.getSurfaceMaterialAdjustment(),
                EMPTY_SURFACE_BUILDER,
                chunkGenData.getOverworldGenRules(),
                chunkGenData.getMultinoiseGenRules(),
                chunkGenData.getLegacyWorldGenRules(),
                chunkGenData.getReplacementData(),
                chunkGenData.getReplacementBiomes(),
                chunkGenData.getVillageType(),
                chunkGenData.getSubSurfaceBuilderData()
        );

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
                sanitizedChunkGenData,
                data.getFoliageSnow()
        );
    }
}
