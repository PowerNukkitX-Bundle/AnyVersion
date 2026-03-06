package org.powernukkitx.anyversion.handler.handlers;

import lombok.extern.slf4j.Slf4j;
import org.cloudburstmc.protocol.bedrock.data.auth.AuthPayload;
import org.cloudburstmc.protocol.bedrock.data.auth.AuthType;
import org.cloudburstmc.protocol.bedrock.data.auth.CertificateChainPayload;
import org.cloudburstmc.protocol.bedrock.data.auth.TokenPayload;
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

import java.util.ArrayList;

@Slf4j
public class LoginHandler extends PacketHandler<LoginPacket> {

    @Override
    public void handle(ProtocolPlayer player, LoginPacket packet) {
        if(packet.getProtocolVersion() < ProtocolVersion.MINECRAFT_PE_1_21_110.protocol()) {
            AuthPayload authPayload = packet.getAuthPayload();
            if(authPayload instanceof TokenPayload payload) {
                CertificateChainPayload certificateChainPayload = new CertificateChainPayload(new ArrayList<>());
                packet.setAuthPayload(new CertificateChainPayload(certificateChainPayload.getChain(), AuthType.SELF_SIGNED));
            }
        }
        if(packet.getProtocolVersion() < ProtocolVersion.MINECRAFT_PE_1_21_90.protocol()) {
            CertificateChainPayload certificateChainPayload = (CertificateChainPayload) packet.getAuthPayload();
            packet.setAuthPayload(new CertificateChainPayload(certificateChainPayload.getChain(), AuthType.SELF_SIGNED));
        }
    }

}
