package dev.gotiger.donationUtil;

import dev.gotiger.donationUtil.command.CommandManager;
import dev.gotiger.donationUtil.config.ConfigManager;
import dev.gotiger.donationUtil.listener.EventListener;
import dev.gotiger.donationUtil.service.ProtectionService;
import org.bukkit.plugin.java.JavaPlugin;

public final class DonationUtil extends JavaPlugin {
    private ConfigManager configManager;
    private ProtectionService protectionService;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        protectionService = new ProtectionService(this, configManager);

        // 명령어 등록
        CommandManager.registerCommands(this, protectionService);

        // 이벤트 등록
        getServer().getPluginManager().registerEvents(new EventListener(protectionService), this);

        getLogger().info("donationUtil 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        getLogger().info("donationUtil 플러그인이 비활성화되었습니다.");
    }
}