package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;
import static org.powernukkitx.item.ItemID.*;

public class ItemDataTable_1_26_30 extends ItemTable {

    public ItemDataTable_1_26_30() {
        super(ProtocolVersion.MINECRAFT_PE_1_26_30,
                of(MUSIC_DISC_BOUNCE),
                of(SULFUR_CUBE_BUCKET),
                of(SULFUR_CUBE_SPAWN_EGG)
        );
    }

}
