package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.enums.SandStoneType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20.SandstoneTransformer.SAND_STONE_TYPE;

public class RedSandstoneTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(RED_SANDSTONE, SAND_STONE_TYPE);

        SandStoneType type = switch(original.getIdentifier()) {
            case CUT_RED_SANDSTONE -> SandStoneType.CUT;
            case CHISELED_RED_SANDSTONE -> SandStoneType.HEIROGLYPHS;
            case SMOOTH_RED_SANDSTONE -> SandStoneType.SMOOTH;
            default -> SandStoneType.DEFAULT;
        };
        return PROPERTIES.getBlockState(SAND_STONE_TYPE.createValue(type));
    }
}
