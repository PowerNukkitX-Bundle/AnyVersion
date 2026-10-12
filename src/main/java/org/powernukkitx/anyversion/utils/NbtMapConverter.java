package org.powernukkitx.anyversion.utils;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.powernukkitx.nbt.tag.ByteTag;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.nbt.tag.ListTag;
import org.powernukkitx.nbt.tag.ShortTag;
import org.powernukkitx.nbt.tag.Tag;

/** Converts PNX NBT tags to the Cloudburst NBT representation used by Bedrock packets. */
public final class NbtMapConverter {
    private NbtMapConverter() {}

    public static NbtMap toCloudburst(CompoundTag tag) {
        NbtMapBuilder builder = NbtMap.builder();
        for (var entry : tag.getTags().entrySet()) {
            String key = entry.getKey();
            Tag value = entry.getValue();
            if (value instanceof CompoundTag compound) {
                builder.put(key, toCloudburst(compound));
            } else if (value instanceof ListTag<?> list) {
                // parseValue() yields Java maps for compound list entries. Cloudburst
                // requires NbtMap elements and the network conversion preserves list type.
                builder.put(key, list.toNetwork());
            } else if (value instanceof ByteTag byteTag) {
                builder.putByte(key, (byte) (byteTag.data & 0xFF));
            } else if (value instanceof ShortTag shortTag) {
                builder.putShort(key, shortTag.data);
            } else {
                builder.put(key, value.parseValue());
            }
        }
        return builder.build();
    }
}
