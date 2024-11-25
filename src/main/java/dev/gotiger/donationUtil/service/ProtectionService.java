package dev.gotiger.donationUtil.service;

import dev.gotiger.donationUtil.DonationUtil;
import dev.gotiger.donationUtil.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtectionService {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    // 생성자에서 DonationUtil과 ConfigManager를 받도록 수정
    public ProtectionService(JavaPlugin plugin, ConfigManager configManager) {
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
            sender.sendMessage(ChatColor.RED + "사용법: /보호권");
        }
    }

    public void giveInventoryProtection(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "사용법: /du inven <플레이어>");
            return;
        }

        // 대상 플레이어 확인
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return;
        }

        // 닉네임 기반으로 보호권 관리
        String targetName = target.getName(); // 플레이어의 닉네임
        FileConfiguration protectionConfig = configManager.getProtectionConfig();

        // 기존 보호권 확인 및 추가
        int currentProtection = protectionConfig.getInt("players." + targetName, 0);
        int updatedProtection = currentProtection + 1;

        protectionConfig.set("players." + targetName, updatedProtection);

        // 보호권 설정 저장
        configManager.saveProtectionConfig();

        // 메시지 전송
        sender.sendMessage(ChatColor.GREEN + targetName + "에게 인벤 보호권을 1회 지급했습니다. 현재 보호권: " + updatedProtection);
        target.sendMessage(ChatColor.GOLD + "인벤 보호권 1회가 지급되었습니다. 현재 보호권: " + updatedProtection);
    }
}
