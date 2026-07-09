package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_30.*;


import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_21_30 extends BlockStateTable {

    public BlockStateTable_1_21_30() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_30,
                of(PRISMARINE_WALL, new CobblestoneWallTransformer()),
                of(RED_SANDSTONE_WALL, new CobblestoneWallTransformer()),
                of(MOSSY_STONE_BRICK_WALL, new CobblestoneWallTransformer()),
                of(MOSSY_COBBLESTONE_WALL, new CobblestoneWallTransformer()),
                of(SANDSTONE_WALL, new CobblestoneWallTransformer()),
                of(NETHER_BRICK_WALL, new CobblestoneWallTransformer()),
                of(GRANITE_WALL, new CobblestoneWallTransformer()),
                of(RED_NETHER_BRICK_WALL, new CobblestoneWallTransformer()),
                of(STONE_BRICK_WALL, new CobblestoneWallTransformer()),
                of(END_STONE_BRICK_WALL, new CobblestoneWallTransformer()),
                of(BRICK_WALL, new CobblestoneWallTransformer()),
                of(ANDESITE_WALL, new CobblestoneWallTransformer()),
                of(DIORITE_WALL, new CobblestoneWallTransformer()),
                //Torches: COROX you forgot to fix them in b44b00125b89b5ea62934e0dec2b742808035cdf. ToDo: Replace those IDs to BlockID.**** once implemented. Shame on you Corox!
//                of("minecraft:colored_torch_purple", new ColoredTorchBpTransformer()),
//                of("minecraft:colored_torch_blue", new ColoredTorchBpTransformer()),
//                of("minecraft:colored_torch_red", new ColoredTorchRgTransformer()),
//                of("minecraft:colored_torch_green", new ColoredTorchRgTransformer()),
                //End of shaming corox
                of(PURPUR_BLOCK, new PurpurTransformer()),
                of(PURPUR_PILLAR, new PurpurTransformer()),
                of(SPONGE, new SpongeTransformer()),
                of(WET_SPONGE, new SpongeTransformer()),
                of(STRUCTURE_VOID, new StructureVoidTransformer()),
                of(TNT, new TNTTransformer())
                //Corox.. shame on you.. AGAIN
//                of("minecraft:underwater_tnt", new TNTTransformer())
                );
    }
}
