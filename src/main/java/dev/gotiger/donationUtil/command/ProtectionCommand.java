package dev.gotiger.donationUtil.command;

import dev.gotiger.donationUtil.service.ProtectionService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ProtectionCommand implements CommandExecutor {
    private final ProtectionService protectionService;

    public ProtectionCommand(ProtectionService protectionService) {
        this.protectionService = protectionService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        protectionService.handleProtectionCommand(sender, args);
        return true;
    }
}
