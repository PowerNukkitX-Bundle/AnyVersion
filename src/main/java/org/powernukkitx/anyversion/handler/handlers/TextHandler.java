package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.Server;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class TextHandler extends PacketHandler<TextPacket> {

    @Override
    public void handle(ProtocolPlayer player, TextPacket packet) {
        ProtocolVersion version = player.getVersion();
        if(version.protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()) {
            if(packet.getType() == TextPacket.Type.TRANSLATION) {
                packet.setNeedsTranslation(false);
                packet.setType(TextPacket.Type.RAW);
                packet.setMessage(Server.getInstance().getLanguage().tr(packet.getMessage(), packet.getParameters().toArray()));
            }
        }
    }

}
