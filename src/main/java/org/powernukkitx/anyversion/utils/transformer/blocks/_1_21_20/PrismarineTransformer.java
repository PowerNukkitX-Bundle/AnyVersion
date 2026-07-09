package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.PrismarineBlockType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class PrismarineTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(PRISMARINE, CommonBlockProperties.PRISMARINE_BLOCK_TYPE);
        PrismarineBlockType type = switch(original.getIdentifier()) {
            case PRISMARINE_BRICKS -> PrismarineBlockType.BRICKS;
            case DARK_PRISMARINE -> PrismarineBlockType.DARK;
            default -> PrismarineBlockType.DEFAULT;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.PRISMARINE_BLOCK_TYPE.createValue(type));
    }

}
