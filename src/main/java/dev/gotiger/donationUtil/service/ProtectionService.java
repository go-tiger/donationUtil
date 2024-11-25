package dev.gotiger.donationUtil.service;

import dev.gotiger.donationUtil.DonationUtil;
import dev.gotiger.donationUtil.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProtectionService {
    private final DonationUtil plugin;
    private final ConfigManager configManager;

    public ProtectionService(DonationUtil plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public int getProtectionCount(String playerName) {
        return configManager.getProtectionConfig().getInt("players." + playerName, 0);
    }

    public void setProtectionCount(String playerName, int count) {
        configManager.getProtectionConfig().set("players." + playerName, count);
        configManager.saveProtectionConfig();
    }

    public void addProtection(String playerName, int amount) {
        int current = getProtectionCount(playerName);
        setProtectionCount(playerName, current + amount);
    }

    public void handleProtectionCommand(CommandSender sender, String[] args) {
        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            int count = getProtectionCount(player.getName());
            player.sendMessage(ChatColor.GREEN + "현재 보호권 개수: " + ChatColor.YELLOW + count);
        } else if (args.length == 1 && sender.hasPermission("protection.check")) {
            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "플레이어 '" + targetName + "'를 찾을 수 없습니다.");
                return;
            }

            int count = getProtectionCount(targetName);
            sender.sendMessage(ChatColor.GREEN + targetName + " 보호권 개수: " + ChatColor.YELLOW + count);
        } else {
            sender.sendMessage(ChatColor.RED + "사용법: /보호권 [플레이어]");
        }
    }
}
