package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.utils.SkinConverter;

public class PlayerSkinHandler extends PacketHandler<PlayerSkinPacket> {

    @Override
    public void handle(ProtocolPlayer player, PlayerSkinPacket packet) {
        if (packet.getSkin() == null && packet.getSerializedSkin() != null) {
            packet.setSkin(SkinConverter.fromSerializedSkin(packet.getSerializedSkin()));
        } else if (packet.getSerializedSkin() == null && packet.getSkin() != null) {
            packet.setSerializedSkin(SkinConverter.toSerializedSkin(packet.getSkin(), packet.isTrustedSkin()));
        }
    }
}
