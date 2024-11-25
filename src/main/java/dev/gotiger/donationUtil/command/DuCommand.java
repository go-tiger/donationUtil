package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.config.ConfigManager;
import dev.gotiger.donationUtil.service.DuService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DuCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final DuService duService;

    public DuCommand(ConfigManager configManager, DuService duService) {
        this.configManager = configManager;
        this.duService = duService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        sender.sendMessage("123");

        switch (args[0].toLowerCase()) {
            case "item":
                sender.sendMessage(args);
                sender.sendMessage("123");
                duService.giveItem(sender, args);
                break;

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