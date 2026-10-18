package org.powernukkitx.anyversion.handler.handlers;

import org.junit.jupiter.api.Test;
import org.powernukkitx.item.Item;
import static org.junit.jupiter.api.Assertions.*;

class SurvivalHandCompatibilityTest {
    @org.junit.jupiter.api.BeforeAll
    static void registerNativeItems() {
        // potion items build their names from the potion registry while ItemRegistry inits
        org.powernukkitx.registry.Registries.EFFECT.init();
        org.powernukkitx.registry.Registries.POTION.init();
        org.powernukkitx.registry.Registries.BLOCK.init();
        org.powernukkitx.registry.Registries.ITEM.init();
        org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.init();
    }
    @Test
    void preservesStackCountOfKeysInsteadOfProducingZeroCountItem() {
        Item key = Item.get("minecraft:tripwire_hook", 0, 32);
        key.setCustomName("Crate key");
        var hand = InventoryTransactionHandler.authoritativeHand(key);
        assertEquals(32, hand.getCount());
        assertNotEquals(0, key.getRuntimeId());
        assertEquals(key.getId(), hand.getDefinition().getIdentifier());
        assertEquals(key.getRuntimeId(), hand.getDefinition().getRuntimeId());
        assertNotNull(hand.getTag());
        assertEquals(key.getNetId(), hand.getNetId());
    }

    @Test
    void preservesPickaxeWearAndNamedTagForSurvivalValidation() {
        Item pickaxe = Item.get("minecraft:diamond_pickaxe", 0, 1);
        pickaxe.setMeta(17);
        pickaxe.setCustomName("Custom pickaxe");
        var hand = InventoryTransactionHandler.authoritativeHand(pickaxe);
        assertEquals(1, hand.getCount());
        assertEquals(17, hand.getDamage());
        assertNotNull(hand.getTag());
        assertNotEquals(0, pickaxe.getRuntimeId());
        assertEquals(pickaxe.getId(), hand.getDefinition().getIdentifier());
        assertEquals(pickaxe.getRuntimeId(), hand.getDefinition().getRuntimeId());
    }
}
