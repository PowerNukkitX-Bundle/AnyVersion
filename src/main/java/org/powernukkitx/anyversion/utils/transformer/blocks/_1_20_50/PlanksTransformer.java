package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_50;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.WoodType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.PLANKS;

public class PlanksTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(PLANKS, CommonBlockProperties.WOOD_TYPE);
        WoodType type = switch(original.getIdentifier()) {
            case OAK_PLANKS -> WoodType.OAK;
            case SPRUCE_PLANKS -> WoodType.SPRUCE;
            case BIRCH_PLANKS -> WoodType.BIRCH;
            case JUNGLE_PLANKS-> WoodType.JUNGLE;
            case ACACIA_PLANKS -> WoodType.ACACIA;
            default -> WoodType.DARK_OAK;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.WOOD_TYPE.createValue(type));
    }
}
