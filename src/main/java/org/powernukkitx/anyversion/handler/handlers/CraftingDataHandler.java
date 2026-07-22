package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.ItemDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.NameDescriptor;
import org.cloudburstmc.protocol.bedrock.data.inventory.descriptor.RecipeIngredient;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.FurnaceRecipePayload;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.ShapedRecipePayload;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.ShapelessRecipePayload;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.SmithingTransformRecipePayload;
import org.cloudburstmc.protocol.bedrock.data.payload.crafting.SmithingTrimRecipePayload;
import org.cloudburstmc.protocol.bedrock.packet.CraftingDataPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.util.Iterator;
import java.util.List;

public class CraftingDataHandler extends PacketHandler<CraftingDataPacket> {

    private static final List<String> FURNACE_TAGS = List.of("furnace", "smoker", "blast_furnace", "campfire", "soul_campfire");

    @Override
    public void handle(ProtocolPlayer player, CraftingDataPacket packet) {
        ProtocolVersion version = player.getVersion();

        // Furnace recipes are downgraded first so that the ones synthesized from
        // shapeless recipes below are not processed a second time.
        packet.getFurnaceRecipes().removeIf(recipe -> !translateFurnaceRecipe(version, recipe));

        processShapedRecipes(version, packet.getShapedRecipes());
        processShapedRecipes(version, packet.getShapedChemistryRecipes());

        processShapelessRecipes(player, packet, packet.getShapelessRecipes());
        processShapelessRecipes(player, packet, packet.getShapelessChemistryRecipes());
        processShapelessRecipes(player, packet, packet.getUserDataShapelessRecipes());

        packet.getSmithingTrimRecipes().removeIf(recipe -> !translateSmithingTrimRecipe(version, recipe));
        packet.getSmithingTransformRecipes().removeIf(recipe -> !translateSmithingTransformRecipe(version, recipe));
    }

    private void processShapedRecipes(ProtocolVersion version, List<ShapedRecipePayload> recipes) {
        recipes.removeIf(recipe -> !translateGrid(version, recipe.getIngredients(), recipe.getResults()));
    }

    private void processShapelessRecipes(ProtocolPlayer player, CraftingDataPacket packet, List<ShapelessRecipePayload> recipes) {
        ProtocolVersion version = player.getVersion();
        Iterator<ShapelessRecipePayload> iterator = recipes.iterator();
        while (iterator.hasNext()) {
            ShapelessRecipePayload recipe = iterator.next();

            List<RecipeIngredient> ingredients = translateIngredients(version, recipe.getIngredients());
            if (ingredients == null) {
                iterator.remove();
                continue;
            }
            List<ItemData> results = downgradeResults(version, recipe.getResults());
            if (results == null) {
                iterator.remove();
                continue;
            }

            if (FURNACE_TAGS.contains(recipe.getTag()) && version.protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()) {
                // Older protocols expect furnace recipes to be sent as furnace entries rather than shapeless ones.
                FurnaceRecipePayload furnace = toFurnaceRecipe(version, ingredients.getFirst(), recipe.getResults().getFirst(), recipe.getTag());
                iterator.remove();
                if (furnace != null) {
                    packet.getFurnaceRecipes().add(furnace);
                }
            } else {
                replace(recipe.getIngredients(), ingredients);
                replace(recipe.getResults(), results);
            }
        }
    }

    private FurnaceRecipePayload toFurnaceRecipe(ProtocolVersion version, RecipeIngredient input, ItemData result, String tag) {
        int inputId = org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getInt(input.toItem().getDefinition().getIdentifier());
        Item item = Item.get(org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getIdentifier(inputId));
        if (item == null) return null;
        ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
        ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
        if (isDropped(downgrade)) return null;
        ItemData resultItem = Registries.ITEM.downgrade(version, result);
        if (isDropped(resultItem)) return null;

        FurnaceRecipePayload payload = new FurnaceRecipePayload();
        payload.setInputId(downgrade.getDefinition().getRuntimeId());
        payload.setAuxValue(-1);
        payload.setResult(resultItem);
        payload.setTag(tag);
        return payload;
    }

