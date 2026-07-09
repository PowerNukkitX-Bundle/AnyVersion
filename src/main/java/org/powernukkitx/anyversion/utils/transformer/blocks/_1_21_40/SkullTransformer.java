package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_40;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.SKULL;

public class SkullTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SKULL, CommonBlockProperties.FACING_DIRECTION);
        return PROPERTIES.getBlockState(CommonBlockProperties.FACING_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.FACING_DIRECTION)));
    }
}
