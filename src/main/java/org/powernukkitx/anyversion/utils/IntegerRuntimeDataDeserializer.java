package org.powernukkitx.anyversion.utils;

import org.powernukkitx.level.format.palette.RuntimeDataDeserializer;

public class IntegerRuntimeDataDeserializer implements RuntimeDataDeserializer<Integer> {
    @Override
    public Integer deserialize(int i) {
        return i;
    }
}
