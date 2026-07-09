package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_21_70 extends BlockStateTable {

    public BlockStateTable_1_21_70() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_70,
                of(BUSH),
                of(CACTUS_FLOWER),
                of(FIREFLY_BUSH),
                of(LEAF_LITTER),
                of(SHORT_DRY_GRASS),
                of(TALL_DRY_GRASS),
                of(WILDFLOWERS));
    }

}
