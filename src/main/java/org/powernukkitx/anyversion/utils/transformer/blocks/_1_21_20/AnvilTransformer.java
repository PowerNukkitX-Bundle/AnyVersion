package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.Damage;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class AnvilTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(ANVIL, CommonBlockProperties.DAMAGE, CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION);
        Damage type = switch(original.getIdentifier()) {
            case CHIPPED_ANVIL -> Damage.SLIGHTLY_DAMAGED;
            case DAMAGED_ANVIL -> Damage.VERY_DAMAGED;
            default -> Damage.UNDAMAGED;
        };
        return PROPERTIES.getBlockState(
                CommonBlockProperties.DAMAGE.createValue(type),
                CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION.createValue(original.getPropertyValue(CommonBlockProperties.MINECRAFT_CARDINAL_DIRECTION))
        );
    }
}
