package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.StoneSlabType2;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class StoneBlockSlab2Transformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(STONE_BLOCK_SLAB2, CommonBlockProperties.MINECRAFT_VERTICAL_HALF, CommonBlockProperties.STONE_SLAB_TYPE_2);
        StoneSlabType2 type = switch(original.getIdentifier()) {
            case PRISMARINE_SLAB -> StoneSlabType2.PRISMARINE_ROUGH;
            case DARK_PRISMARINE_SLAB -> StoneSlabType2.PRISMARINE_DARK;
            case SMOOTH_SANDSTONE_SLAB -> StoneSlabType2.SMOOTH_SANDSTONE;
            case PURPUR_SLAB -> StoneSlabType2.PURPUR;
            case RED_NETHER_BRICK_SLAB -> StoneSlabType2.RED_NETHER_BRICK;
            case PRISMARINE_BRICK_SLAB -> StoneSlabType2.PRISMARINE_BRICK;
            case MOSSY_COBBLESTONE_SLAB -> StoneSlabType2.MOSSY_COBBLESTONE;
            default -> StoneSlabType2.RED_SANDSTONE;
        };
        return PROPERTIES.getBlockState(
                CommonBlockProperties.MINECRAFT_VERTICAL_HALF.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_VERTICAL_HALF)),
                CommonBlockProperties.STONE_SLAB_TYPE_2.createValue(type)
        );
    }

}
