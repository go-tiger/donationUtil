package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DuCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public DuCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                configManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "설정이 리로드되었습니다.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "알 수 없는 명령어입니다.");
                break;
        }
        return true;
    }
}