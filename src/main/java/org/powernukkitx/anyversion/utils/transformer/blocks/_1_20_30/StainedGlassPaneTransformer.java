package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Color;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.STAINED_GLASS_PANE;

public class StainedGlassPaneTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(STAINED_GLASS_PANE, CommonBlockProperties.COLOR);
        Color type = switch(original.getIdentifier()) {
            case BLACK_STAINED_GLASS_PANE -> Color.BLACK;
            case BLUE_STAINED_GLASS_PANE -> Color.BLUE;
            case BROWN_STAINED_GLASS_PANE -> Color.BROWN;
            case CYAN_STAINED_GLASS_PANE -> Color.CYAN;
            case GRAY_STAINED_GLASS_PANE -> Color.GRAY;
            case GREEN_STAINED_GLASS_PANE -> Color.GREEN;
            case LIGHT_BLUE_STAINED_GLASS_PANE -> Color.LIGHT_BLUE;
            case LIME_STAINED_GLASS_PANE -> Color.LIME;
            case MAGENTA_STAINED_GLASS_PANE -> Color.MAGENTA;
            case ORANGE_STAINED_GLASS_PANE -> Color.ORANGE;
            case PINK_STAINED_GLASS_PANE -> Color.PINK;
            case PURPLE_STAINED_GLASS_PANE -> Color.PURPLE;
            case RED_STAINED_GLASS_PANE -> Color.RED;
            case LIGHT_GRAY_STAINED_GLASS_PANE -> Color.SILVER;
            case YELLOW_STAINED_GLASS_PANE -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.COLOR.createValue(type));
    }
}
