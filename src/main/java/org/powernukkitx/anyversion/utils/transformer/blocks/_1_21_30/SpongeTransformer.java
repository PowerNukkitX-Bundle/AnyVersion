package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_30;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.SpongeType;
import lombok.extern.slf4j.Slf4j;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

@Slf4j
public class SpongeTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(SPONGE, CommonBlockProperties.SPONGE_TYPE);
        SpongeType type = switch(original.getIdentifier()) {
            case WET_SPONGE -> SpongeType.WET;
            default -> SpongeType.DRY;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.SPONGE_TYPE.createValue(type));
    }

}
