package org.powernukkitx.anyversion.utils.table.entity;


import org.powernukkitx.entity.EntityID;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.of;

public class EntityTable_1_21_110 extends EntityTable {

    public EntityTable_1_21_110() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_110,
                of(EntityID.COPPER_GOLEM)
        );
    }
}
