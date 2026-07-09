package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.StoneBrickType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class StoneBrickTransformer extends BlockStateTransformer {

    public static final String STONEBRICK = "minecraft:stonebrick";

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(STONEBRICK, CommonBlockProperties.STONE_BRICK_TYPE);
        StoneBrickType type = switch(original.getIdentifier()) {
            case MOSSY_STONE_BRICKS -> StoneBrickType.MOSSY;
            case CRACKED_STONE_BRICKS -> StoneBrickType.CRACKED;
            case CHISELED_STONE_BRICKS -> StoneBrickType.CHISELED;
            default -> StoneBrickType.DEFAULT;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.STONE_BRICK_TYPE, type);
    }

}
