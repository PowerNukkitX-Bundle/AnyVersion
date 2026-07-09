package org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.OldLogType;
import org.powernukkitx.block.property.type.EnumPropertyType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.LOG;

public class OldLogTransformer extends BlockStateTransformer {

    EnumPropertyType<OldLogType> OLD_LOG_TYPE = EnumPropertyType.of("old_log_type", OldLogType.class, OldLogType.values()[0]);

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(LOG, OLD_LOG_TYPE, CommonBlockProperties.PILLAR_AXIS);
        OldLogType type = switch(original.getIdentifier()) {
            case OAK_LOG -> OldLogType.OAK;
            case SPRUCE_LOG -> OldLogType.SPRUCE;
            case BIRCH_LOG -> OldLogType.BIRCH;
            default -> OldLogType.JUNGLE;
        };
        return PROPERTIES.getBlockState(
                OLD_LOG_TYPE.createValue(type),
                CommonBlockProperties.PILLAR_AXIS.createValue(original.getPropertyValue(CommonBlockProperties.PILLAR_AXIS))
        );
    }
}
