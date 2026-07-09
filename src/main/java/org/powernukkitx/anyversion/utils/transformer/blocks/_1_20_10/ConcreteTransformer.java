package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Color;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.CONCRETE;

public class ConcreteTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CONCRETE, CommonBlockProperties.COLOR);
        Color type = switch(original.getIdentifier()) {
            case BLACK_CONCRETE -> Color.BLACK;
            case BLUE_CONCRETE -> Color.BLUE;
            case BROWN_CONCRETE -> Color.BROWN;
            case CYAN_CONCRETE -> Color.CYAN;
            case GRAY_CONCRETE -> Color.GRAY;
            case GREEN_CONCRETE -> Color.GREEN;
            case LIGHT_BLUE_CONCRETE -> Color.LIGHT_BLUE;
            case LIME_CONCRETE -> Color.LIME;
            case MAGENTA_CONCRETE -> Color.MAGENTA;
            case ORANGE_CONCRETE -> Color.ORANGE;
            case PINK_CONCRETE -> Color.PINK;
            case PURPLE_CONCRETE -> Color.PURPLE;
            case RED_CONCRETE -> Color.RED;
            case LIGHT_GRAY_CONCRETE -> Color.SILVER;
            case YELLOW_CONCRETE -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.COLOR.createValue(type));
    }
}
