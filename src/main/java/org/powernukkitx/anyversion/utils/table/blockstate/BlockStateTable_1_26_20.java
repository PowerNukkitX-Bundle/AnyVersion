package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_110.IronChainTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_26_20 extends BlockStateTable {

    public BlockStateTable_1_26_20() {
        super(ProtocolVersion.MINECRAFT_PE_1_26_20,
                of(GOLDEN_DANDELION)
        );
    }

}
