package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

public class ContainerCloseHandler extends PacketHandler<ContainerClosePacket> {

    @Override
    public void handle(ProtocolPlayer player, ContainerClosePacket packet) {
    }
}
