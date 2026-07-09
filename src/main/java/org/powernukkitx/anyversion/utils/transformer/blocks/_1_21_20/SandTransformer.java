package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.SandType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class SandTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SAND, CommonBlockProperties.SAND_TYPE);
        SandType type = switch(original.getIdentifier()) {
            case RED_SAND -> SandType.RED;
            default -> SandType.NORMAL;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.SAND_TYPE, type);
    }

}
