package dev.gotiger.donationUtil.service;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
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

    private String getMonsterChance(FileConfiguration config, int chance) {
        int typeChance = 0;

        for (String key : Arrays.asList("common", "rare", "boss")) {
            typeChance += config.getInt("monster." + key + ".chance");
            if (chance <= typeChance) {
                return key;
            }
        }
        return null;
    }

    public void spawnMonsters(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "사용법: /du monster <플레이어> <수치>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return;
        }
        int count;
        try {
            count = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "수치는 숫자로 입력해야 합니다.");
            return;
        }

        FileConfiguration config = plugin.getConfig();
        if (!config.contains("monster")) {
            sender.sendMessage(ChatColor.RED + "설정값에 monster 설정이 없습니다.");
            return;
        }

        Location location = target.getLocation();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int chance = random.nextInt(100) + 1;
            plugin.getLogger().info("chance: " + chance);
            String category = getMonsterChance(config, chance);

            if (category == null) {
                sender.sendMessage(ChatColor.RED + "몬스터 확률 설정이 잘못되었습니다.");
                return;
            }

            List<String> monsters = config.getStringList("monster." + category + ".list");
            if (monsters.isEmpty()) {
                sender.sendMessage(ChatColor.RED + category + "카테고리에 몬스터가 없습니다.");
                continue;
            }

            String randomMonster = monsters.get(random.nextInt(monsters.size()));
            EntityType type = EntityType.fromName(randomMonster);

            if (type == null) {
                sender.sendMessage(ChatColor.RED + randomMonster + "은(는) 유효한 몬스터가 아닙니다.");
                continue;
            }

            target.getWorld().spawnEntity(target.getLocation(), type);
            sender.sendMessage(ChatColor.GREEN + target.getName() + " 주변에 " + randomMonster + "를 소환했습니다.");
        }
    }
}