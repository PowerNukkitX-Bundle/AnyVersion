package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_70 extends ItemTable {

    public ItemDataTable_1_21_70() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_70,
                of(BLUE_EGG),
                of(BROWN_EGG)
        );
    }

}
