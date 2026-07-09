package org.powernukkitx.anyversion.utils.palette;

import org.powernukkitx.block.BlockState;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class BlockHashBlockPalette extends BlockPalette {

    public BlockHashBlockPalette() {
        super(ProtocolVersion.getCurrent());
    }

    @Override
    public int getRuntimeId(BlockState state) {
        return state.blockStateHash();
    }
}
