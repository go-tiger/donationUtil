package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.DonationUtil;
import dev.gotiger.donationUtil.config.ConfigManager;

public class CommandManager {
    public static void registerCommands(DonationUtil plugin) {
        ConfigManager configManager = new ConfigManager(plugin);  // ConfigManager 생성
        plugin.getCommand("du").setExecutor(new DuCommand(configManager));  // DuCommand에 전달
    }
}
