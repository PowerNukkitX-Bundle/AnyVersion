package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.Server;
import org.cloudburstmc.protocol.bedrock.data.TextPacketType;
import org.cloudburstmc.protocol.bedrock.data.payload.text.MessageAndParams;
import org.cloudburstmc.protocol.bedrock.data.payload.text.MessageOnly;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class TextHandler extends PacketHandler<TextPacket> {

    @Override
    public void handle(ProtocolPlayer player, TextPacket packet) {
        if (player.getVersion().protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()
                && packet.getMessageType() == TextPacketType.TRANSLATE
                && packet.getBody() instanceof MessageAndParams body) {
            MessageOnly translated = new MessageOnly();
            translated.setMessage(Server.getInstance().getLanguage().tr(body.getMessage(), body.getParameterList().toArray()));
            packet.setLocalize(false);
            packet.setMessageType(TextPacketType.RAW);
            packet.setBody(translated);
        }
    }
}
