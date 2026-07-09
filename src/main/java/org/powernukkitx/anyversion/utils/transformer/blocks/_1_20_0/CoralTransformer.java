package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.CoralColor;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.CORAL;

public class CoralTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CORAL, CommonBlockProperties.CORAL_COLOR, CommonBlockProperties.DEAD_BIT);
        boolean dead = original.getIdentifier().startsWith("minecraft:dead_");
        CoralColor type = switch(original.getIdentifier().replace("dead_", "")) {
            case FIRE_CORAL -> CoralColor.RED;
            case BRAIN_CORAL -> CoralColor.PINK;
            case TUBE_CORAL -> CoralColor.BLUE;
            case HORN_CORAL -> CoralColor.YELLOW;
            default -> CoralColor.PURPLE;
        };
        return PROPERTIES.getBlockState(
                CommonBlockProperties.CORAL_COLOR.createValue(type),
                CommonBlockProperties.DEAD_BIT.createValue(dead)
        );
    }
}
