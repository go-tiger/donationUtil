package dev.gotiger.donationUtil.config;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ConfigManager {
    private final DonationUtil plugin;
    private FileConfiguration mainConfig;
    private File protectionFile;
    private FileConfiguration protectionConfig;

    public ConfigManager(DonationUtil plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    private void loadConfigs() {
        plugin.saveDefaultConfig();
        mainConfig = plugin.getConfig();

        protectionFile = new File(plugin.getDataFolder(), "protection.yml");
        if (!protectionFile.exists()) {
            plugin.saveResource("protection.yml", false);
        }
        protectionConfig = YamlConfiguration.loadConfiguration(protectionFile);
    }

    public FileConfiguration getConfig() {
        return mainConfig;
    }

    public Map<String, Object> getMonsterConfig() {
        return mainConfig.getConfigurationSection("monster").getValues(false);
    }

    public Map<String, Object> getMonsterGroup(String groupName) {
        return mainConfig.getConfigurationSection("monster." + groupName).getValues(false);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        mainConfig = plugin.getConfig();
    }

    public FileConfiguration getProtectionConfig() {
        return protectionConfig;
    }
    public void saveProtectionConfig() {
        try {
            protectionConfig.save(protectionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadProtectionConfig() {
        protectionConfig = YamlConfiguration.loadConfiguration(protectionFile);
    }
}
