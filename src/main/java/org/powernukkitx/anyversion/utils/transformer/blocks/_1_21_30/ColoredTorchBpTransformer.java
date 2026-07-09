package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.COLORED_TORCH_PURPLE;

public class ColoredTorchBpTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(COLORED_TORCH_PURPLE, CommonBlockProperties.COLOR_BIT, CommonBlockProperties.TORCH_FACING_DIRECTION);
        boolean type = original.getIdentifier().equals("minecraft:colored_torch_purple");
        return PROPERTIES.getBlockState(
                CommonBlockProperties.COLOR_BIT.createValue(type),
                CommonBlockProperties.TORCH_FACING_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.TORCH_FACING_DIRECTION))
        );
    }

}
