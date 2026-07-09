package org.powernukkitx.anyversion.utils;

import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.server.ServerStartedEvent;

public class ServerStartedListener implements Listener {

    @EventHandler
    public void on(ServerStartedEvent event) {
        CloudburstRegistry.get().reload();
        for(ProtocolVersion version : ProtocolVersion.getVersions()) {
            version.helper().setItemDefinitions(CloudburstRegistry.get().getItemDefinitionRegistry());
            version.helper().setBlockDefinitions(CloudburstRegistry.get().getBlockDefinitionRegistry());
        }
    }

}
