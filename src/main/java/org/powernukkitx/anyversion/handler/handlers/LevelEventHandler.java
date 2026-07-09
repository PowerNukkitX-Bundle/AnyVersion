package org.powernukkitx.anyversion.handler.handlers;

import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.BlockStateImpl;
import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

@Slf4j
public class LevelEventHandler extends PacketHandler<LevelEventPacket> {

    @Override
    public void handle(ProtocolPlayer player, LevelEventPacket packet) {
        if(packet.getType() == LevelEvent.PARTICLE_DESTROY_BLOCK) {
            BlockState origen = org.powernukkitx.registry.Registries.BLOCKSTATE.get(packet.getData());
            BlockStateImpl downgraded = (BlockStateImpl) Registries.BLOCKSTATE.downgrade(player.getVersion(), origen);
            packet.setData(Registries.BLOCKPALETTE.getRuntimeId(player.getVersion(), downgraded));
        }
    }

}
