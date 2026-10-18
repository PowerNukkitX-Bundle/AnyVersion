package org.powernukkitx.anyversion.utils.transformer.blocks._1_26_50;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.TRIP_WIRE;

/**
 * Takes the four sides off a trip wire, which unlike a fence carries state of
 * its own that has to come along.
 *
 * @author xRookieFight
 * @since 14/09/2026
 */
public class TripWireTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(TRIP_WIRE, CommonBlockProperties.ATTACHED_BIT, CommonBlockProperties.DISARMED_BIT, CommonBlockProperties.POWERED_BIT, CommonBlockProperties.SUSPENDED_BIT);
        return PROPERTIES.getBlockState(
                CommonBlockProperties.ATTACHED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.ATTACHED_BIT)),
                CommonBlockProperties.DISARMED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.DISARMED_BIT)),
                CommonBlockProperties.POWERED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.POWERED_BIT)),
                CommonBlockProperties.SUSPENDED_BIT.createValue(original.getPropertyValue(CommonBlockProperties.SUSPENDED_BIT))
        );
    }
}
