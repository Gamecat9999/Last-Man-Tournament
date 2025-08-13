package com.yourname.lms.Commands;

import com.yourname.lms.TournamentManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuddenDeathCommand implements CommandExecutor {
    private final TournamentManager manager;

    public SuddenDeathCommand(TournamentManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lastmanstanding.admin")) {
            sender.sendMessage("You don't have permission.");
            return true;
        }

        manager.triggerSuddenDeath();
        sender.sendMessage("Sudden Death triggered.");
        return true;
    }
}