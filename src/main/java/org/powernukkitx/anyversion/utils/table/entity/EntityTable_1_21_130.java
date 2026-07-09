package org.powernukkitx.anyversion.utils.table.entity;


import org.powernukkitx.entity.EntityID;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.of;

public class EntityTable_1_21_130 extends EntityTable {

    public EntityTable_1_21_130() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_130,
                of(EntityID.PARCHED),
                of(EntityID.CAMEL_HUSK),
                of(EntityID.NAUTILUS),
                of(EntityID.ZOMBIE_NAUTILUS)
        );
    }
}
