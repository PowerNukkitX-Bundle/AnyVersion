package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_10;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Color;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.SHULKER_BOX;

public class ShulkerBoxTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SHULKER_BOX, CommonBlockProperties.COLOR);
        Color type = switch(original.getIdentifier()) {
            case BLACK_SHULKER_BOX -> Color.BLACK;
            case BLUE_SHULKER_BOX -> Color.BLUE;
            case BROWN_SHULKER_BOX -> Color.BROWN;
            case CYAN_SHULKER_BOX -> Color.CYAN;
            case GRAY_SHULKER_BOX -> Color.GRAY;
            case GREEN_SHULKER_BOX -> Color.GREEN;
            case LIGHT_BLUE_SHULKER_BOX -> Color.LIGHT_BLUE;
            case LIME_SHULKER_BOX -> Color.LIME;
            case MAGENTA_SHULKER_BOX -> Color.MAGENTA;
            case ORANGE_SHULKER_BOX -> Color.ORANGE;
            case PINK_SHULKER_BOX -> Color.PINK;
            case PURPLE_SHULKER_BOX -> Color.PURPLE;
            case RED_SHULKER_BOX -> Color.RED;
            case LIGHT_GRAY_SHULKER_BOX -> Color.SILVER;
            case YELLOW_SHULKER_BOX -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.COLOR.createValue(type));
    }
}
