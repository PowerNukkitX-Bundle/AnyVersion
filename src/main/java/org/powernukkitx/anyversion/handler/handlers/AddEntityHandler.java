package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.packet.AddActorPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class AddEntityHandler extends PacketHandler<AddActorPacket> {

    @Override
    public void handle(ProtocolPlayer player, AddActorPacket packet) {
        ProtocolVersion version = player.getVersion();
        packet.setActorData(SetEntityDataHandler.copyActorData(packet.getActorData()));
        Registries.ENTITY.transform(version, packet);
        SetEntityDataHandler.fixEntityFlags(version, packet.getActorData());
    }

}
