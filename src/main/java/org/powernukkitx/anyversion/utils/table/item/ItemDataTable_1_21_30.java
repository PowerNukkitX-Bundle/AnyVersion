package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_30.PurpurTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_30.SpongeTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_30.WallTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_30 extends ItemTable {

    public ItemDataTable_1_21_30() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_30,
                of(COBBLESTONE_WALL, new WallTransformer()),
                of(PURPUR_BLOCK, new PurpurTransformer()),
                of(SPONGE, new SpongeTransformer())
        );
    }

}
