package org.powernukkitx.anyversion.processors;

import cn.nukkit.Server;
import cn.nukkit.network.process.PacketHandler;
import cn.nukkit.network.process.PlayerSessionHolder;
import cn.nukkit.network.process.handler.EmoteHandler;
import org.cloudburstmc.protocol.bedrock.packet.EmotePacket;

public class PEmoteProcessor implements PacketHandler<EmotePacket> {
    private final EmoteHandler delegate = new EmoteHandler();

    @Override
    public void handle(EmotePacket packet, PlayerSessionHolder holder, Server server) {
        delegate.handle(packet, holder, server);
    }
}
