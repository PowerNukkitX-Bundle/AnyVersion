package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

public class ObserverTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(original.getIdentifier(), CommonBlockProperties.FACING_DIRECTION, CommonBlockProperties.POWERED_BIT);
        return PROPERTIES.getBlockState(
                CommonBlockProperties.FACING_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_FACING_DIRECTION).ordinal()),
                CommonBlockProperties.POWERED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.POWERED_BIT))
        );
    }
}
