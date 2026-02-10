package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.customblock.CustomBlockDefinition;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.registry.ItemRegistry;
import cn.nukkit.registry.ItemRuntimeIdRegistry;
import cn.nukkit.registry.Registries;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.data.BlockPropertyData;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemVersion;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StartGameHandler extends PacketHandler<StartGamePacket> {


    @Override
    public void handle(ProtocolPlayer player, StartGamePacket packet) {
        List<ItemDefinition> definitions = new ArrayList<>();

        if(player.protocol() < ProtocolVersion.MINECRAFT_PE_1_26_0.protocol()) {
            packet.setServerId("");
            packet.setWorldId("");
            packet.setScenarioId("");
            packet.setOwnerId("");
        }

        if(player.protocol() < ProtocolVersion.MINECRAFT_PE_1_21_130.protocol()) {
            packet.getBlockProperties().clear();
            for(CustomBlockDefinition definition : Registries.BLOCK.getCustomBlockDefinitionList()) {
                CompoundTag nbt = definition.nbt().copy();
                if(nbt.containsList("permutations")) {
                    ListTag<CompoundTag> permutations = nbt.getList("permutations", CompoundTag.class);
                    for(CompoundTag permutation : permutations.getAll()) {
                        CompoundTag componentsP = permutation.getCompound("components");
                        CompoundTag collision = componentsP.getCompound("minecraft:collision_box");
                        collision.putBoolean("enabled", true);
                        Vector3 origin = Vector3.ZERO.clone();
                        Vector3 size = Vector3.ZERO.clone();
                        ListTag<CompoundTag> boxes = collision.removeAndGet("boxes");
                        for(CompoundTag box : boxes.getAll()) {
                            origin.setX(NukkitMath.min(origin.getX(), box.getFloat("minX") - 8));
                            origin.setY(NukkitMath.min(origin.getY(), box.getFloat("minY")));
                            origin.setZ(NukkitMath.min(origin.getZ(), box.getFloat("minZ") - 8));
                            size.setX(NukkitMath.max(size.getX(), box.getFloat("maxX") - origin.getX()));
                            size.setY(NukkitMath.max(size.getY(), box.getFloat("maxY") - origin.getY()));
                            size.setZ(NukkitMath.max(size.getZ(), box.getFloat("maxZ") - origin.getZ()));
                        }
                        collision.putList("origin", new ListTag<FloatTag>()
                                        .add(new FloatTag(origin.getX()))
                                        .add(new FloatTag(origin.getY()))
                                        .add(new FloatTag(origin.getZ())))
                                .putList("size", new ListTag<FloatTag>()
                                        .add(new FloatTag(size.getX()))
                                        .add(new FloatTag(size.getY()))
                                        .add(new FloatTag(size.getZ())));
                    }
                }
                packet.getBlockProperties().add(new BlockPropertyData(definition.identifier(), InventoryTransactionHandler.deepcopy(nbt)));
            }
        }

        if(player.protocol() < ProtocolVersion.MINECRAFT_PE_1_21_90.protocol()) {
            packet.setAuthoritativeMovementMode(AuthoritativeMovementMode.SERVER);
        }

        if(player.protocol() < ProtocolVersion.MINECRAFT_PE_1_21_60.protocol()) {
            HashSet<String> modifiedIdentifiers = new HashSet<>();
            for(ItemRuntimeIdRegistry.ItemData data : ItemRuntimeIdRegistry.getITEMDATA()) {
                CompoundTag tag = new CompoundTag();

                if (ItemRegistry.getItemComponents().containsCompound(data.identifier())) {
                    CompoundTag item_tag = ItemRegistry.getItemComponents().getCompound(data.identifier());
                    tag.putCompound("components", item_tag.getCompound("components"));
                }
                else if (Registries.ITEM.getCustomItemDefinition().containsKey(data.identifier())) {
                    tag = Registries.ITEM.getCustomItemDefinition().get(data.identifier()).nbt();
                }
                SimpleItemDefinition definition = new SimpleItemDefinition(data.identifier(), data.runtimeId(), ItemVersion.from(data.version()), data.componentBased(), NbtMap.fromMap(tag.parseValue()));
                ItemData cbItemdata = ItemData.builder().definition(definition).build();
                SimpleItemDefinition downgraded = (SimpleItemDefinition) org.powernukkitx.anyversion.registries.Registries.ITEM.downgrade(player.getVersion(), cbItemdata).getDefinition();
                String downgradedIdentifier = downgraded.getIdentifier();
                if(downgradedIdentifier.equals(org.powernukkitx.anyversion.registries.Registries.ITEM.getOutdated(cbItemdata).getDefinition().getIdentifier())) continue;
                if(downgradedIdentifier.equals(cbItemdata.getDefinition().getIdentifier())) {
                    if(modifiedIdentifiers.contains(downgradedIdentifier)) {
                        if(cbItemdata.getDefinition().getRuntimeId() == Registries.ITEM_RUNTIMEID.getInt(downgradedIdentifier)) {
                            continue;
                        } else {
                            throw new RuntimeException("Expected a changing runtimeId for " + downgradedIdentifier);
                        }
                    }
                } else modifiedIdentifiers.add(downgradedIdentifier);
                definitions.add(downgraded);
            }
        }
        packet.setItemDefinitions(definitions);
    }
}
