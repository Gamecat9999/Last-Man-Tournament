package com.yourname.lms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TournamentManager {
    private final LastManStanding plugin;
    private final Set<UUID> activePlayers = new HashSet<>();
    private boolean running = false;
    private BukkitTask timerTask;
    private BukkitTask scoreboardTask;
    private int secondsLeft = 600;

    public TournamentManager(LastManStanding plugin) {
        this.plugin = plugin;
    }

    public void startTournament() {
        activePlayers.clear();
        for (Player p : Bukkit.getOnlinePlayers()) {
            activePlayers.add(p.getUniqueId());
            p.sendMessage(ChatColor.GOLD + "Tournament started! Survive for 10 minutes!");
        }
        running = true;
        secondsLeft = 600;

        timerTask = Bukkit.getScheduler().runTaskLater(plugin, this::checkSuddenDeath, 20 * secondsLeft);
        scoreboardTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateScoreboard, 0L, 20L);
    }

    public void endTournament() {
        running = false;

        if (timerTask != null) timerTask.cancel();
        if (scoreboardTask != null) scoreboardTask.cancel();

        Scoreboard blank = Bukkit.getScoreboardManager().getNewScoreboard();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(blank);
            p.sendMessage(ChatColor.RED + "Tournament ended.");
        }

        activePlayers.clear();
    }

    public void triggerSuddenDeath() {
        if (!running) return;

        List<Player> contenders = new ArrayList<>();
        for (UUID uuid : activePlayers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                contenders.add(p);
            }
        }

        new ArenaGenerator().teleportPlayers(contenders);
        Bukkit.broadcastMessage(ChatColor.RED + "⚔ Sudden Death has begun! Fight in the sky arena!");
    }

    private void checkSuddenDeath() {
        if (!running) return;
        triggerSuddenDeath();
    }

    private void updateScoreboard() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isOnline()) continue;

            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("lms", "dummy", ChatColor.BOLD + "Last Man Standing");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            obj.getScore(ChatColor.YELLOW + "Time Left:").setScore(secondsLeft);
            obj.getScore(ChatColor.GREEN + "Players Left:").setScore(activePlayers.size());

            p.setScoreboard(board);
        }

        secondsLeft--;
        if (secondsLeft <= 0 && running) {
            checkSuddenDeath();
        }
    }

    public boolean isActive(Player player) {
        return activePlayers.contains(player.getUniqueId());
    }

    public void eliminate(Player player) {
        activePlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "You have been eliminated!");
    }

    public Set<UUID> getActivePlayers() {
        return activePlayers;
    }

    public boolean isRunning() {
        return running;
    }
}