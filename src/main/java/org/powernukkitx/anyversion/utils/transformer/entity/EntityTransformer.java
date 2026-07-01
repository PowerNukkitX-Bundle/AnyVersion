package org.powernukkitx.anyversion.utils.transformer.entity;


import org.cloudburstmc.protocol.bedrock.packet.AddActorPacket;

public abstract class EntityTransformer {

    public abstract void transform(AddActorPacket definition);

}
