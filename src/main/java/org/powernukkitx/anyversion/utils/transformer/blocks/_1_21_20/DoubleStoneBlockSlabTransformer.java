package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.StoneSlabType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class DoubleStoneBlockSlabTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(DOUBLE_STONE_BLOCK_SLAB, CommonBlockProperties.MINECRAFT_VERTICAL_HALF, CommonBlockProperties.STONE_SLAB_TYPE);
        StoneSlabType type = switch(original.getIdentifier()) {
            case SMOOTH_STONE_DOUBLE_SLAB -> StoneSlabType.SMOOTH_STONE;
            case SANDSTONE_DOUBLE_SLAB -> StoneSlabType.SANDSTONE;
            case PETRIFIED_OAK_DOUBLE_SLAB -> StoneSlabType.WOOD;
            case COBBLESTONE_DOUBLE_SLAB -> StoneSlabType.COBBLESTONE;
            case BRICK_DOUBLE_SLAB -> StoneSlabType.BRICK;
            case STONE_BRICK_DOUBLE_SLAB -> StoneSlabType.STONE_BRICK;
            case QUARTZ_DOUBLE_SLAB -> StoneSlabType.QUARTZ;
            default -> StoneSlabType.NETHER_BRICK;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.STONE_SLAB_TYPE.createValue(type),
                CommonBlockProperties.MINECRAFT_VERTICAL_HALF.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_VERTICAL_HALF)));
    }

}
