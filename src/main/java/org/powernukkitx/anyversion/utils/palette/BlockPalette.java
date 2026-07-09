package org.powernukkitx.anyversion.utils.palette;

import org.powernukkitx.block.BlockState;
import lombok.Getter;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public abstract class BlockPalette {

    @Getter
    protected final ProtocolVersion version;

    protected BlockPalette(ProtocolVersion version)  {
        this.version = version;
    }

    public abstract int getRuntimeId(BlockState state);
}
