package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.CurrentCmdVersion;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

public class CommandRequestHandler extends PacketHandler<CommandRequestPacket> {

    @Override
    public void handle(ProtocolPlayer player, CommandRequestPacket packet) {
        CurrentCmdVersion[] versions = CurrentCmdVersion.values();

        if (packet.getVersion() == null) {
            int legacyVersion = packet.getLegacyVersion();
            packet.setVersion(legacyVersion >= 0 && legacyVersion < versions.length
                    ? versions[legacyVersion]
                    : CurrentCmdVersion.LATEST);
        } else {
            packet.setLegacyVersion(packet.getVersion().ordinal());
        }
    }
}
