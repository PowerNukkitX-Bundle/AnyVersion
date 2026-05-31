package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.inventory.CreativeItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.CreativeContentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

import java.util.ArrayList;
import java.util.List;

public class CreativeContentHandler extends PacketHandler<CreativeContentPacket> {

    @Override
    public void handle(ProtocolPlayer player, CreativeContentPacket packet) {
        List<CreativeItemData> c = new ArrayList<>(packet.getContents());
        packet.getContents().clear();
        for(CreativeItemData data : c) {
            ItemData itemData = Registries.ITEM.downgrade(player.getVersion(), data.getItemInstance());
            if(itemData.getDefinition().getIdentifier().equals(Registries.ITEM.getOutdated(itemData).getDefinition().getIdentifier())) continue;
            CreativeItemData newData = CreativeItemData.builder()
                    .itemInstance(itemData)
                    .creativeNetId(data.getCreativeNetId())
                    .groupIndex(data.getGroupIndex())
                    .build();
            packet.getContents().add(newData);
        }
    }

}
