package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_70;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.OldLeafType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.LEAVES;

public class LeavesTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(LEAVES, CommonBlockProperties.OLD_LEAF_TYPE, CommonBlockProperties.PERSISTENT_BIT, CommonBlockProperties.UPDATE_BIT);
        OldLeafType type = switch(original.getIdentifier()) {
            case OAK_LEAVES -> OldLeafType.OAK;
            case SPRUCE_LEAVES -> OldLeafType.SPRUCE;
            case BIRCH_LEAVES -> OldLeafType.BIRCH;
            default -> OldLeafType.JUNGLE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.OLD_LEAF_TYPE.createValue(type),
                CommonBlockProperties.PERSISTENT_BIT.createValue(original.getPropertyValue(CommonBlockProperties.PERSISTENT_BIT)),
                CommonBlockProperties.UPDATE_BIT.createValue(original.getPropertyValue(CommonBlockProperties.UPDATE_BIT))
        );
    }
}
