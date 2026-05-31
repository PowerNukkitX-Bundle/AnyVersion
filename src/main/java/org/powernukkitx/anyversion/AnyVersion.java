package org.powernukkitx.anyversion;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import lombok.Getter;
import org.powernukkitx.anyversion.manager.ProtocolManager;
import org.powernukkitx.anyversion.registries.Registries;
import org.powernukkitx.anyversion.utils.ProtocolVersion;
import org.powernukkitx.anyversion.utils.ServerStartedListener;

public class AnyVersion extends PluginBase {

    @Getter
    private static AnyVersion plugin;

    public static AnyVersion getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        AnyVersion.plugin = this;
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ProtocolManager(), this);
        pluginManager.registerEvents(new ServerStartedListener(), this);
        Registries.init();
    }
}
