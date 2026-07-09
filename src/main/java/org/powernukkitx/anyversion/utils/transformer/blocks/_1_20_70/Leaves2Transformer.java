package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_70;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.NewLeafType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.LEAVES2;

public class Leaves2Transformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(LEAVES2, CommonBlockProperties.NEW_LEAF_TYPE, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT);
        NewLeafType type = switch(original.getIdentifier()) {
            case ACACIA_LEAVES -> NewLeafType.ACACIA;
            default -> NewLeafType.DARK_OAK;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.NEW_LEAF_TYPE.createValue(type),
                CommonBlockProperties.PERSISTENT_BIT.createValue(original.getPropertyValue(CommonBlockProperties.PERSISTENT_BIT)),
                CommonBlockProperties.UPDATE_BIT.createValue(original.getPropertyValue(CommonBlockProperties.UPDATE_BIT))
        );
    }
}
