package org.powernukkitx.anyversion.utils.transformer.blocks._1_19_80;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.NewLogType;
import org.powernukkitx.block.property.type.EnumPropertyType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;
import static org.powernukkitx.item.ItemID.LOG;

public class NewLogTransformer extends BlockStateTransformer {

    EnumPropertyType<NewLogType> NEW_LOG_TYPE = EnumPropertyType.of("new_log_type", NewLogType.class, NewLogType.values()[0]);

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(LOG, NEW_LOG_TYPE, CommonBlockProperties.PILLAR_AXIS);
        NewLogType type = switch(original.getIdentifier()) {
            case ACACIA_LOG -> NewLogType.ACACIA;
            default -> NewLogType.DARK_OAK;
        };
        return PROPERTIES.getBlockState(
                NEW_LOG_TYPE.createValue(type),
                CommonBlockProperties.PILLAR_AXIS.createValue(original.getPropertyValue(CommonBlockProperties.PILLAR_AXIS))
        );
    }
}
