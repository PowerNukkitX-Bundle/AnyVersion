package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.block.BlockID;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.registries.Registries;

public class MobArmorEquipmentHandler extends PacketHandler<MobArmorEquipmentPacket> {

    @Override
    public void handle(ProtocolPlayer player, MobArmorEquipmentPacket packet) {
        ItemData body = packet.getBody();
        if(!body.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), body);
            packet.setBody(downgraded);
        }
        ItemData boots = packet.getFeet();
        if(!boots.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), boots);
            packet.setFeet(downgraded);
        }
        ItemData leggings = packet.getLegs();
        if(!leggings.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), leggings);
            packet.setLegs(downgraded);
        }
        ItemData chestplate = packet.getTorso();
        if(!chestplate.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), chestplate);
            packet.setTorso(downgraded);
        }
        ItemData helmet = packet.getHead();
        if(!helmet.getDefinition().getIdentifier().equals(BlockID.AIR)) {
            ItemData downgraded = Registries.ITEM.downgrade(player.getVersion(), helmet);
            packet.setHead(downgraded);
        }
    }

}
