package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;
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

import static cn.nukkit.network.protocol.CraftingDataPacket.*;

public class CraftingDataHandler extends PacketHandler<CraftingDataPacket> {

    @Override
    public void handle(ProtocolPlayer player, CraftingDataPacket packet) {
        List<RecipeData> craftingData = new ArrayList<>(packet.getCraftingData());
        packet.getCraftingData().clear();
        recipe:
        for(RecipeData uncast : craftingData) {
            RecipeData result = uncast;
            if(uncast instanceof CraftingRecipeData data) {
                ObjectArrayList<ItemDescriptorWithCount> ingredients = new ObjectArrayList<>();
                ObjectArrayList<ItemData> results = new ObjectArrayList<>();
                for(ItemDescriptorWithCount descriptor : data.getIngredients()) {
                    ItemDescriptorWithCount itemDescriptor = translateItemDescriptorWithCount(player.getVersion(), descriptor);
                    if(itemDescriptor == null) continue recipe;
                    ingredients.add(itemDescriptor);
                }
                for(ItemData itemData : data.getResults()) {
                    ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                    if(downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                    results.add(downgrade);
                }
                if(data instanceof ShapedRecipeData shapedRecipeData) {
                    result = ShapedRecipeData.of(shapedRecipeData.getType(), shapedRecipeData.getId(), shapedRecipeData.getWidth(), shapedRecipeData.getHeight(), ingredients, results, shapedRecipeData.getUuid(), shapedRecipeData.getTag(), shapedRecipeData.getPriority(), shapedRecipeData.getNetId(), shapedRecipeData.isAssumeSymetry(), shapedRecipeData.getRequirement());
                } else if(data instanceof ShapelessRecipeData shapelessRecipeData) {
                    switch (shapelessRecipeData.getTag()) {
                        case CRAFTING_TAG_FURNACE,
                             CRAFTING_TAG_SMOKER,
                             CRAFTING_TAG_BLAST_FURNACE,
                             CRAFTING_TAG_CAMPFIRE,
                             CRAFTING_TAG_SOUL_CAMPFIRE -> {
                            if(player.getVersion().protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()) {
                                int inputId = cn.nukkit.registry.Registries.ITEM_RUNTIMEID.getInt(ingredients.getFirst().toItem().getDefinition().getIdentifier());
                                ItemData resultItem = data.getResults().getFirst();
                                Item item = Item.get(cn.nukkit.registry.Registries.ITEM_RUNTIMEID.getIdentifier(inputId));
                                if(item != null) {
                                    ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
                                    ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                                    if(downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                                    inputId = downgrade.getDefinition().getRuntimeId();
                                } else continue recipe; //This never happens. Was added just in case!
                                resultItem = Registries.ITEM.downgrade(player.getVersion(), resultItem);
                                if(resultItem.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(resultItem).getDefinition().getIdentifier())) continue recipe;
                                result = FurnaceRecipeData.of(CraftingDataType.FURNACE_DATA, inputId, 0, resultItem, data.getTag());
                            } else {
                                result = ShapelessRecipeData.of(shapelessRecipeData.getType(), shapelessRecipeData.getId(), ingredients, results, shapelessRecipeData.getUuid(), shapelessRecipeData.getTag(), shapelessRecipeData.getPriority(), shapelessRecipeData.getNetId(), shapelessRecipeData.getRequirement());
                            }
                        }
                        default -> {
                            result = ShapelessRecipeData.of(shapelessRecipeData.getType(), shapelessRecipeData.getId(), ingredients, results, shapelessRecipeData.getUuid(), shapelessRecipeData.getTag(), shapelessRecipeData.getPriority(), shapelessRecipeData.getNetId(), shapelessRecipeData.getRequirement());
                        }
                    }
                }
            } else if(uncast instanceof FurnaceRecipeData data) {
                int inputId = data.getInputId();
                int damage = data.getInputData();
                ItemData resultItem = data.getResult();
                Item item = Item.get(cn.nukkit.registry.Registries.ITEM_RUNTIMEID.getIdentifier(inputId));
                if(item != null) {
                    ItemData itemData = ItemData.builder().definition(new SimpleItemDefinition(item.getId(), item.getRuntimeId(), false)).build();
                    ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), itemData);
                    if(downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                    inputId = downgrade.getDefinition().getRuntimeId();
                    damage = downgrade.getDamage();
                } else continue recipe; //This never happens. Was added just in case!
                resultItem = Registries.ITEM.downgrade(player.getVersion(), resultItem);
                if(resultItem.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(resultItem).getDefinition().getIdentifier())) continue recipe;
                result = FurnaceRecipeData.of(data.getType(), inputId, damage, resultItem, data.getTag());
            } else if(uncast instanceof SmithingTrimRecipeData data) {
                ItemDescriptorWithCount base = translateItemDescriptorWithCount(player.getVersion(), data.getBase());
                ItemDescriptorWithCount addition = translateItemDescriptorWithCount(player.getVersion(), data.getAddition());
                ItemDescriptorWithCount template = translateItemDescriptorWithCount(player.getVersion(), data.getTemplate());
                if(base == null || addition == null || template == null) continue recipe;
                result = SmithingTrimRecipeData.of(data.getId(), base, addition, template, data.getTag(), data.getNetId());
            } else if(uncast instanceof SmithingTransformRecipeData data) {
                ItemDescriptorWithCount base = translateItemDescriptorWithCount(player.getVersion(), data.getBase());
                ItemDescriptorWithCount addition = translateItemDescriptorWithCount(player.getVersion(), data.getAddition());
                ItemDescriptorWithCount template = translateItemDescriptorWithCount(player.getVersion(), data.getTemplate());
                ItemData downgrade = Registries.ITEM.downgrade(player.getVersion(), data.getResult());
                if(base == null || addition == null || template == null || downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) continue recipe;
                result = SmithingTransformRecipeData.of(data.getId(), base, addition, template, downgrade, data.getTag(), data.getNetId());
            }
            packet.getCraftingData().add(result);
        }
    }

    protected ItemDescriptorWithCount translateItemDescriptorWithCount(ProtocolVersion version, ItemDescriptorWithCount descriptor) {
        ItemDescriptor itemDescriptor = descriptor.getDescriptor();
        if(itemDescriptor instanceof DefaultDescriptor defaultDescriptor) {
            ItemData itemData = itemDescriptor.toItem().build();
            ItemData downgrade = Registries.ITEM.downgrade(version, itemData);
            if(downgrade.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgrade).getDefinition().getIdentifier())) return null;
            itemDescriptor = new DefaultDescriptor(downgrade.getDefinition(), defaultDescriptor.getAuxValue());
        }
        return new ItemDescriptorWithCount(itemDescriptor, descriptor.getCount());
    }

}
