package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.CoralColor;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class CoralBlockTransformer extends BlockStateTransformer {

    public static final String CORAL_BLOCK = "minecraft:coral_block";

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CORAL_BLOCK, CommonBlockProperties.CORAL_COLOR, CommonBlockProperties.DEAD_BIT);
        boolean dead = original.getIdentifier().startsWith("minecraft:dead_");
        CoralColor type = switch(original.getIdentifier().replace("dead_", "")) {
            case TUBE_CORAL_BLOCK -> CoralColor.BLUE;
            case BRAIN_CORAL_BLOCK -> CoralColor.PINK;
            case BUBBLE_CORAL_BLOCK -> CoralColor.PURPLE;
            case FIRE_CORAL_BLOCK -> CoralColor.RED;
            default -> CoralColor.YELLOW;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.DEAD_BIT.createValue(dead),
                CommonBlockProperties.CORAL_COLOR.createValue(type));
    }

}
