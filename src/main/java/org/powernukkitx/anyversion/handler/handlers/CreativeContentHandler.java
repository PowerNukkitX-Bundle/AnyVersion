package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.payload.creative.CreativeItemEntryPayload;
import org.cloudburstmc.protocol.bedrock.packet.CreativeContentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.util.Iterator;

public class CreativeContentHandler extends PacketHandler<CreativeContentPacket> {

    @Override
    public void handle(ProtocolPlayer player, CreativeContentPacket packet) {
        Iterator<CreativeItemEntryPayload> iterator = packet.getEntries().iterator();
        while (iterator.hasNext()) {
            CreativeItemEntryPayload data = iterator.next();
            if (data.getItemInstance().getDefinition() == null) {
                continue;
            }
            ItemData itemData = Registries.ITEM.downgrade(player.getVersion(), data.getItemInstance());
            if (itemData.getDefinition() == null || Registries.ITEM.getOutdated(itemData).getDefinition() == null) {
                continue;
            }
            if (itemData.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(itemData).getDefinition().getIdentifier())) {
                iterator.remove();
                continue;
            }
            data.setItemInstance(itemData);
        }
    }

}
