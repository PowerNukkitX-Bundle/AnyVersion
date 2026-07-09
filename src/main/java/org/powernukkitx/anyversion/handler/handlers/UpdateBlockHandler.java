package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.block.BlockState;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

public class UpdateBlockHandler extends PacketHandler<UpdateBlockPacket> {

    @Override
    public void handle(ProtocolPlayer player, UpdateBlockPacket packet) {
        BlockState current = org.powernukkitx.registry.Registries.BLOCKSTATE.get(packet.getDefinition().getRuntimeId());
        BlockState downgraded = Registries.BLOCKSTATE.downgrade(player.getVersion(), current);
        BlockDefinition blockDefinition = new SimpleBlockDefinition(
                downgraded.getIdentifier(),
                Registries.BLOCKPALETTE.getRuntimeId(player.getVersion(), downgraded),
                downgraded.getBlockStateTag()
        );
        packet.setDefinition(blockDefinition);
    }

}
