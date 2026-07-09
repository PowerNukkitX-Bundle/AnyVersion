package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.MinecraftVerticalHalf;
import org.powernukkitx.block.property.type.BooleanPropertyType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

public class BasicSlabTransformer extends BlockStateTransformer {

    BooleanPropertyType TOP_SLOT_BIT = BooleanPropertyType.of("top_slot_bit", false);

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(original.getIdentifier(), TOP_SLOT_BIT);
        return PROPERTIES.getBlockState(TOP_SLOT_BIT.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_VERTICAL_HALF) == MinecraftVerticalHalf.TOP));
    }
}
