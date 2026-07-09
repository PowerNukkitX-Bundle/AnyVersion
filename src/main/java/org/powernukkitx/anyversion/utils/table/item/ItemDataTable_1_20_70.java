package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_70.*;


import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.tags.BlockTags.GRASS;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_70 extends ItemTable {

    public ItemDataTable_1_20_70() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_70,
                of(GRASS, new GrassTransformer()),
                of(LEAVES, new LeavesTransformer()),
                of(LEAVES2, new Leaves2Transformer()),
                of(WOOD, new WoodTransformer()),
                of(WOODEN_SLAB, new WoodenSlabTransformer())
        );
    }

}
