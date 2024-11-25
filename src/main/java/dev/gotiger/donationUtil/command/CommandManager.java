package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.DonationUtil;
import dev.gotiger.donationUtil.config.ConfigManager;
import dev.gotiger.donationUtil.service.DuService;
import dev.gotiger.donationUtil.service.ProtectionService;

public class CommandManager {
    public static void registerCommands(DonationUtil plugin, ProtectionService protectionService) {
        ConfigManager configManager = new ConfigManager(plugin);
        DuService duService = new DuService(plugin);

        plugin.getCommand("du").setExecutor(new DuCommand(configManager, duService, protectionService));
        plugin.getCommand("보호권").setExecutor(new ProtectionCommand(protectionService));
    }
}
