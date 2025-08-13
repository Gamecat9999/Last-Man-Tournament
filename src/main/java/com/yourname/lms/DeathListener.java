package com.yourname.lms;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final TournamentManager manager;

    public DeathListener(TournamentManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!manager.isRunning()) return;
        if (!manager.isActive(player)) return;

        manager.eliminate(player);
        event.setDeathMessage(ChatColor.DARK_RED + player.getName() + " has been eliminated!");

        int remaining = manager.getActivePlayers().size();
        if (remaining == 1) {
            Player winner = manager.getActivePlayers().stream()
                .map(uuid -> player.getServer().getPlayer(uuid))
                .filter(p -> p != null)
                .findFirst()
                .orElse(null);

            if (winner != null) {
                winner.sendMessage(ChatColor.GOLD + "You are the last one standing!");
                player.getServer().broadcastMessage(ChatColor.GREEN + winner.getName() + " wins the tournament!");
            }

            manager.endTournament();
        }
    }
}