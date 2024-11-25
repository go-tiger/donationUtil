package dev.gotiger.donationUtil.config;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final DonationUtil plugin;

    public ConfigManager(DonationUtil plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }


    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }
}