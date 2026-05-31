package org.powernukkitx.anyversion.handler.handlers;

import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;

public class AvailableCommandsHandler extends PacketHandler<AvailableCommandsPacket> {
    @Override
    public void handle(ProtocolPlayer player, AvailableCommandsPacket packet) {
        for(CommandData command : packet.getCommands()) {
            for(CommandOverloadData overload : command.getOverloads()) {
                for(CommandParamData param : overload.getOverloads()) {
                    if(param.getType() == null) {
                        param.setType(CommandParam.UNKNOWN);
                    }
                }
            }
        }
    }
}
