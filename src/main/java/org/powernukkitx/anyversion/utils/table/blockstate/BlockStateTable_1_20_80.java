package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80.CoralFanTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80.DeadCoralFanTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80.SaplingTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80.RedFlowerTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.block.BlockID.DEAD_FIRE_CORAL_FAN;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_20_80 extends BlockStateTable {

    public BlockStateTable_1_20_80() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_80,
                of(ACACIA_SAPLING, new SaplingTransformer()),
                of(BIRCH_SAPLING, new SaplingTransformer()),
                of(DARK_OAK_SAPLING, new SaplingTransformer()),
                of(JUNGLE_SAPLING, new SaplingTransformer()),
                of(OAK_SAPLING, new SaplingTransformer()),
                of(SPRUCE_SAPLING, new SaplingTransformer()),
                of(ALLIUM, new RedFlowerTransformer()),
                of(CORNFLOWER, new RedFlowerTransformer()),
                of(AZURE_BLUET, new RedFlowerTransformer()),
                of(LILY_OF_THE_VALLEY, new RedFlowerTransformer()),
                of(BLUE_ORCHID, new RedFlowerTransformer()),
                of(OXEYE_DAISY, new RedFlowerTransformer()),
                of(POPPY, new RedFlowerTransformer()),
                of(ORANGE_TULIP, new RedFlowerTransformer()),
                of(PINK_TULIP, new RedFlowerTransformer()),
                of(WHITE_TULIP, new RedFlowerTransformer()),
                of(RED_TULIP, new RedFlowerTransformer()),
                of(TUBE_CORAL_FAN, new CoralFanTransformer()),
                of(BRAIN_CORAL_FAN, new CoralFanTransformer()),
                of(BUBBLE_CORAL_FAN, new CoralFanTransformer()),
                of(HORN_CORAL_FAN, new CoralFanTransformer()),
                of(FIRE_CORAL_FAN, new CoralFanTransformer()),
                of(DEAD_TUBE_CORAL_FAN, new DeadCoralFanTransformer()),
                of(DEAD_BRAIN_CORAL_FAN, new DeadCoralFanTransformer()),
                of(DEAD_BUBBLE_CORAL_FAN, new DeadCoralFanTransformer()),
                of(DEAD_HORN_CORAL_FAN, new DeadCoralFanTransformer()),
                of(DEAD_FIRE_CORAL_FAN, new DeadCoralFanTransformer())
        );
    }
}
