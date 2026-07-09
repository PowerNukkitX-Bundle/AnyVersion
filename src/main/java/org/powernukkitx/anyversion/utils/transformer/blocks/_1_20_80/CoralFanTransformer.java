package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.CoralColor;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class CoralFanTransformer extends BlockStateTransformer {

    private static final String CORAL_FAN = "minecraft:coral_fan";

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CORAL_FAN, CommonBlockProperties.CORAL_COLOR, CommonBlockProperties.CORAL_FAN_DIRECTION);
        CoralColor type = switch(original.getIdentifier()) {
            case TUBE_CORAL_FAN -> CoralColor.BLUE;
            case BRAIN_CORAL_FAN -> CoralColor.PINK;
            case FIRE_CORAL_FAN -> CoralColor.RED;
            case BUBBLE_CORAL_FAN -> CoralColor.PURPLE;
            default -> CoralColor.YELLOW;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.CORAL_COLOR.createValue(type),
                CommonBlockProperties.CORAL_FAN_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.CORAL_FAN_DIRECTION)));
    }

}
