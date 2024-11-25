package dev.gotiger.donationUtil;

import org.bukkit.plugin.java.JavaPlugin;

public final class DonationUtil extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("donationUtil 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        getLogger().info("donationUtil 플러그인이 비활성화되었습니다.");
    }
}