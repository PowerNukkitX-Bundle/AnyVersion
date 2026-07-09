package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_21_0.*;


import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_21_0 extends ItemTable {

    public ItemDataTable_1_21_0() {
        super(ProtocolVersion.MINECRAFT_PE_1_21_0,
                of(STONE_BLOCK_SLAB, new StoneBlockSlabTransformer()),
                of(CORAL_BLOCK, new CoralBlockTransformer()),
                of(DOUBLE_PLANT, new DoublePlantTransformer()),
                of(TALLGRASS, new TallgrassTransformer()),
                of(LINGERING_POTION, new PotionTransformer()),
                of(POTION, new PotionTransformer()),
                of(SPLASH_POTION, new PotionTransformer()),
                of(ARROW, new PotionTransformer()),
                of(ENCHANTED_BOOK, new EnchantmentBookTransformer()),
                of(BOLT_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(BREEZE_ROD),
                of(FLOW_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(FLOW_POTTERY_SHERD),
                of(GUSTER_BANNER_PATTERN),
                of(GUSTER_POTTERY_SHERD),
                of(MACE),
                of(MUSIC_DISC_CREATOR),
                of(MUSIC_DISC_CREATOR_MUSIC_BOX),
                of(MUSIC_DISC_PRECIPICE),
                of(OMINOUS_BOTTLE),
                of(TRIAL_KEY),
                of(OMINOUS_TRIAL_KEY),
                of(SCRAPE_POTTERY_SHERD),
                of(BREEZE_SPAWN_EGG),
                of(BOGGED_SPAWN_EGG),
                of(WIND_CHARGE),
                of(CHERRY_SIGN),
                of(CHERRY_BOAT),
                of(CHERRY_CHEST_BOAT),
                of(BAMBOO_SIGN),
                of(BAMBOO_RAFT),
                of(BAMBOO_CHEST_RAFT)
        );
    }

}
