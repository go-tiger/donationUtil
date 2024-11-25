package dev.gotiger.donationUtil.service;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DuService {
    private final DonationUtil plugin;
    public DuService(DonationUtil plugin) {
        this.plugin = plugin;
    }
    private final Random random = new Random();

    public void giveItem(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "사용법: /du item <플레이어> [아이템] [수량]");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return;
        }

        ItemStack item;
        if (args.length >= 3) {
            Material material = Material.matchMaterial(args[2]);
            if (material == null) {
                sender.sendMessage(ChatColor.RED + "잘못된 아이템 이름입니다.");
                return;
            }
            int amount = args.length >= 4 ? Integer.parseInt(args[3]) : 1;
            item = new ItemStack(material, amount);
        } else {
            Material randomItem = Material.values()[random.nextInt(Material.values().length)];
            item = new ItemStack(randomItem, 1);
        }

        target.getInventory().addItem(item);
        sender.sendMessage(ChatColor.GREEN + target.getName() + "에게 " + item.getType() + " x" + item.getAmount() + " 아이템을 지급했습니다.");
    }
}