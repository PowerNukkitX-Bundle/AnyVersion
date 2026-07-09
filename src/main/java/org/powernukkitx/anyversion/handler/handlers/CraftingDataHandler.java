package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataEntryType;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.*;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.DefaultDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptorWithCount;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.util.ArrayList;
import java.util.List;

public class CraftingDataHandler extends PacketHandler<CraftingDataPacket> {

    private static final List<String> FURNACE_TAGS = List.of("furnace", "smoker", "blast_furnace", "campfire", "soul_campfire");

    @Override
    public void handle(ProtocolPlayer player, CraftingDataPacket packet) {
        List<CraftingDataEntry> craftingData = new ArrayList<>(packet.getCraftingEntries());
        packet.getCraftingEntries().clear();
        recipe:
        for (CraftingDataEntry uncast : craftingData) {
            CraftingDataEntry result = uncast;
            if (uncast instanceof GridCraftingDataEntry data) {
                ObjectArrayList<ItemDescriptorWithCount> ingredients = new ObjectArrayList<>();
                ObjectArrayList<ItemData> results = new ObjectArrayList<>();
                for (ItemDescriptorWithCount descriptor : data.getIngredientList()) {
                    ItemDescriptorWithCount itemDescriptor = translateItemDescriptorWithCount(player.getVersion(), descriptor);
                    if (itemDescriptor == null) continue recipe;
                    ingredients.add(itemDescriptor);
                }
                for (ItemData itemData : data.getProductionList()) {
                    ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                    if (downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                    results.add(downgrade);
                }
                if (data instanceof ShapedRecipe shaped) {
                    result = ShapedRecipe.of(shaped.getType(), shaped.getRecipeUniqueId(), shaped.getRecipeWidth(), shaped.getRecipeHeight(), ingredients, results, shaped.getRecipeID(), shaped.getRecipeTag(), shaped.getPriority(), shaped.getNetId(), shaped.isAssumeSymmetry(), shaped.getUnlockingRequirement());
                } else if (data instanceof ShapelessRecipe shapeless) {
                    if (FURNACE_TAGS.contains(shapeless.getRecipeTag()) && player.getVersion().protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()) {
                        int inputId = org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getInt(ingredients.getFirst().toItem().getDefinition().getIdentifier());
                        ItemData resultItem = data.getProductionList().getFirst();
                        Item item = Item.get(org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getIdentifier(inputId));
                        if (item == null) continue recipe;
                        ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
                        ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                        if (downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                        inputId = downgrade.getDefinition().getRuntimeId();
                        resultItem = Registries.ITEM.downgrade(player.getVersion(), resultItem);
                        if (resultItem.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(resultItem).getDefinition().getIdentifier())) continue recipe;
                        result = FurnaceRecipe.of(CraftingDataEntryType.FURNACE_RECIPE, inputId, 0, resultItem, shapeless.getRecipeTag());
                    } else {
                        result = ShapelessRecipe.of(shapeless.getType(), shapeless.getRecipeUniqueId(), ingredients, results, shapeless.getRecipeID(), shapeless.getRecipeTag(), shapeless.getPriority(), shapeless.getNetId(), shapeless.getUnlockingRequirement());
                    }
                }
            } else if (uncast instanceof FurnaceRecipe data) {
                int inputId = data.getInputId();
                int damage = data.getInputData();
                ItemData resultItem = data.getResultItem();
                Item item = Item.get(org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getIdentifier(inputId));
                if (item == null) continue recipe;
                ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
                ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                if (downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                inputId = downgrade.getDefinition().getRuntimeId();
                damage = downgrade.getDamage();
                resultItem = Registries.ITEM.downgrade(player.getVersion(), resultItem);
                if (resultItem.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(resultItem).getDefinition().getIdentifier())) continue recipe;
                result = FurnaceRecipe.of(data.getType(), inputId, damage, resultItem, data.getRecipeTag());
            } else if (uncast instanceof SmithingTrimRecipe data) {
                ItemDescriptorWithCount base = translateItemDescriptorWithCount(player.getVersion(), data.getBaseIngredient());
                ItemDescriptorWithCount addition = translateItemDescriptorWithCount(player.getVersion(), data.getAdditionIngredient());
                ItemDescriptorWithCount template = translateItemDescriptorWithCount(player.getVersion(), data.getTemplateIngredient());
                if (base == null || addition == null || template == null) continue recipe;
                result = SmithingTrimRecipe.of(data.getRecipeUniqueId(), base, addition, template, data.getRecipeTag(), data.getNetId());
            } else if (uncast instanceof SmithingTransformRecipe data) {
                ItemDescriptorWithCount base = translateItemDescriptorWithCount(player.getVersion(), data.getBaseIngredient());
                ItemDescriptorWithCount addition = translateItemDescriptorWithCount(player.getVersion(), data.getAdditionIngredient());
                ItemDescriptorWithCount template = translateItemDescriptorWithCount(player.getVersion(), data.getTemplateIngredient());
                ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), data.getResult());
                if (base == null || addition == null || template == null || downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                result = SmithingTransformRecipe.of(data.getRecipeUniqueId(), base, addition, template, downgrade, data.getRecipeTag(), data.getNetId());
            }
            packet.getCraftingEntries().add(result);
        }
    }

    protected ItemDescriptorWithCount translateItemDescriptorWithCount(ProtocolVersion version, ItemDescriptorWithCount descriptor) {
        ItemDescriptor itemDescriptor = descriptor.getDescriptor();
        if (itemDescriptor instanceof DefaultDescriptor defaultDescriptor) {
            ItemData itemData = itemDescriptor.toItem().build();
            ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
            if (downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) return null;
            itemDescriptor = new DefaultDescriptor(downgrade.getDefinition(), defaultDescriptor.getAuxValue());
        }
        return new ItemDescriptorWithCount(itemDescriptor, descriptor.getCount());
    }
}
