package org.powernukkitx.anyversion.utils.transformer.blocks;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IdentifierTransformer extends BlockStateTransformer {

    public final String identifier;

    @Override
    public BlockState transform(BlockState original) {
        return new BlockProperties(identifier).getDefaultState();
    }
}
