package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.StructureVoidType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class StructureVoidTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(STRUCTURE_VOID, CommonBlockProperties.STRUCTURE_VOID_TYPE);
        return PROPERTIES.getBlockState(CommonBlockProperties.STRUCTURE_VOID_TYPE.createValue(StructureVoidType.VOID));
    }

}
