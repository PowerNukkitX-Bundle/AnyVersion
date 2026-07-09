package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.TallGrassType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.TALLGRASS;

public class TallgrassTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(TALLGRASS, CommonBlockProperties.TALL_GRASS_TYPE);

        TallGrassType type = switch(original.getIdentifier()) {
            case FERN -> TallGrassType.FERN;
            default -> TallGrassType.DEFAULT;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.TALL_GRASS_TYPE.createValue(type));
    }

}
