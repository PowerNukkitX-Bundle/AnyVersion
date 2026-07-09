package org.powernukkitx.anyversion.utils.transformer.blocks._1_21_20;

import org.powernukkitx.block.BlockProperties;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.CommonBlockProperties;
import org.powernukkitx.block.property.enums.MonsterEggStoneType;
import org.powernukkitx.anyversion.utils.transformer.blocks.BlockStateTransformer;

import static org.powernukkitx.block.BlockID.*;

public class MonsterEggTransformer extends BlockStateTransformer {

    @Override
    public BlockState transform(BlockState original) {
        BlockProperties PROPERTIES = new BlockProperties(MONSTER_EGG, CommonBlockProperties.MONSTER_EGG_STONE_TYPE);
        MonsterEggStoneType type = switch(original.getIdentifier()) {
            case INFESTED_COBBLESTONE -> MonsterEggStoneType.COBBLESTONE;
            case INFESTED_STONE_BRICKS -> MonsterEggStoneType.STONE_BRICK;
            case INFESTED_MOSSY_STONE_BRICKS -> MonsterEggStoneType.MOSSY_STONE_BRICK;
            case INFESTED_CRACKED_STONE_BRICKS -> MonsterEggStoneType.CRACKED_STONE_BRICK;
            case INFESTED_CHISELED_STONE_BRICKS -> MonsterEggStoneType.CHISELED_STONE_BRICK;
            default -> MonsterEggStoneType.STONE;
        };
        return PROPERTIES.getBlockState(CommonBlockProperties.MONSTER_EGG_STONE_TYPE, type);
    }

}
