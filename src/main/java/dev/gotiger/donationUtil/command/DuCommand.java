package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.config.ConfigManager;
import dev.gotiger.donationUtil.service.DuService;
import dev.gotiger.donationUtil.service.ProtectionService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuCommand implements CommandExecutor, TabCompleter {
    private final ConfigManager configManager;
    private final DuService duService;
    private final ProtectionService protectionService;

    public DuCommand(ConfigManager configManager, DuService duService, ProtectionService protectionService) {
        this.configManager = configManager;
        this.duService = duService;
        this.protectionService = protectionService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "item":
                duService.giveItem(sender, args);
                break;

            case "monster":
                duService.spawnMonsters(sender, args);
                break;

            case "lightning":
                duService.strikeLightning(sender, args);
                break;

            case "tp":
                duService.tpRandomPlayer(sender, args);
                break;

            case "inven":
                protectionService.giveInventoryProtection(sender, args);
                break;

            case "clear":
                duService.clearInventoryPlayer(sender, args);
                break;

            case "kill":
                duService.killPlayer(sender, args);
                break;

            case "lava":
                duService.lavaPlayer(sender, args);
                break;

            case "sky":
                duService.skyPlayer(sender, args);
                break;

            case "buff":
                duService.buffPlayer(sender, args);
                break;

            case "debuff":
                duService.debuffPlayer(sender, args);
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("item", "monster", "lightning", "tp", "inven", "clear", "kill", "lava", "sky", "buff", "debuff", "reload");
            StringUtil.copyPartialMatches(args[0], subCommands, completions);
        } else if (args.length == 2) {
            if (Arrays.asList("item", "monster", "tp", "clear", "kill", "lava", "sky", "buff", "debuff").contains(args[0].toLowerCase())) {
                List<String> playerNames = new ArrayList<>();
                for (Player player : sender.getServer().getOnlinePlayers()) {
                    playerNames.add(player.getName());
                }
                StringUtil.copyPartialMatches(args[1], playerNames, completions);
            }
        }
        return completions;
    }
}