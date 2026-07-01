package org.powernukkitx.anyversion.handler.handlers;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.cloudburstmc.protocol.bedrock.codec.ActorDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.data.actor.ActorDataMap;
import org.cloudburstmc.protocol.bedrock.data.actor.ActorDataTypes;
import org.cloudburstmc.protocol.bedrock.data.actor.ActorFlags;
import org.cloudburstmc.protocol.bedrock.packet.SetActorDataPacket;
import org.cloudburstmc.protocol.bedrock.transformer.FlagTransformer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.lang.reflect.Field;

public class SetEntityDataHandler extends PacketHandler<SetActorDataPacket> {

    private static final Field entityDataField;
    private static final Field typeMapField;
    private static final Field toIdField;

    static {
        try {
            entityDataField = BaseBedrockCodecHelper.class.getDeclaredField("actorData");
            entityDataField.setAccessible(true);
            typeMapField = FlagTransformer.class.getDeclaredField("typeMap");
            typeMapField.setAccessible(true);
            toIdField = TypeMap.class.getDeclaredField("toId");
            toIdField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(ProtocolPlayer player, SetActorDataPacket packet) {
        ProtocolVersion protocol = player.getVersion();
        packet.setActorData(copyActorData(packet.getActorData()));
        fixEntityFlags(protocol, packet.getActorData());
    }

    public static ActorDataMap copyActorData(ActorDataMap meta) {
        ActorDataMap copy = new ActorDataMap();
        copy.putAll(meta);
        if (meta.getFlags() != null) {
            copy.putFlags(meta.getFlags().clone());
        }
        return copy;
    }

    public static final void fixEntityFlags(ProtocolVersion version, ActorDataMap meta) {
        if(version.protocol() < ProtocolVersion.MINECRAFT_PE_1_26_20.protocol()) {
            meta.remove(ActorDataTypes.RESERVED_139);
            meta.remove(ActorDataTypes.NAMEPLATE_RENDER_DISTANCE_MAX);
        }
        BedrockCodecHelper codec = version.helper();
        if(meta.getFlags() != null) {
            try {
                ActorDataTypeMap map = (ActorDataTypeMap) entityDataField.get(codec);
                if(map.fromType(ActorDataTypes.FLAGS).getTransformer() instanceof FlagTransformer transformer) {
                    TypeMap<ActorFlags> typeMap = (TypeMap<ActorFlags>) typeMapField.get(transformer);
                    Object2IntMap<ActorFlags> toId = (Object2IntMap<ActorFlags>) toIdField.get(typeMap);
                    for(ActorFlags flag : meta.getFlags().stream().toList()) {
                        if(!toId.containsKey(flag)) {
                            meta.setFlag(flag, false);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
