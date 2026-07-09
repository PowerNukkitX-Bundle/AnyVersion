package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_21_90 extends BlockStateTable {

    public BlockStateTable_1_21_90() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_90,
                of(DRIED_GHAST)
        );
    }

}
