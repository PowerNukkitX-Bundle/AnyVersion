package org.powernukkitx.anyversion.utils;

import org.powernukkitx.block.BlockState;
import org.powernukkitx.level.format.palette.RuntimeDataDeserializer;
import org.powernukkitx.registry.Registries;

public class BlockStateRuntimeDataDeserializer implements RuntimeDataDeserializer<BlockState> {
    @Override
    public BlockState deserialize(int i) {
        return Registries.BLOCKSTATE.get(i);
    }
}
