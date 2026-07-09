package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_60.CreakingHeartTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_60.DoorTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_60.FenceGateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_21_60 extends BlockStateTable {

    public BlockStateTable_1_21_60() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_60,
                of(ACACIA_DOOR, new DoorTransformer()),
                of(BAMBOO_DOOR, new DoorTransformer()),
                of(BIRCH_DOOR, new DoorTransformer()),
                of(CHERRY_DOOR, new DoorTransformer()),
                of(COPPER_DOOR, new DoorTransformer()),
                of(CRIMSON_DOOR, new DoorTransformer()),
                of(DARK_OAK_DOOR, new DoorTransformer()),
                of(EXPOSED_COPPER_DOOR, new DoorTransformer()),
                of(IRON_DOOR, new DoorTransformer()),
                of(JUNGLE_DOOR, new DoorTransformer()),
                of(ACACIA_DOOR, new DoorTransformer()),
                of(MANGROVE_DOOR, new DoorTransformer()),
                of(OXIDIZED_COPPER_DOOR, new DoorTransformer()),
                of(PALE_OAK_DOOR, new DoorTransformer()),
                of(SPRUCE_DOOR, new DoorTransformer()),
                of(WARPED_DOOR, new DoorTransformer()),
                of(WAXED_COPPER_DOOR, new DoorTransformer()),
                of(WAXED_EXPOSED_COPPER_DOOR, new DoorTransformer()),
                of(WAXED_OXIDIZED_COPPER_DOOR, new DoorTransformer()),
                of(WAXED_WEATHERED_COPPER_DOOR, new DoorTransformer()),
                of(WEATHERED_COPPER_DOOR, new DoorTransformer()),
                of(WOODEN_DOOR, new DoorTransformer()),
                of(ACACIA_FENCE_GATE, new FenceGateTransformer()),
                of(BAMBOO_FENCE_GATE, new FenceGateTransformer()),
                of(BIRCH_FENCE_GATE, new FenceGateTransformer()),
                of(CHERRY_FENCE_GATE, new FenceGateTransformer()),
                of(CRIMSON_FENCE_GATE, new FenceGateTransformer()),
                of(DARK_OAK_FENCE_GATE, new FenceGateTransformer()),
                of(FENCE_GATE, new FenceGateTransformer()),
                of(JUNGLE_FENCE_GATE, new FenceGateTransformer()),
                of(MANGROVE_FENCE_GATE, new FenceGateTransformer()),
                of(PALE_OAK_FENCE_GATE, new FenceGateTransformer()),
                of(SPRUCE_FENCE_GATE, new FenceGateTransformer()),
                of(WARPED_FENCE_GATE, new FenceGateTransformer()),
                of(CREAKING_HEART, new CreakingHeartTransformer())
        );
    }
}
