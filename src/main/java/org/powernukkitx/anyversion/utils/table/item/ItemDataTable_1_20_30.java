package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_30.ConcretePowderTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_30.StainedGlassPaneTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_30.StainedGlassTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_30.TerracottaTransformer;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_30 extends ItemTable {

    public ItemDataTable_1_20_30() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_30,
                of(CONCRETE_POWDER, new ConcretePowderTransformer()),
                of(STAINED_HARDENED_CLAY, new TerracottaTransformer()),
                of(STAINED_GLASS, new StainedGlassTransformer()),
                of(STAINED_GLASS_PANE, new StainedGlassPaneTransformer())
        );
    }

}
