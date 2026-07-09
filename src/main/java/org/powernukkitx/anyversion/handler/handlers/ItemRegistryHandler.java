package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.nbt.tag.*;
import org.powernukkitx.registry.ItemRuntimeIdRegistry;
import org.powernukkitx.registry.Registries;
import org.cloudburstmc.nbt.*;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemVersion;
import org.cloudburstmc.protocol.bedrock.packet.ItemRegistryPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistryHandler extends PacketHandler<ItemRegistryPacket> {

    @Override
    public void handle(ProtocolPlayer player, ItemRegistryPacket packet) {
        if (player.protocol() < ProtocolVersion.MINECRAFT_PE_1_21_60.protocol()) {
            List<ItemDefinition> definitions = new ArrayList<>();
            for (ItemRuntimeIdRegistry.ItemData data : ItemRuntimeIdRegistry.getITEMDATA()) {
                if (Registries.ITEM.getCustomItemDefinition().containsKey(data.identifier())) {
                    CompoundTag tag = Registries.ITEM.getCustomItemDefinition().get(data.identifier()).nbt().copy();
                    if (player.getVersion().protocol() < ProtocolVersion.MINECRAFT_PE_1_20_60.protocol()) {
                        CompoundTag icon = tag.getCompound("components")
                                .getCompound("item_properties")
                                .getCompound("minecraft:icon");
                        icon.putString("texture", icon.getCompound("textures").getString("default"));
                        icon.remove("textures");
                    }
                    definitions.add(new SimpleItemDefinition(data.identifier(), data.runtimeId(), ItemVersion.from(data.version()), data.componentBased(), fromCompound(tag)));
                }
            }
            packet.getItemData().clear();
            packet.getItemData().addAll(definitions);
        }
    }

    public NbtMap fromCompound(CompoundTag tag) {
        NbtMapBuilder builder = NbtMap.builder();
        for (var entry : tag.getTags().entrySet()) {
            String key = entry.getKey();
            Tag value = entry.getValue();
            if (value instanceof CompoundTag v) {
                builder.put(key, fromCompound(v));
            } else if (value instanceof ListTag<?> v) {
                builder.putList(key, (NbtType) NbtType.byId(v.type), (java.util.List) v.parseValue());
            } else if (value instanceof ByteTag v) {
                builder.putByte(key, (byte) (v.data & 0xFF));
            } else if (value instanceof ShortTag v) {
                builder.putShort(key, v.data);
            } else {
                builder.put(key, value.parseValue());
            }
        }
        return builder.build();
    }
}
