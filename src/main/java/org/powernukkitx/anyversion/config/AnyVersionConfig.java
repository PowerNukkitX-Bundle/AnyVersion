package org.powernukkitx.anyversion.config;

import lombok.Getter;
import org.powernukkitx.anyversion.AnyVersion;

import org.powernukkitx.utils.Config;

import java.util.Map;

public class AnyVersionConfig {

    public static String CONFIG_VERSION = "1.0.0";

    @Getter
    private final int lowestVersion;
    @Getter
    private final String configVersion;

    public AnyVersionConfig() {
        var plugin = AnyVersion.getPlugin();
        plugin.saveDefaultConfig();

        String currentVersion = plugin.getConfig().getString("configVersion", "0.0.0");

        if (!currentVersion.equals(CONFIG_VERSION)) {
            mergeMissingKeys(plugin);

            plugin.getConfig().set("configVersion", CONFIG_VERSION);
            plugin.saveConfig();
        }

        this.lowestVersion = plugin.getConfig().getInt("lowestVersion", 618);
        this.configVersion = plugin.getConfig().getString("configVersion", CONFIG_VERSION);
    }

    private void mergeMissingKeys(AnyVersion plugin) {
        try {
            Config defaultConfig = new Config("config.yml", Config.YAML);
            Config currentConfig = plugin.getConfig();

            for (Map.Entry<String, Object> entry : defaultConfig.getAll().entrySet()) {
                if (!currentConfig.exists(entry.getKey())) {
                    currentConfig.set(entry.getKey(), entry.getValue());
                }
            }

        } catch (Exception e) {
            plugin.getLogger().error("Config merge error: ", e);
        }
    }
}