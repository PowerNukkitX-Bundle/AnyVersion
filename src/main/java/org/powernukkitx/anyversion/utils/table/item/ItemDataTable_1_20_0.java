package org.powernukkitx.anyversion.utils.table.item;

import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_0.CarpetTransformer;
import org.powernukkitx.anyversion.utils.transformer.items._1_20_0.CoralTransformer;

import static org.powernukkitx.item.ItemID.*;
import static org.powernukkitx.anyversion.utils.definition.ItemDefinition.of;

public class ItemDataTable_1_20_0 extends ItemTable {

    public ItemDataTable_1_20_0() {
        super(ProtocolVersion.MINECRAFT_PE_1_20_0,
                of(CARPET, new CarpetTransformer()),
                of(CORAL, new CoralTransformer()),
                of(ANGLER_POTTERY_SHERD),
                of(ARCHER_POTTERY_SHERD),
                of(ARMS_UP_POTTERY_SHERD),
                of(BLADE_POTTERY_SHERD),
                of(BREWER_POTTERY_SHERD),
                of(BRUSH),
                of(BURN_POTTERY_SHERD),
                of(CAMEL_SPAWN_EGG),
                of(DANGER_POTTERY_SHERD),
                of(EXPLORER_POTTERY_SHERD),
                of(FRIEND_POTTERY_SHERD),
                of(HEART_POTTERY_SHERD),
                of(HEARTBREAK_POTTERY_SHERD),
                of(HOWL_POTTERY_SHERD),
                of(MINER_POTTERY_SHERD),
                of(MOURNER_POTTERY_SHERD),
                of(MUSIC_DISC_RELIC),
                of(PITCHER_POD),
                of(PLENTY_POTTERY_SHERD),
                of(PRIZE_POTTERY_SHERD),
                of(SHEAF_POTTERY_SHERD),
                of(SHELTER_POTTERY_SHERD),
                of(SKULL_POTTERY_SHERD),
                of(SNIFFER_SPAWN_EGG),
                of(SNORT_POTTERY_SHERD),
                of(CHERRY_SIGN),
                of(TORCHFLOWER_SEEDS),
                of(NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                of(COAST_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(DUNE_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(EYE_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(HOST_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(RAISER_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(RIB_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(SNORT_POTTERY_SHERD),
                of(SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(TIDE_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(VEX_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(WARD_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE),
                of(WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
        );
    }

}
