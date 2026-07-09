package org.powernukkitx.anyversion.utils.table.entity;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.entity.EntityID.*;
import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.of;

public class EntityTable_1_20_0 extends EntityTable {

    public EntityTable_1_20_0() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_0,
                of(SNIFFER),
                of(CAMEL)
        );
    }
}
