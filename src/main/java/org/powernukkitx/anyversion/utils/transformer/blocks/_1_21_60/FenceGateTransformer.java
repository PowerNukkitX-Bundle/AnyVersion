package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_60;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

public class FenceGateTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(original.getIdentifier(), CommonBlockProperties.DIRECTION, CommonBlockProperties.IN_WALL_BIT, CommonBlockProperties.OPEN_BIT);
        int direction = switch (original.getPropertyValue(CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION)) {
            case SOUTH -> 0;
            case WEST -> 1;
            case NORTH -> 2;
            case EAST -> 3;
        };
        return PROPERTIES.getBlockState(
                CommonBlockProperties.DIRECTION.createValue(direction),
                CommonBlockProperties.IN_WALL_BIT.createValue(original.getPropertyValue(CommonBlockProperties.IN_WALL_BIT)),
                CommonBlockProperties.OPEN_BIT.createValue(original.getPropertyValue(CommonBlockProperties.OPEN_BIT))
        );
    }
}
