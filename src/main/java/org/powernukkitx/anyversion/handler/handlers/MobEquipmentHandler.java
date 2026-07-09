package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.block.BlockID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

public class MobEquipmentHandler extends PacketHandler<MobEquipmentPacket> {

    @Override
    public void handle(ProtocolPlayer player, MobEquipmentPacket packet) {
        ItemData body = packet.getItem();
        if(body.getDefinition() == null) return; //Packet inbound
        if(!body.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), body);
            packet.setItem(downgraded);
        }
    }

}
