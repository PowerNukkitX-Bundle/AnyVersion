package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class TNTTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(TNT, CommonBlockProperties.EXPLODE_BIT, CommonBlockProperties.ALLOW_UNDERWATER_BIT);
        boolean type = original.getIdentifier().equals("minecraft:underwater_tnt");
        return PROPERTIES.getBlockState(
                CommonBlockProperties.ALLOW_UNDERWATER_BIT.createValue(type),
                CommonBlockProperties.EXPLODE_BIT.createValue(original.getPropertyValue(CommonBlockProperties.EXPLODE_BIT))
        );
    }

}
