package org.powernukkitx.anyversion.utils.table.entity;


import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.*;
import static org.powernukkitx.entity.EntityID.*;

public class EntityTable_1_21_0 extends EntityTable {

    public EntityTable_1_21_0() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_0,
                of(BOGGED),
                of(BREEZE)
        );
    }
}
