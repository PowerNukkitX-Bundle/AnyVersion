package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.block.BlockID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.AddItemActorPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class AddItemEntityHandler extends PacketHandler<AddItemActorPacket> {

    @Override
    public void handle(ProtocolPlayer player, AddItemActorPacket packet) {
        ProtocolVersion version = player.getVersion();
        ItemData body = packet.getItem();
        if(!body.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(version, body);
            packet.setItem(downgraded);
        }
        packet.setEntityData(SetEntityDataHandler.copyActorData(packet.getEntityData()));
        SetEntityDataHandler.fixEntityFlags(version, packet.getEntityData());
    }

}
