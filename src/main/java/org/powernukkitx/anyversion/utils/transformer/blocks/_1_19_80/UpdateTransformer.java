package org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.anyversion.utils.definition.BlockStateDefinition;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class UpdateTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(INFO_UPDATE);
        return PROPERTIES.getDefaultState();
    }

    //For versions pre 1.19.80, use this "of" method!
    public static BlockStateDefinition of(String state) {
        return of(state, new UpdateTransformer());
    }

    //For versions pre 1.19.80, use this "of" method!
    public static BlockStateDefinition of(String state, BlockStateTransformer transformer) {
        return new BlockStateDefinition(state, transformer);
    }
}
