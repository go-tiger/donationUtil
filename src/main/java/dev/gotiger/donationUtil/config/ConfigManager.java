package dev.gotiger.donationUtil.config;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class ConfigManager {
    private final DonationUtil plugin;

    public ConfigManager(DonationUtil plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public Map<String, Object> getMonsterConfig() {
        return getConfig().getConfigurationSection("monster").getValues(false);
    }

    public  Map<String, Object> getMonsterGroup(String groupName) {
        return getConfig().getConfigurationSection("monster." + groupName).getValues(false);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }
}