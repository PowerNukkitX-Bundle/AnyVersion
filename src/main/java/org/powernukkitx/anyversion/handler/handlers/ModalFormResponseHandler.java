package org.powernukkitx.anyversion.handler.handlers;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDivider;
import cn.nukkit.form.element.ElementHeader;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.custom.ElementCustom;
import cn.nukkit.form.window.CustomForm;
import cn.nukkit.form.window.Form;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.powernukkitx.anyversion.handler.PacketHandler;
import org.powernukkitx.anyversion.manager.ProtocolPlayer;
import org.powernukkitx.anyversion.utils.ProtocolVersion;

public class ModalFormResponseHandler extends PacketHandler<ModalFormResponsePacket> {
    @Override
    public void handle(ProtocolPlayer player, ModalFormResponsePacket packet) {
        if (player.protocol() <= ProtocolVersion.MINECRAFT_PE_1_21_70.protocol()) {
            Player p = player.nukkitPlayer();
            if (p == null) return;
            Form<?> form = p.getFormWindows().get(packet.getFormID());
            if (form instanceof CustomForm form1 && packet.getJsonResponse() != null) {
                JsonArray res = JsonParser.parseString(packet.getJsonResponse().trim()).getAsJsonArray();
                JsonArray array = new JsonArray();
                int element = -1;
                for (int idx = 0; idx < form1.elements().size(); idx++) {
                    ElementCustom element1 = form1.elements().get(idx);
                    if (!(element1 instanceof ElementHeader || element1 instanceof ElementDivider)) {
                        if (player.protocol() == ProtocolVersion.MINECRAFT_PE_1_21_70.protocol() && element1 instanceof ElementLabel) {
                            array.add("");
                            continue;
                        }
                        element++;
                        array.add(res.get(element));
                    } else {
                        array.add("");
                    }
                }
                packet.setJsonResponse(array.toString());
            }
        }
    }
}
