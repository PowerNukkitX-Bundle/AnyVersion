package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_40;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class StemTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        String identifier = switch (original.getIdentifier()) {
            case RED_MUSHROOM_BLOCK -> RED_MUSHROOM_BLOCK;
            default -> BROWN_MUSHROOM_BLOCK;
        };
        BlockProperties PROPERTIES = new BlockProperties(identifier, CommonBlockProperties.HUGE_MUSHROOM_BITS);
        int bits = switch (original.getIdentifier()) {
            case MUSHROOM_STEM -> 15;
            default -> original.getPropertyValue(CommonBlockProperties.HUGE_MUSHROOM_BITS);
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.HUGE_MUSHROOM_BITS.createValue(bits));
    }
}
