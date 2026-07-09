package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_110 extends ItemTable {

    public ItemDataTable_1_21_110() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_110,
                of(COPPER_GOLEM_SPAWN_EGG),
                of(COPPER_NUGGET),
                of(COPPER_BOOTS),
                of(COPPER_CHESTPLATE),
                of(COPPER_HELMET),
                of(COPPER_HORSE_ARMOR),
                of(COPPER_LEGGINGS),
                of(COPPER_AXE),
                of(COPPER_HOE),
                of(COPPER_PICKAXE),
                of(COPPER_SHOVEL),
                of(COPPER_SWORD)
        );
    }

}
