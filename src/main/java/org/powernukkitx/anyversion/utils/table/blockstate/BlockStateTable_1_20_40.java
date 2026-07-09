package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_40.FacingToCardinalTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_20_40 extends BlockStateTable {

    public BlockStateTable_1_20_40() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_40,
                of(CHEST, new FacingToCardinalTransformer()),
                of(ENDER_CHEST, new FacingToCardinalTransformer()),
                of(STONECUTTER_BLOCK, new FacingToCardinalTransformer()),
                of(TRAPPED_CHEST, new FacingToCardinalTransformer())
        );
    }
}
