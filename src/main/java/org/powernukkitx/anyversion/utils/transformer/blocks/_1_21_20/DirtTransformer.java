package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.DirtType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class DirtTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(DIRT, CommonBlockProperties.DIRT_TYPE);
        DirtType type = switch(original.getIdentifier()) {
            case COARSE_DIRT -> DirtType.COARSE;
            default -> DirtType.NORMAL;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.DIRT_TYPE, type);
    }

}
