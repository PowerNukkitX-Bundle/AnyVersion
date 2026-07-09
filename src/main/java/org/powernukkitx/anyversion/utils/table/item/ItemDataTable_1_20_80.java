package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_80.CoralFanTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_80.RedFlowerTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_80.SaplingTransformer;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_80 extends ItemTable {

    public ItemDataTable_1_20_80() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_80,
                of(ARMADILLO_SCUTE),
                of(ARMADILLO_SPAWN_EGG),
                of(WOLF_ARMOR),
                of(FLOW_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(FLOW_POTTERY_SHERD),
                of(FLOW_BANNER_PATTERN),
                of(CORAL_FAN, new CoralFanTransformer()),
                of(CORAL_FAN_DEAD, new CoralFanTransformer()),
                of(RED_FLOWER, new RedFlowerTransformer()),
                of(SAPLING, new SaplingTransformer())
        );
    }

}
