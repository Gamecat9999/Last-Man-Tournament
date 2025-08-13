package com.yourname.lms;

import com.yourname.lms.Commands.EndTournamentCommand;
import com.yourname.lms.Commands.StartTournamentCommand;
import com.yourname.lms.Commands.SuddenDeathCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class LastManStanding extends JavaPlugin {
    private TournamentManager manager;

    @Override
    public void onEnable() {
        manager = new TournamentManager(this);

        // Register commands
        getCommand("starttournament").setExecutor(new StartTournamentCommand(manager));
        getCommand("endtournament").setExecutor(new EndTournamentCommand(manager));
        getCommand("suddendeath").setExecutor(new SuddenDeathCommand(manager));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new DeathListener(manager), this);

        getLogger().info("LastManStanding plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LastManStanding plugin disabled.");
    }

    public TournamentManager getManager() {
        return manager;
    }
}