package org.powernukkitx.anyversion.manager;

import cn.nukkit.Player;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public record ProtocolPlayer(BedrockSession player, int protocol, Player nukkitPlayer) {

    public ProtocolPlayer(BedrockSession player, int protocol) {
        this(player, protocol, null);
    }

    public ProtocolVersion getVersion() {
        return ProtocolVersion.get(protocol);
    }

    public ProtocolPlayer withPlayer(Player player) {
        return new ProtocolPlayer(player.getSession(), protocol, player);
    }
}