    private boolean translateFurnaceRecipe(ProtocolVersion version, FurnaceRecipePayload recipe) {
        Item item = Item.get(org.powernukkitx.registry.Registries.ITEM_RUNTIMEID.getIdentifier(recipe.getInputId()));
        if (item == null) return false;
        ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
        ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
        if (isDropped(downgrade)) return false;
        ItemData resultItem = Registries.ITEM.downgrade(version, recipe.getResult());
        if (isDropped(resultItem)) return false;

        recipe.setInputId(downgrade.getDefinition().getRuntimeId());
        // A non-aux furnace recipe keeps its auxValue at -1; only aux recipes carry a damage value.
        if (recipe.getAuxValue() != -1) {
            recipe.setAuxValue(downgrade.getDamage());
        }
        recipe.setResult(resultItem);
        return true;
    }

    private boolean translateSmithingTrimRecipe(ProtocolVersion version, SmithingTrimRecipePayload recipe) {
        RecipeIngredient base = translateIngredient(version, recipe.getBaseIngredient());
        RecipeIngredient addition = translateIngredient(version, recipe.getAdditionIngredient());
        RecipeIngredient template = translateIngredient(version, recipe.getTemplateIngredient());
        if (base == null || addition == null || template == null) return false;
        recipe.setBaseIngredient(base);
        recipe.setAdditionIngredient(addition);
        recipe.setTemplateIngredient(template);
        return true;
    }

    private boolean translateSmithingTransformRecipe(ProtocolVersion version, SmithingTransformRecipePayload recipe) {
        RecipeIngredient base = translateIngredient(version, recipe.getBaseIngredient());
        RecipeIngredient addition = translateIngredient(version, recipe.getAdditionIngredient());
        RecipeIngredient template = translateIngredient(version, recipe.getTemplateIngredient());
        ItemData result = Registries.ITEM.downgrade(version, recipe.getResult());
        if (base == null || addition == null || template == null || isDropped(result)) return false;
        recipe.setBaseIngredient(base);
        recipe.setAdditionIngredient(addition);
        recipe.setTemplateIngredient(template);
        recipe.setResult(result);
        return true;
    }

    private boolean translateGrid(ProtocolVersion version, List<RecipeIngredient> ingredients, List<ItemData> results) {
        List<RecipeIngredient> translatedIngredients = translateIngredients(version, ingredients);
        if (translatedIngredients == null) return false;
        List<ItemData> downgradedResults = downgradeResults(version, results);
        if (downgradedResults == null) return false;
        replace(ingredients, translatedIngredients);
        replace(results, downgradedResults);
        return true;
    }

    private List<RecipeIngredient> translateIngredients(ProtocolVersion version, List<RecipeIngredient> ingredients) {
        ObjectArrayList<RecipeIngredient> translated = new ObjectArrayList<>();
        for (RecipeIngredient ingredient : ingredients) {
            RecipeIngredient result = translateIngredient(version, ingredient);
            if (result == null) return null;
            translated.add(result);
        }
        return translated;
    }

    private List<ItemData> downgradeResults(ProtocolVersion version, List<ItemData> results) {
        ObjectArrayList<ItemData> downgraded = new ObjectArrayList<>();
        for (ItemData itemData : results) {
            ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
            if (isDropped(downgrade)) return null;
            downgraded.add(downgrade);
        }
        return downgraded;
    }

    protected RecipeIngredient translateIngredient(ProtocolVersion version, RecipeIngredient ingredient) {
        ItemDescriptor descriptor = ingredient.getDescriptor();
        if (descriptor instanceof NameDescriptor nameDescriptor) {
            ItemData itemData = descriptor.toItem().build();
            ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
            if (isDropped(downgrade)) return null;
            descriptor = new NameDescriptor(downgrade.getDefinition(), nameDescriptor.getAuxValue());
        }
        return new RecipeIngredient(descriptor, ingredient.getStackSize());
    }

    private boolean isDropped(ItemData downgrade) {
        return downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier());
    }

    private <T> void replace(List<T> target, List<T> replacement) {
        target.clear();
        target.addAll(replacement);
    }
}
