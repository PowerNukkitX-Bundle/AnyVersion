package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.nbt.NbtMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionChunkGenData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeDefinitionData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeSurfaceBuilderData;
import org.cloudburstmc.protocol.bedrock.data.biome.BiomeSurfaceMaterialData;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.net.ItemStackLegacyRequestId;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.InventoryTransaction;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseActionType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseClientCooldownState;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUsePredictedResult;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.ItemUseTriggerType;
import org.cloudburstmc.protocol.bedrock.data.payload.inventory.transaction.data.ItemUseInventoryTransaction;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;
import org.cloudburstmc.protocol.bedrock.packet.BiomeDefinitionListPacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import it.unimi.dsi.fastutil.Pair;
import org.junit.jupiter.api.Test;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.nbt.tag.ListTag;

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

class LegacyPacketCompatibilityTest {
    @Test
    void suppliesMainHandForLegacyItemUsePacketsBeforeNewCodecEncoding() {
        ItemUseInventoryTransaction use = new ItemUseInventoryTransaction();
        use.setActions(new InventoryTransaction());
        use.setActionType(ItemUseActionType.USE);
        use.setTriggerType(ItemUseTriggerType.PLAYER_INPUT);
        use.setPosition(Vector3i.ZERO);
        CompoundTag pickaxeNbt = new CompoundTag();
        pickaxeNbt.putList("custom_ench", new ListTag<CompoundTag>(10));
        ListTag<CompoundTag> vanillaEnchantments = new ListTag<>(10);
        vanillaEnchantments.add(new CompoundTag().putShort("id", 15).putShort("lvl", 2));
        pickaxeNbt.putList("ench", vanillaEnchantments);
        use.setItem(ItemData.builder()
                .definition(new SimpleItemDefinition("minecraft:stone_pickaxe", 316, false))
                .tag(InventoryTransactionHandler.deepcopy(pickaxeNbt))
                .build());
        use.setFromPosition(Vector3f.ZERO);
        use.setClickPosition(Vector3f.ZERO);
        use.setTargetBlockId(new SimpleBlockDefinition("minecraft:air", 0, NbtMap.EMPTY));
        use.setClientInteractPrediction(ItemUsePredictedResult.SUCCESS);
        use.setClientCooldownState(ItemUseClientCooldownState.ON);
        InventoryTransactionPacket packet = new InventoryTransactionPacket();
        packet.setLegacyRequestID(new ItemStackLegacyRequestId(0));
        packet.setTransaction(use);

        new InventoryTransactionHandler().handle(packetPlayer(), packet);

        assertEquals(org.cloudburstmc.protocol.bedrock.data.HandSlot.MAINHAND, use.getHand());
        ByteBuf encoded = Unpooled.buffer();
        try {
            assertDoesNotThrow(() -> ProtocolVersion.get(2192).codec().tryEncode(
                    ProtocolVersion.get(2192).helper(), encoded, packet));
        } finally {
            encoded.release();
        }
    }

    @Test
    void stripsRuntimeBlockDefinitionsFromOldClientBiomeChunkGenerationData() {
        var unknownBlock = new SimpleBlockDefinition("minecraft:stone", 999_999, NbtMap.EMPTY);
        var surface = new BiomeSurfaceBuilderData(
                new BiomeSurfaceMaterialData(unknownBlock, null, null, null, null, 0),
                false, false, false, false, null, null, null);
        var chunkGen = new BiomeDefinitionChunkGenData(
                null, List.of(), null, null, surface, null, null, null, null,
                List.of(), null, surface);
        var biome = new BiomeDefinitionData(
                (short) 1, 0.5f, 0.5f, 0, 0, 0, 0, 0, 0,
                Color.BLUE, true, List.of(), chunkGen, 0);

        BiomeDefinitionData sanitized = BiomeDefinitionListHandler.sanitizeBiome(biome);

        assertNull(sanitized.getChunkGenData());
        assertEquals(biome.getId(), sanitized.getId());
        assertEquals(biome.getMapWaterColor(), sanitized.getMapWaterColor());

        BiomeDefinitionListPacket packet = new BiomeDefinitionListPacket();
        packet.setDefinitions(NbtMap.EMPTY);
        packet.setBiomes(List.of(Pair.of((short) 1, sanitized)));
        ByteBuf encoded = Unpooled.buffer();
        try {
            assertDoesNotThrow(() -> ProtocolVersion.get(2169).codec().tryEncode(
                    ProtocolVersion.get(2169).helper(), encoded, packet));
        } finally {
            encoded.release();
        }
    }

    @Test
    void fillsMissingContainerTypeBeforeNewCodecEncoding() {
        ContainerClosePacket packet = new ContainerClosePacket();
        packet.setContainerID((byte) 0);

        new ContainerCloseHandler().handle(packetPlayer(), packet);

        assertEquals(ContainerType.NONE, packet.getContainerType());
        ByteBuf encoded = Unpooled.buffer();
        try {
            assertDoesNotThrow(() -> ProtocolVersion.get(2192).codec().tryEncode(
                    ProtocolVersion.get(2192).helper(), encoded, packet));
        } finally {
            encoded.release();
        }
    }

    private static ProtocolPlayer packetPlayer() {
        return new ProtocolPlayer(null, 2169);
    }
}
