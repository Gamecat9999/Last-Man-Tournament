package com.yourname.lms.Commands;

import com.yourname.lms.TournamentManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndTournamentCommand implements CommandExecutor {
    private final TournamentManager manager;

    public EndTournamentCommand(TournamentManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.endTournament();
        sender.sendMessage("Tournament ended.");
        return true;
    }
}