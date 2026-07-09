package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Color;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.STAINED_HARDENED_CLAY;

public class StainedHardenedClayTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(STAINED_HARDENED_CLAY, CommonBlockProperties.COLOR);
        Color type = switch(original.getIdentifier()) {
            case BLACK_TERRACOTTA -> Color.BLACK;
            case BLUE_TERRACOTTA -> Color.BLUE;
            case BROWN_TERRACOTTA -> Color.BROWN;
            case CYAN_TERRACOTTA -> Color.CYAN;
            case GRAY_TERRACOTTA -> Color.GRAY;
            case GREEN_TERRACOTTA -> Color.GREEN;
            case LIGHT_BLUE_TERRACOTTA -> Color.LIGHT_BLUE;
            case LIME_TERRACOTTA -> Color.LIME;
            case MAGENTA_TERRACOTTA -> Color.MAGENTA;
            case ORANGE_TERRACOTTA -> Color.ORANGE;
            case PINK_TERRACOTTA -> Color.PINK;
            case PURPLE_TERRACOTTA -> Color.PURPLE;
            case RED_TERRACOTTA -> Color.RED;
            case LIGHT_GRAY_TERRACOTTA -> Color.SILVER;
            case YELLOW_TERRACOTTA -> Color.YELLOW;
            default -> Color.WHITE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.COLOR.createValue(type));
    }
}
