package dev.gotiger.donationUtil.listener;

import dev.gotiger.donationUtil.DonationUtil;
import dev.gotiger.donationUtil.service.ProtectionService;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EventListener implements Listener {
    private final ProtectionService protectionService;

    public EventListener(ProtectionService protectionService) {
        this.protectionService = protectionService;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String playerName = player.getName();

        boolean keepInventory = Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY));

        if (keepInventory) {
            player.sendMessage(ChatColor.RED + "현재 서버 게임룰에 의해 인벤토리가 보호되어 있습니다.");
            return;
        }

        int protectionCount = protectionService.getProtectionCount(playerName);
        if (protectionCount > 0) {
            protectionService.setProtectionCount(playerName, protectionCount - 1);
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.getDrops().clear();
            player.sendMessage(ChatColor.GREEN + "보호권을 사용하여 인벤토리가 보호되었습니다. 남은 보호권: " + ChatColor.YELLOW + (protectionCount - 1));
        }
    }
}