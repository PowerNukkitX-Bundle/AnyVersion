package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks.IdentifierTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_20_70.*;


import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.tags.BlockTags.GRASS;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_20_70 extends BlockStateTable {

    public  BlockStateTable_1_20_70() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_70,
                of(OAK_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(SPRUCE_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(BIRCH_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(JUNGLE_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(ACACIA_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(DARK_OAK_DOUBLE_SLAB, new DoubleWoodenSlabTransformer()),
                of(OAK_LEAVES, new LeavesTransformer()),
                of(SPRUCE_LEAVES, new LeavesTransformer()),
                of(BIRCH_LEAVES, new LeavesTransformer()),
                of(JUNGLE_LEAVES, new LeavesTransformer()),
                of(ACACIA_LEAVES, new Leaves2Transformer()),
                of(DARK_OAK_LEAVES, new Leaves2Transformer()),
                of(OAK_SLAB, new WoodenSlabTransformer()),
                of(SPRUCE_SLAB, new WoodenSlabTransformer()),
                of(BIRCH_SLAB, new WoodenSlabTransformer()),
                of(JUNGLE_SLAB, new WoodenSlabTransformer()),
                of(ACACIA_SLAB, new WoodenSlabTransformer()),
                of(DARK_OAK_SLAB, new WoodenSlabTransformer()),
                of(OAK_WOOD, new WoodTransformer()),
                of(SPRUCE_WOOD, new WoodTransformer()),
                of(BIRCH_WOOD, new WoodTransformer()),
                of(JUNGLE_WOOD, new WoodTransformer()),
                of(ACACIA_WOOD, new WoodTransformer()),
                of(DARK_OAK_WOOD, new WoodTransformer()),
                of(STRIPPED_OAK_WOOD, new WoodTransformer()),
                of(STRIPPED_SPRUCE_WOOD, new WoodTransformer()),
                of(STRIPPED_BIRCH_WOOD, new WoodTransformer()),
                of(STRIPPED_JUNGLE_WOOD, new WoodTransformer()),
                of(STRIPPED_ACACIA_WOOD, new WoodTransformer()),
                of(STRIPPED_DARK_OAK_WOOD, new WoodTransformer()),
                of(GRASS_BLOCK, new IdentifierTransformer(GRASS))
        );
    }
}
