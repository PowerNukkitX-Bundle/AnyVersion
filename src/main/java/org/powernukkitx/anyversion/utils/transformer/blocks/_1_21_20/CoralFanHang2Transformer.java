package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class CoralFanHang2Transformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CORAL_FAN_HANG2, CommonBlockProperties.CORAL_DIRECTION, CommonBlockProperties.CORAL_HANG_TYPE_BIT, CommonBlockProperties.DEAD_BIT);
        boolean dead = original.getIdentifier().startsWith("minecraft:dead_");
        boolean type = original.getIdentifier().contains("fire");
        return PROPERTIES.getBlockState(CommonBlockProperties.DEAD_BIT.createValue(dead),
                CommonBlockProperties.CORAL_HANG_TYPE_BIT.createValue(type),
                CommonBlockProperties.CORAL_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.CORAL_DIRECTION)));
    }

}
