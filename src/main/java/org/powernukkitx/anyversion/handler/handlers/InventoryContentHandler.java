package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.BlockID;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.util.List;

public class InventoryContentHandler extends PacketHandler<InventoryContentPacket> {

    @Override
    public void handle(ProtocolPlayer player, InventoryContentPacket packet) {
        List<ItemData> content = new ObjectArrayList<>();
        for(ItemData data : packet.getSlots()) {
            if(data.getDefinition() == null || data.getDefinition().getIdentifier().equals(BlockID.AIR)) {
                content.add(data);
                continue;
            }
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), data);
            if(downgraded.getDefinition() == null
                    || Registries.ITEM.getOutdated(downgraded).getDefinition() == null
                    || data.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(downgraded).getDefinition().getIdentifier())) {
                content.add(downgraded);
                continue;
            }
            content.add(downgraded);
        }
        packet.setSlots(content);
    }

}
