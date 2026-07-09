package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

public class LightBlockTransformer extends BlockStateTransformer {

    public static final String LIGHT_BLOCK = "minecraft:light_block";

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(LIGHT_BLOCK, CommonBlockProperties.BLOCK_LIGHT_LEVEL);
        int lightLevel = Integer.parseInt(original.getIdentifier().replace(LIGHT_BLOCK + "_", ""));
        return PROPERTIES.getBlockState(CommonBlockProperties.BLOCK_LIGHT_LEVEL.createValue(lightLevel));
    }
}
