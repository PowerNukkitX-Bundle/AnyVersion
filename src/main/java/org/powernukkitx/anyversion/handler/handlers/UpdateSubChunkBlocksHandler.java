package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.BlockState;
import org.cloudburstmc.protocol.bedrock.data.BlockChangeEntry;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
import org.cloudburstmc.protocol.bedrock.packet.UpdateSubChunkBlocksPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.util.ArrayList;
import java.util.List;

public class UpdateSubChunkBlocksHandler extends PacketHandler<UpdateSubChunkBlocksPacket> {
    @Override
    public void handle(ProtocolPlayer player, UpdateSubChunkBlocksPacket packet) {
        List<BlockChangeEntry> entries = new ArrayList<>(packet.getStandardBlocks());
        packet.getStandardBlocks().clear();
        for (BlockChangeEntry entry : entries) {
            BlockState current = cn.nukkit.registry.Registries.BLOCKSTATE.get(entry.getDefinition().getRuntimeId());
            BlockState downgraded = Registries.BLOCKSTATE.downgrade(player.getVersion(), current);
            BlockDefinition blockDefinition = new SimpleBlockDefinition(
                    downgraded.getIdentifier(),
                    Registries.BLOCKPALETTE.getRuntimeId(player.getVersion(), downgraded),
                    downgraded.getBlockStateTag()
            );
            packet.getStandardBlocks().add(new BlockChangeEntry(entry.getPos(), blockDefinition, entry.getUpdateFlags(), entry.getSyncMessageEntityUniqueID(), entry.getMessage()));
        }
    }
}
