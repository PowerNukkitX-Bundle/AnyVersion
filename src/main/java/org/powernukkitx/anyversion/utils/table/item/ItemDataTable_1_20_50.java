package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_50.PlanksTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_50.StoneTransformer;

import static org.powernukkitx.block.BlockID.STONE;
import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_50 extends ItemTable {

    public ItemDataTable_1_20_50() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_50,
                of(PLANKS, new PlanksTransformer()),
                of(STONE, new StoneTransformer())
        );
    }

}
