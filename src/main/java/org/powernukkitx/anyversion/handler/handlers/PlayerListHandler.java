package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.payload.list.PlayerListAddEntry;
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.utils.SkinConverter;

public class PlayerListHandler extends PacketHandler<PlayerListPacket> {

    @Override
    public void handle(ProtocolPlayer player, PlayerListPacket packet) {
        packet.getEntries().stream()
                .filter(PlayerListAddEntry.class::isInstance)
                .map(PlayerListAddEntry.class::cast)
                .forEach(entry -> {
                    if (entry.getSkin() == null && entry.getSerializedSkin() != null) {
                        entry.setSkin(SkinConverter.fromSerializedSkin(entry.getSerializedSkin()));
                    } else if (entry.getSerializedSkin() == null && entry.getSkin() != null) {
                        entry.setSerializedSkin(SkinConverter.toSerializedSkin(entry.getSkin(), entry.isTrustedSkin()));
                    }
                });
    }
}
