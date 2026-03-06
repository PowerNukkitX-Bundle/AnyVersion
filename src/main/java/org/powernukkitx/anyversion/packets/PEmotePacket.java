package org.powernukkitx.anyversion.packets;

import cn.nukkit.network.connection.util.HandleByteBuf;
import cn.nukkit.network.protocol.EmotePacket;
import cn.nukkit.network.protocol.types.EmoteFlag;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class PEmotePacket extends EmotePacket implements ProtocolizedPacket, Cloneable {

    protected ProtocolPlayer protocolPlayer;

    @Override
    public void decode(HandleByteBuf byteBuf) {
        this.runtimeId = byteBuf.readEntityRuntimeId();
        this.emoteID = byteBuf.readString();
        if (protocolPlayer.protocol() >= ProtocolVersion.MINECRAFT_PE_1_21_30.protocol()) {
            this.emoteDuration = byteBuf.readUnsignedVarInt();
        } else this.emoteDuration = 0;
        this.xuid = byteBuf.readString();
        this.platformId = byteBuf.readString();
        this.flags = EmoteFlag.fromByte(byteBuf.readByte());
    }

    @Override
    public void encode(HandleByteBuf byteBuf) {
        byteBuf.writeEntityRuntimeId(this.runtimeId);
        byteBuf.writeString(this.emoteID);
        if (protocolPlayer.protocol() >= ProtocolVersion.MINECRAFT_PE_1_21_30.protocol()) {
            byteBuf.writeUnsignedVarInt(this.emoteDuration);
        }
        byteBuf.writeString(this.xuid != null ? this.xuid : "");
        byteBuf.writeString(this.platformId != null ? this.platformId : "");
        byteBuf.writeByte(EmoteFlag.toByte(this.flags));
    }

    @Override
    public void setPlayer(ProtocolPlayer player) {
        this.protocolPlayer = player;
    }

    @Override
    public PEmotePacket clone() {
        PEmotePacket clone = new PEmotePacket();
        clone.protocolPlayer = this.protocolPlayer;
        clone.emoteDuration = this.emoteDuration;
        clone.emoteID = this.emoteID;
        clone.flags = this.flags;
        clone.platformId = this.platformId;
        clone.runtimeId = this.runtimeId;
        clone.xuid = this.xuid;
        return clone;
    }
}
