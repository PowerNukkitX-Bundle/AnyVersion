package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.ChiselType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class QuartzTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(QUARTZ_BLOCK, CommonBlockProperties.CHISEL_TYPE, CommonBlockProperties.PILLAR_AXIS);
        ChiselType type = switch(original.getIdentifier()) {
            case QUARTZ_PILLAR -> ChiselType.LINES;
            case CHISELED_QUARTZ_BLOCK -> ChiselType.CHISELED;
            case SMOOTH_QUARTZ -> ChiselType.SMOOTH;
            default -> ChiselType.DEFAULT;
        };
        return PROPERTIES.getBlockState(
                CommonBlockProperties.CHISEL_TYPE.createValue(type),
                CommonBlockProperties.PILLAR_AXIS.createValue(original.getPropertyValue(CommonBlockProperties.PILLAR_AXIS))
        );
    }
}
