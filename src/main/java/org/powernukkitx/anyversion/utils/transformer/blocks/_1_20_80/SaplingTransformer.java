package org.powernukkitx.anyversion.utils.transformer.blocks._1_20_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.type.EnumPropertyType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class SaplingTransformer extends BlockStateTransformer {

    private static final String SAPLING = "minecraft:sapling";

    EnumPropertyType<SaplingType> SAPLING_TYPE = EnumPropertyType.of("sapling_type", SaplingType.class, SaplingType.values()[0]);

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SAPLING, CommonBlockProperties.AGE_BIT, SAPLING_TYPE);
        SaplingType type = switch(original.getIdentifier()) {
            case ACACIA_SAPLING -> SaplingType.ACACIA;
            case BIRCH_SAPLING -> SaplingType.BIRCH;
            case DARK_OAK_SAPLING -> SaplingType.DARK_OAK;
            case JUNGLE_SAPLING -> SaplingType.JUNGLE;
            case SPRUCE_SAPLING -> SaplingType.SPRUCE;
            default -> SaplingType.OAK;
        };
        return PROPERTIES.getBlockState(SAPLING_TYPE.createValue(type),
                CommonBlockProperties.AGE_BIT.createValue(original.getPropertyValue(CommonBlockProperties.AGE_BIT)));
    }



    public enum SaplingType {
        ACACIA,

        BIRCH,

        DARK_OAK,

        JUNGLE,

        OAK,

        SPRUCE
    }

}
