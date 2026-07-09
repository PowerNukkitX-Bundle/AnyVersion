package org.powernukkitx.anyversion.utils.transformer.entity;

import org.powernukkitx.entity.EntityID;
import org.cloudburstmc.protocol.bedrock.data.actor.ActorDataTypes;
import org.cloudburstmc.protocol.bedrock.packet.AddActorPacket;

public class UnknownEntityTransformer extends EntityTransformer {

    @Override
    public void transform(AddActorPacket packet) {
        packet.getActorData().put(ActorDataTypes.NAME, "§c§lOutdated client\n§c" + packet.getActorType().replace("minecraft:", "").toUpperCase());
        packet.getActorData().put(ActorDataTypes.NAMETAG_ALWAYS_SHOW, (byte) 1);
        packet.setActorType(EntityID.ZOMBIE);

    }

}
