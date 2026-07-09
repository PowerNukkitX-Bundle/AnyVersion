package org.powernukkitx.anyversion.utils.table.entity;


import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.entity.EntityID.ARMADILLO;
import static org.powernukkitx.anyversion.utils.definition.EntityDefinition.of;

public class EntityTable_1_21_80 extends EntityTable {

    public EntityTable_1_21_80() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_80,
                of(ARMADILLO)
        );
    }
}
