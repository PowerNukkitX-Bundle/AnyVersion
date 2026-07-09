package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_0;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.DoublePlantType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.DOUBLE_PLANT;

public class DoublePlantTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(DOUBLE_PLANT, CommonBlockProperties.DOUBLE_PLANT_TYPE, CommonBlockProperties.UPPER_BLOCK_BIT);

        DoublePlantType type = switch(original.getIdentifier()) {
            case SUNFLOWER -> DoublePlantType.SUNFLOWER;
            case LILAC -> DoublePlantType.SYRINGA;
            case TALL_GRASS -> DoublePlantType.GRASS;
            case LARGE_FERN -> DoublePlantType.FERN;
            case ROSE_BUSH -> DoublePlantType.ROSE;
            default -> DoublePlantType.PAEONIA;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.DOUBLE_PLANT_TYPE.createValue(type),
                CommonBlockProperties.UPPER_BLOCK_BIT.createValue(original.getPropertyValue(CommonBlockProperties.UPPER_BLOCK_BIT)));
    }

}
