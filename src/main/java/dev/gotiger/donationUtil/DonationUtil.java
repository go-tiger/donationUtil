package dev.gotiger.donationUtil;

import dev.gotiger.donationUtil.command.CommandManager;
import dev.gotiger.donationUtil.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DonationUtil extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        // 명령어 등록
        CommandManager.registerCommands(this);

        getLogger().info("donationUtil 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        getLogger().info("donationUtil 플러그인이 비활성화되었습니다.");
    }
}