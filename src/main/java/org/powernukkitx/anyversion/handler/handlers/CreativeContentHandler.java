package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.payload.creative.CreativeGroupInfoPayload;
import org.cloudburstmc.protocol.bedrock.data.payload.creative.CreativeItemEntryPayload;
import org.cloudburstmc.protocol.bedrock.packet.CreativeContentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.util.ArrayList;
import java.util.List;

public class CreativeContentHandler extends PacketHandler<CreativeContentPacket> {

    @Override
    public void handle(ProtocolPlayer player, CreativeContentPacket packet) {
        List<CreativeItemEntryPayload> c = new ArrayList<>(packet.getEntries());
        packet.getEntries().clear();
        for(CreativeItemEntryPayload data : c) {
            if (data.getItemInstance().getDefinition() == null) {
                packet.getEntries().add(data);
                continue;
            }
            ItemData itemData = Registries.ITEM.downgrade(player.getVersion(), data.getItemInstance());
            if(itemData.getDefinition() == null || Registries.ITEM.getOutdated(itemData).getDefinition() == null) {
                packet.getEntries().add(data);
                continue;
            }
            if(itemData.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(itemData).getDefinition().getIdentifier())) continue;
            CreativeItemEntryPayload newData = new CreativeItemEntryPayload();
            newData.setItemInstance(itemData);
            newData.setCreativeNetId(data.getCreativeNetId());
            newData.setGroupIndex(data.getGroupIndex());
            packet.getEntries().add(newData);
        }

        for(CreativeGroupInfoPayload data : packet.getGroups()) {
            ItemData itemData = Registries.ITEM.downgrade(player.getVersion(), data.getGroupIconItem());
            data.setGroupIconItem(itemData);
        }
    }
}