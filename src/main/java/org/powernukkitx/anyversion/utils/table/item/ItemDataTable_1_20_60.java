package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_60.HardStainedGlassTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_60.ScuteTransformer;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_60 extends ItemTable {

    public ItemDataTable_1_20_60() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_60,
                of(TURTLE_SCUTE, new ScuteTransformer()),
                of(HARD_STAINED_GLASS, new HardStainedGlassTransformer()),
                of(HARD_STAINED_GLASS_PANE, new HardStainedGlassTransformer())
        );
    }

}
