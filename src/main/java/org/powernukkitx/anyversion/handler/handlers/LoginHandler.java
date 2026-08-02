package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.auth.PlayerAuthenticationType;
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

public class LoginHandler extends PacketHandler<LoginPacket> {

    @Override
    public void handle(ProtocolPlayer player, LoginPacket packet) {
        if (packet.getAuthenticationType() == null) {
            packet.setAuthenticationType(PlayerAuthenticationType.SELF_SIGNED);
        }
    }
}
