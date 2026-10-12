package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Test;
import org.powernukkitx.anyversion.utils.NbtMapConverter;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.nbt.tag.ListTag;

import static org.junit.jupiter.api.Assertions.*;

class NbtMapConverterTest {
    @Test
    void keepsCompoundListElementsAsNetworkNbtMapsAndPreservesEmptyListType() {
        ListTag<CompoundTag> enchantments = new ListTag<>(10);
        enchantments.add(new CompoundTag().putShort("id", 15).putShort("lvl", 2));
        CompoundTag item = new CompoundTag()
                .putList("ench", enchantments)
                .putList("custom_ench", new ListTag<CompoundTag>(10))
                .putCompound("display", new CompoundTag().putString("Name", "Custom pickaxe"));

        NbtMap converted = NbtMapConverter.toCloudburst(item);
        NbtList<?> list = (NbtList<?>) converted.get("ench");
        assertEquals(NbtType.COMPOUND, list.getType());
        assertInstanceOf(NbtMap.class, list.get(0));
        assertEquals(2, ((NbtMap) list.get(0)).getShort("lvl"));
        NbtList<?> empty = (NbtList<?>) converted.get("custom_ench");
        assertTrue(empty.isEmpty());
        assertEquals(NbtType.COMPOUND, empty.getType());
        assertEquals("Custom pickaxe", converted.getCompound("display").getString("Name"));
    }
}
