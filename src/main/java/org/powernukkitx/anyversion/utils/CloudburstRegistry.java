package org.powernukkitx.anyversion.utils;

import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.registry.ItemRegistry;
import org.powernukkitx.registry.ItemRuntimeIdRegistry;
import org.powernukkitx.registry.Registries;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.*;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemVersion;
import org.cloudburstmc.protocol.common.DefinitionRegistry;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.SimpleDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;

public class CloudburstRegistry {

    //We use the same palette for every version of the game and translate later on.

    private static CloudburstRegistry INSTANCE;

    public static CloudburstRegistry get() {
        if(INSTANCE == null) {
            INSTANCE = new CloudburstRegistry();
        }
        return INSTANCE;
    }

    private DefinitionRegistry<ItemDefinition> itemDefinitionRegistry;
    private DefinitionRegistry<BlockDefinition> blockDefinitionRegistry;
    private DefinitionRegistry<NamedDefinition> namedDefinitionRegistry;

    private CloudburstRegistry() {
        reload();
    }

    protected void reload() {
        List<ItemDefinition> itemDefinitions = new ArrayList<>();
        for(ItemRuntimeIdRegistry.ItemData data : ItemRuntimeIdRegistry.getITEMDATA()) {
            CompoundTag tag = new CompoundTag();

            NbtMap itemComponents = ItemRegistry.getItemComponents();
            if (itemComponents.containsKey(data.identifier())) {
                NbtMap itemTag = itemComponents.getCompound(data.identifier());
                NbtMap components = itemTag.getCompound("components");
                SimpleItemDefinition definition = new SimpleItemDefinition(data.identifier(), data.runtimeId(), ItemVersion.from(data.version()), data.componentBased(), NbtMap.builder().putCompound("components", components).build());
                itemDefinitions.add(definition);
                continue;
            }
            if (Registries.ITEM.getCustomItemDefinition().containsKey(data.identifier())) {
                tag = Registries.ITEM.getCustomItemDefinition().get(data.identifier()).nbt();
            }
            SimpleItemDefinition definition = new SimpleItemDefinition(data.identifier(), data.runtimeId(), ItemVersion.from(data.version()), data.componentBased(), NbtMap.fromMap(tag.parseValue()));
            itemDefinitions.add(definition);
        }
        List<BlockDefinition> blockDefinitions = new ArrayList<>();
        for (var blockState : Registries.BLOCKSTATE.getAllState()) {
            NbtMap map = blockState.getBlockStateTag();
            SimpleBlockDefinition definition = new SimpleBlockDefinition(blockState.getIdentifier(), blockState.blockStateHash(), map);
            blockDefinitions.add(definition);
        }
        List<NamedDefinition> namedDefinitions = new ArrayList<>();
        itemDefinitionRegistry = SimpleDefinitionRegistry.<ItemDefinition>builder().addAll(itemDefinitions).build();
        blockDefinitionRegistry = SimpleDefinitionRegistry.<BlockDefinition>builder().addAll(blockDefinitions).build();
        namedDefinitionRegistry = SimpleDefinitionRegistry.<NamedDefinition>builder().addAll(namedDefinitions).build();
    }

    DefinitionRegistry<ItemDefinition> getItemDefinitionRegistry() {
        return itemDefinitionRegistry;
    }

    DefinitionRegistry<BlockDefinition> getBlockDefinitionRegistry() {
        return blockDefinitionRegistry;
    }

    DefinitionRegistry<NamedDefinition> getNamedDefinitionRegistry() {
        return namedDefinitionRegistry;
    }
}
