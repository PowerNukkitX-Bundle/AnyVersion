package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

public class SculkSensorTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(original.getIdentifier(), CommonBlockProperties.POWERED_BIT);
        return PROPERTIES.getBlockState(CommonBlockProperties.POWERED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.SCULK_SENSOR_PHASE) != 0));
    }
}
