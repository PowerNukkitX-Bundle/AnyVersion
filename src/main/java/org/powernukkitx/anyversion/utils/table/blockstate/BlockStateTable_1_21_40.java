package org.powernukkitx.anyversion.utils.table.blockstate;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_40.SkullTransformer;
import org.powernukkitx.anyversion.utils.transformer.blocks._1_21_40.StemTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.anyversion.utils.definition.BlockStateDefinition.of;

public class BlockStateTable_1_21_40 extends BlockStateTable {

    public BlockStateTable_1_21_40() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_40,
                of(SKELETON_SKULL, new SkullTransformer()),
                of(PLAYER_HEAD, new SkullTransformer()),
                of(WITHER_SKELETON_SKULL, new SkullTransformer()),
                of(ZOMBIE_HEAD, new SkullTransformer()),
                of(CREEPER_HEAD, new SkullTransformer()),
                of(DRAGON_HEAD, new SkullTransformer()),
                of(PIGLIN_HEAD, new SkullTransformer()),
                of(RED_MUSHROOM_BLOCK, new StemTransformer()),
                of(BROWN_MUSHROOM_BLOCK, new StemTransformer()),
                of(MUSHROOM_STEM, new StemTransformer())
        );
    }
}
