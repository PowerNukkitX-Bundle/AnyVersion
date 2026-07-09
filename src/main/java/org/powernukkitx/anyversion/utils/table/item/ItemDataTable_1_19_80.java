package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_19_80.FenceTransformer;

import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;
import static org.powernukkitx.item.ItemID.*;

public class ItemDataTable_1_19_80 extends ItemTable {

    public ItemDataTable_1_19_80() {
        super(ProtocolVersion.MINECRAFT_PE_1_19_80,
                of(FENCE, new FenceTransformer()),
                of(LOG),
                of(LOG2),
                of(BOAT),
                of(CHEST_BOAT)
        );
    }

}
