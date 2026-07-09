package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.StoneSlabType3;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class DoubleStoneBlockSlab3Transformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(DOUBLE_STONE_BLOCK_SLAB3, CommonBlockProperties.MINECRAFT_VERTICAL_HALF, CommonBlockProperties.STONE_SLAB_TYPE_3);
        StoneSlabType3 type = switch(original.getIdentifier()) {
            case SMOOTH_RED_SANDSTONE_DOUBLE_SLAB -> StoneSlabType3.SMOOTH_RED_SANDSTONE;
            case POLISHED_GRANITE_DOUBLE_SLAB -> StoneSlabType3.POLISHED_GRANITE;
            case GRANITE_DOUBLE_SLAB -> StoneSlabType3.GRANITE;
            case POLISHED_DIORITE_DOUBLE_SLAB -> StoneSlabType3.POLISHED_DIORITE;
            case DIORITE_DOUBLE_SLAB -> StoneSlabType3.DIORITE;
            case POLISHED_ANDESITE_DOUBLE_SLAB -> StoneSlabType3.POLISHED_ANDESITE;
            case ANDESITE_DOUBLE_SLAB -> StoneSlabType3.ANDESITE;
            default -> StoneSlabType3.END_STONE_BRICK;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.STONE_SLAB_TYPE_3.createValue(type),
                CommonBlockProperties.MINECRAFT_VERTICAL_HALF.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_VERTICAL_HALF)));
    }

}
