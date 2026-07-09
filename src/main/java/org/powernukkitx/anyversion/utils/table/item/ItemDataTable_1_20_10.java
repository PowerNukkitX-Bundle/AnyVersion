package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_10.ConcreteTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_10.ShulkerBoxTransformer;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_10 extends ItemTable {

    public ItemDataTable_1_20_10() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_10,
                of(CONCRETE, new ConcreteTransformer()),
                of(SHULKER_BOX, new ShulkerBoxTransformer())
        );
    }

}
