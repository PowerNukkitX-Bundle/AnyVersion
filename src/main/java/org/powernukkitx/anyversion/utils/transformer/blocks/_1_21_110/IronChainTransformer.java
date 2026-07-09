package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_110;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.property.CommonBlockProperties.PILLAR_AXIS;

public class IronChainTransformer extends BlockStateTransformer {

    public static final String CHAIN = "minecraft:chain";

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CHAIN, PILLAR_AXIS);
        return PROPERTIES.getBlockState(PILLAR_AXIS.createValue(original.getPropertyValue(PILLAR_AXIS)));
    }
}
