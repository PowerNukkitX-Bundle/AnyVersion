package org.powernukkitx.anyversion;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import lombok.Getter;
import org.powernukkitx.anyversion.config.AnyVersionConfig;
import org.powernukkitx.anyversion.manager.ProtocolManager;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.ServerStartedListener;

public class AnyVersion extends PluginBase {

    @Getter
    private static AnyVersion plugin;

    @Getter
    private AnyVersionConfig configuration;

    @Override
    public void onLoad() {
        AnyVersion.plugin = this;
        configuration = new AnyVersionConfig();
    }

    @Override
    public void onEnable() {
        if(ProtocolInfo.CURRENT_PROTOCOL > ProtocolVersion.getMax().protocol()) {
            getPlugin().getLogger().critical("The current protocol is not supported by AnyVersion! Please update the plugin.");
            setEnabled(false);
            return;
        }
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ProtocolManager(), this);
        pluginManager.registerEvents(new ServerStartedListener(), this);
        Registries.init();
    }
}
