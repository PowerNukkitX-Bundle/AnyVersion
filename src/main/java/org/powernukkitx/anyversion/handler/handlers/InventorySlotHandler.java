package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.block.BlockID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

public class InventorySlotHandler extends PacketHandler<InventorySlotPacket> {

    @Override
    public void handle(ProtocolPlayer player, InventorySlotPacket packet) {
        ItemData data = packet.getItem();
        if(data.getDefinition() == null || data.getDefinition().getIdentifier().equals(BlockID.AIR)) return;
        ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), data);
        packet.setItem(downgraded);
    }

}
