package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.camera.CameraAimAssistPresetDefinition;
import org.cloudburstmc.protocol.bedrock.packet.CameraAimAssistPresetsPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class CameraAimAssistPresetsHandler extends PacketHandler<CameraAimAssistPresetsPacket> {

    @Override
    public void handle(ProtocolPlayer player, CameraAimAssistPresetsPacket packet) {
        for (CameraAimAssistPresetDefinition preset : packet.getPresets()) {
            if (preset.getCategories() == null) {
                preset.setCategories("");
            }
        }
    }
}
