package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.enums.SandStoneType;
import org.powernukkitx.block.property.type.EnumPropertyType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class SandstoneTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SANDSTONE, SAND_STONE_TYPE);
        SandStoneType type = switch(original.getIdentifier()) {
            case CUT_SANDSTONE -> SandStoneType.CUT;
            case CHISELED_SANDSTONE -> SandStoneType.HEIROGLYPHS;
            case SMOOTH_SANDSTONE -> SandStoneType.SMOOTH;
            default -> SandStoneType.DEFAULT;
        };
        return PROPERTIES.getBlockState(SAND_STONE_TYPE.createValue(type));
    }

    public static EnumPropertyType<SandStoneType> SAND_STONE_TYPE = EnumPropertyType.of("sand_stone_type", SandStoneType.class, SandStoneType.values()[0]);
}
