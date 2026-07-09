package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.block.BlockID;
import org.powernukkitx.item.ItemID;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items.UnknownItemTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_0.StoneBlockSlabTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_20.*;



import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;
import static org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20.StoneBrickTransformer.STONEBRICK;
import static org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20.LightBlockTransformer.LIGHT_BLOCK;
import static org.powernukkitx.anyversion.utils.transformer.items._1_21_20.FlowerTransformer.YELLOW_FLOWER;

public class ItemDataTable_1_21_20 extends ItemTable {

    public ItemDataTable_1_21_20() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_20,
                of(BlockID.ANVIL, new AnvilTransformer()),
                of(YELLOW_FLOWER, new FlowerTransformer()),
                of(BlockID.DIRT, new DirtTransformer()),
                of(LIGHT_BLOCK, new UnknownItemTransformer()),
                of(MONSTER_EGG, new MonsterEggTransformer()),
                of(BlockID.PRISMARINE, new PrismarineTransformer()),
                of(BlockID.QUARTZ_BLOCK, new QuartzTransformer()),
                of(ItemID.RED_SANDSTONE, new SandstoneTransformer()),
                of(BlockID.SAND, new SandTransformer()),
                of(ItemID.SANDSTONE, new SandstoneTransformer()),
                of(STONEBRICK, new StoneBrickTransformer()),
                of(DOUBLE_STONE_BLOCK_SLAB, new StoneBlockSlabTransformer()),
                of(DOUBLE_STONE_BLOCK_SLAB2, new StoneBlockSlab2Transformer()),
                of(DOUBLE_STONE_BLOCK_SLAB3, new StoneBlockSlab3Transformer()),
                of(DOUBLE_STONE_BLOCK_SLAB4, new StoneBlockSlab4Transformer()),
                of(STONE_BLOCK_SLAB2, new StoneBlockSlab2Transformer()),
                of(STONE_BLOCK_SLAB3, new StoneBlockSlab3Transformer()),
                of(STONE_BLOCK_SLAB4, new StoneBlockSlab4Transformer())
        );
    }

}
