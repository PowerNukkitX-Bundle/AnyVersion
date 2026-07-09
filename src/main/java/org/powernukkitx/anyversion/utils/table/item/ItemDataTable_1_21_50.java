package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_50 extends ItemTable {

    public ItemDataTable_1_21_50() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_50,
                of(CREAKING_SPAWN_EGG),
                of(PALE_OAK_BOAT),
                of(PALE_OAK_CHEST_BOAT),
                of(PALE_OAK_HANGING_SIGN),
                of(PALE_OAK_SIGN),
                of(RESIN_BRICK)
        );
    }

}
