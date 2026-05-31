package org.powernukkitx.anyversion.packets;

import org.cloudburstmc.protocol.bedrock.packet.EmotePacket;

public class PEmotePacket extends EmotePacket implements ProtocolizedPacket {
    private int protocol;

    @Override
    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
}
