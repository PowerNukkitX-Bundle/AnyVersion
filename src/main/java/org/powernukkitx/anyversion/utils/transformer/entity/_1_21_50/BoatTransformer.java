package org.powernukkitx.anyversion.utils.transformer.entity._1_21_50;

import org.cloudburstmc.protocol.bedrock.data.actor.ActorDataTypes;
import org.cloudburstmc.protocol.bedrock.packet.AddActorPacket;
import org.powernukkitx.anyversion.utils.transformer.entity.EntityTransformer;

public class BoatTransformer extends EntityTransformer {

    @Override
    public void transform(AddActorPacket packet) {
        if (packet.getActorData().get(ActorDataTypes.VARIANT) instanceof Integer variant && variant == 9) {
            packet.getActorData().put(ActorDataTypes.NAME, "Outdated client\nPALE_OAK_" + packet.getActorType().replace("minecraft:", "").toUpperCase());
            packet.getActorData().put(ActorDataTypes.NAMETAG_ALWAYS_SHOW, (byte) 1);
            packet.getActorData().put(ActorDataTypes.VARIANT, 0);
        }
    }
}
