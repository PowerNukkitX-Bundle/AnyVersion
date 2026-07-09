package org.powernukkitx.anyversion.utils.transformer.blocks;

import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.BlockUnknown;

public class UnknownStateTransformer extends BlockStateTransformer {
    @Override
    public BlockState transform(BlockState original) {
        return BlockUnknown.PROPERTIES.getDefaultState();
    }
}
