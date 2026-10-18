package org.powernukkitx.anyversion.utils.transformer.blocks._1_26_50;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

/**
 * Takes the corner off a stair. 1.26.50 has the server work out the corner a
 * stair forms with the stairs around it and write it into the state; before
 * that the client shaped the corner itself.
 *
 * @author xRookieFight
 * @since 14/09/2026
 */
public class StairsTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(original.getIdentifier(), CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION);
        return PROPERTIES.getBlockState(
                CommonBlockProperties.UPSIDE_DOWN_BIT.createValue(original.getPropertyValue(CommonBlockProperties.UPSIDE_DOWN_BIT)),
                CommonBlockProperties.WEIRDO_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.WEIRDO_DIRECTION))
        );
    }
}
