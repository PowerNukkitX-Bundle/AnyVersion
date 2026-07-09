package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_50.PlanksTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_50.StoneTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_20_50 extends BlockStateTable {

    public BlockStateTable_1_20_50() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_50,
                of(OAK_PLANKS, new PlanksTransformer()),
                of(SPRUCE_PLANKS, new PlanksTransformer()),
                of(BIRCH_PLANKS, new PlanksTransformer()),
                of(JUNGLE_PLANKS, new PlanksTransformer()),
                of(ACACIA_PLANKS, new PlanksTransformer()),
                of(DARK_OAK_PLANKS, new PlanksTransformer()),
                of(STONE, new StoneTransformer()),
                of(DIORITE, new StoneTransformer()),
                of(GRANITE, new StoneTransformer()),
                of(ANDESITE, new StoneTransformer()),
                of(POLISHED_DIORITE, new StoneTransformer()),
                of(POLISHED_GRANITE, new StoneTransformer()),
                of(POLISHED_ANDESITE, new StoneTransformer())
        );
    }
}
