package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.powernukkitx.Player;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.inventory.Inventory;

public class ContainerCloseHandler extends PacketHandler<ContainerClosePacket> {

    @Override
    public void handle(ProtocolPlayer player, ContainerClosePacket packet) {
        if (packet.getContainerType() != null) {
            return;
        }
        // clients before 1.21.0 don't send the container type, but the newer codec can't write a null one
        Player p = player.nukkitPlayer();
        Inventory window = p == null ? null : p.getWindowById(packet.getContainerID());
        packet.setContainerType(window != null ? window.getType() : ContainerType.NONE);
    }
}
