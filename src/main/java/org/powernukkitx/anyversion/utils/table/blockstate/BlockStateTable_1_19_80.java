package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80.FenceTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80.NewLogTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80.OldLogTransformer;


import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80.UpdateTransformer.of;

public class BlockStateTable_1_19_80 extends BlockStateTable {

    public BlockStateTable_1_19_80() {
        super(ProtocolVersion.MINECRAFT_PE_1_19_80,
                of(UNKNOWN),
                of(OAK_FENCE, new FenceTransformer()),
                of(SPRUCE_FENCE, new FenceTransformer()),
                of(BIRCH_FENCE, new FenceTransformer()),
                of(JUNGLE_FENCE, new FenceTransformer()),
                of(ACACIA_FENCE, new FenceTransformer()),
                of(DARK_OAK_FENCE, new FenceTransformer()),
                of(OAK_LOG, new OldLogTransformer()),
                of(BIRCH_LOG, new OldLogTransformer()),
                of(SPRUCE_LOG, new OldLogTransformer()),
                of(JUNGLE_LOG, new OldLogTransformer()),
                of(ACACIA_LOG, new NewLogTransformer()),
                of(DARK_OAK_LOG, new NewLogTransformer())
        );
    }
}
