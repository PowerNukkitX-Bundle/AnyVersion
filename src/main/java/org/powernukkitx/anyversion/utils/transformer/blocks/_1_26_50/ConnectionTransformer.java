package org.powernukkitx.anyversion.utils.transformer.blocks._1_26_50;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

/**
 * Takes the four sides off a fence, a pane or a set of bars. Those blocks carry
 * the sides they connect to from 1.26.50 on and carried nothing at all before
 * it, so there is nothing left to copy over.
 *
 * @author xRookieFight
 * @since 14/09/2026
 */
public class ConnectionTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        return new BlockProperties(original.getIdentifier()).getDefaultState();
    }
}
