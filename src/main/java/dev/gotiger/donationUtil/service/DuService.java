package dev.gotiger.donationUtil.service;

import dev.gotiger.donationUtil.DonationUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class DuService {
    private final DonationUtil plugin;
    public DuService(DonationUtil plugin) {
        this.plugin = plugin;
    }
    private final Random random = new Random();
    private final Map<UUID, UUID> lastTeleportedTo = new HashMap<>();

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

    public void strikeLightning(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "사용법: /du lightning <플레이어>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return;
        }

        FileConfiguration config = plugin.getConfig(); // Config 가져오기
        String damageType = config.getString("lightning.damage.type", "half");
        boolean fire = config.getBoolean("lightning.fire", true);

        World world = target.getWorld();
        Location location = target.getLocation();

        // 불이 붙는 설정 여부에 따라 번개 생성
        if (fire) {
            world.strikeLightning(location);
        } else {
            world.strikeLightningEffect(location);
        }

        // 데미지 계산
        double health = target.getHealth();
        double damage;

        if (damageType.equalsIgnoreCase("half")) {
            damage = health / 2;
            if (health <= 4.0) {
                target.setHealth(0);
                sender.sendMessage(ChatColor.RED + target.getName() + "는 즉사했습니다.");
            } else {
                target.damage(damage);
                sender.sendMessage(ChatColor.GREEN + target.getName() + "에게 하트의 절반(" + damage + ") 데미지를 입혔습니다.");
            }
        } else {
            try {
                int flatDamage = Integer.parseInt(damageType);
                damage = flatDamage;
                if (health <= damage) {
                    target.setHealth(0);
                    sender.sendMessage(ChatColor.RED + target.getName() + "는 즉사했습니다.");
                } else {
                    target.damage(damage);
                    sender.sendMessage(ChatColor.GREEN + target.getName() + "에게 " + damage + " 데미지를 입혔습니다.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Config에서 잘못된 데미지 타입이 설정되었습니다. (half 또는 정수)");
            }
        }
    }

    public void tpRandomPlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "사용법: /du tp <플레이어>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return;
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.remove(target);

        players.removeIf(player -> player.getGameMode() == GameMode.SPECTATOR);

        if (players.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "텔레포트할 다른 플레이어가 없습니다.");
            return;
        }

        UUID lastTargetUUID = lastTeleportedTo.get(target.getUniqueId());
        players.removeIf(player -> player.getUniqueId().equals(lastTargetUUID));

        if (players.isEmpty()) {
            players = new ArrayList<>(Bukkit.getOnlinePlayers());
            players.remove(target);
            players.removeIf(player -> player.getGameMode() == GameMode.SPECTATOR);
        }

        Player randomPlayer;
        if (players.size() == 1) {
            randomPlayer = players.get(0);
        } else {
            randomPlayer = players.get(new Random().nextInt(players.size()));
        }

        target.teleport(randomPlayer.getLocation());
        lastTeleportedTo.put(target.getUniqueId(), randomPlayer.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + target.getName() + "이(가) " + randomPlayer.getName() + "에게 텔레포트되었습니다.");
        target.sendMessage(ChatColor.AQUA + "당신은 " + randomPlayer.getName() + "에게 텔레포트되었습니다.");
    }

    public void clearInventoryPlayer(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "사용법: /du clear <플레이어>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어 " + args[1] + "를 찾을 수 없습니다.");
            return;
        }

        FileConfiguration config = plugin.getConfig(); // Config 가져오기
        boolean includeOffHand = config.getBoolean("clear.includeOffHand", true);
        boolean includeMainHand = config.getBoolean("clear.includeMainHand", true);
        boolean includeArmor = config.getBoolean("clear.includeArmor", true);
        boolean includeAir = config.getBoolean("clear.includeAir", false);

        Inventory inventory = target.getInventory();

        List<Integer> slotsToRemove = new ArrayList<>();

        if (!includeOffHand) {
            slotsToRemove.add(40);
        }

        if (!includeMainHand) {
            ItemStack mainHandItem = target.getInventory().getItemInMainHand();
            if (mainHandItem != null && mainHandItem.getType() != Material.AIR) {
                for (int i = 0; i < 9; i++) {
                    ItemStack hotbarItem = target.getInventory().getItem(i);
                    if (hotbarItem != null && hotbarItem.isSimilar(mainHandItem)) {
                        slotsToRemove.add(i);
                    }
                }
            }
        }

        if (!includeArmor) {
            for (int i = 36; i <= 39; i++) {
                slotsToRemove.add(i);
            }
        }

        if (includeAir) {
            for (int i = 0; i < 45; i++) {
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() == Material.AIR) {
                    slotsToRemove.add(i);
                }
            }
        }

        List<Integer> slotsToCheck = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            if (!slotsToRemove.contains(i)) {
                slotsToCheck.add(i);
            }
        }

        Random random = new Random();

        if (!includeAir || slotsToCheck.isEmpty()) {
            target.sendMessage(ChatColor.RED + "삭제할 아이템이 없습니다.");
            return;
        }

        int randomSlot = slotsToCheck.get(random.nextInt(slotsToCheck.size()));
        plugin.getLogger().info("remove slot number: " + randomSlot);

        ItemStack item = inventory.getItem(randomSlot);

        if (item != null && item.getType() != Material.AIR) {
            inventory.setItem(randomSlot, null);
            target.sendMessage(ChatColor.GREEN + "인벤토리에서 랜덤 아이템이 삭제되었습니다.");
        } else if (item != null && item.getType() == Material.AIR && includeAir) {
            inventory.setItem(randomSlot, null);
            target.sendMessage(ChatColor.GREEN + "빈 슬롯이 삭제되었습니다.");
        }
    }

    public void killPlayer(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "사용법: /du kill <플레이어>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어 " + args[1] + "를 찾을 수 없습니다.");
            return;
        }
        target.setHealth(0);
    }
}