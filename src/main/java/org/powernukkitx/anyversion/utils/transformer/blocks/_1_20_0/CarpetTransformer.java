package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Color;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.CARPET;

public class CarpetTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(CARPET, CommonBlockProperties.COLOR);
        Color type = switch(original.getIdentifier()) {
            case BLACK_CARPET -> Color.BLACK;
            case BLUE_CARPET -> Color.BLUE;
            case BROWN_CARPET -> Color.BROWN;
            case CYAN_CARPET -> Color.CYAN;
            case GRAY_CARPET -> Color.GRAY;
            case GREEN_CARPET -> Color.GREEN;
            case LIGHT_BLUE_CARPET -> Color.LIGHT_BLUE;
            case LIME_CARPET -> Color.LIME;
            case MAGENTA_CARPET -> Color.MAGENTA;
            case ORANGE_CARPET -> Color.ORANGE;
            case PINK_CARPET -> Color.PINK;
            case PURPLE_CARPET -> Color.PURPLE;
            case RED_CARPET -> Color.RED;
            case LIGHT_GRAY_CARPET -> Color.SILVER;
            case YELLOW_CARPET -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.COLOR.createValue(type));
    }
}
