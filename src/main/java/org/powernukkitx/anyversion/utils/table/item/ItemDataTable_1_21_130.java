package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_130 extends ItemTable {

    public ItemDataTable_1_21_130() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_130,
                of(CAMEL_HUSK_SPAWN_EGG),
                of(COPPER_NAUTILUS_ARMOR),
                of(COPPER_SPEAR),
                of(DIAMOND_NAUTILUS_ARMOR),
                of(DIAMOND_SPEAR),
                of(GOLDEN_NAUTILUS_ARMOR),
                of(GOLDEN_SPEAR),
                of(IRON_NAUTILUS_ARMOR),
                of(IRON_SPEAR),
                of(NAUTILUS_SPAWN_EGG),
                of(NETHERITE_HORSE_ARMOR),
                of(NETHERITE_NAUTILUS_ARMOR),
                of(NETHERITE_SPEAR),
                of(PARCHED_SPAWN_EGG),
                of(STONE_SPEAR),
                of(WOODEN_SPEAR),
                of(ZOMBIE_NAUTILUS_SPAWN_EGG)
        );
    }

}
