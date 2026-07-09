package org.powernukkitx.anyversion.utils.table.entity;


import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.entity._1_21_50.BoatTransformer;

import static org.powernukkitx.entity.EntityID.*;
import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.of;

public class EntityTable_1_21_50 extends EntityTable {

    public EntityTable_1_21_50() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_50,
                of(BOAT, new BoatTransformer()),
                of(CHEST_BOAT, new BoatTransformer())
        );
    }
}
