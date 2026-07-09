package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.CoralColor;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.CORAL_FAN_DEAD;

public class DeadCoralFanTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CORAL_FAN_DEAD, CommonBlockProperties.CORAL_COLOR, CommonBlockProperties.CORAL_FAN_DIRECTION);
        CoralColor type = switch(original.getIdentifier()) {
            case DEAD_TUBE_CORAL_FAN -> CoralColor.BLUE;
            case DEAD_BRAIN_CORAL_FAN -> CoralColor.PINK;
            case DEAD_FIRE_CORAL_FAN -> CoralColor.RED;
            case DEAD_BUBBLE_CORAL_FAN -> CoralColor.PURPLE;
            default -> CoralColor.YELLOW;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.CORAL_COLOR.createValue(type),
                CommonBlockProperties.CORAL_FAN_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.CORAL_FAN_DIRECTION)));
    }

}
