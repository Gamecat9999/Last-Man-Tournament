package com.yourname.lms;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class ArenaGenerator {
    private static final int PLATFORM_SIZE = 50;
    private static final int HEIGHT = 200;
    private static final int MAX_ATTEMPTS = 10;

    public Location generateArena(World world) {
        Random rand = new Random();
        Location base = null;

        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int x = rand.nextInt(10000) - 5000;
            int z = rand.nextInt(10000) - 5000;
            Location check = new Location(world, x, world.getHighestBlockYAt(x, z), z);
            Material ground = check.getBlock().getType();

            if (!ground.name().contains("WATER") && !ground.name().contains("LAVA")) {
                base = new Location(world, x, HEIGHT, z);
                break;
            }
        }

        if (base == null) {
            base = new Location(world, 0, HEIGHT, 0); // fallback
        }

        generatePlatform(base);
        return base;
    }

    private void generatePlatform(Location center) {
        World world = center.getWorld();
        int half = PLATFORM_SIZE / 2;

        for (int x = -half; x <= half; x++) {
            for (int z = -half; z <= half; z++) {
                Location blockLoc = center.clone().add(x, 0, z);
                world.getBlockAt(blockLoc).setType(Material.STONE);
            }
        }
    }

    public void teleportPlayers(List<Player> players) {
        World world = Bukkit.getWorlds().get(0); // default overworld
        Location arenaCenter = generateArena(world);

        int radius = PLATFORM_SIZE / 3;
        double angleStep = 360.0 / players.size();
        double angle = 0;

        for (Player p : players) {
            double radians = Math.toRadians(angle);
            double xOffset = radius * Math.cos(radians);
            double zOffset = radius * Math.sin(radians);

            Location spawn = arenaCenter.clone().add(xOffset, 1, zOffset);
            p.teleport(spawn);
            p.sendMessage(ChatColor.RED + "You have been summoned to the Sudden Death Arena!");
            angle += angleStep;
        }
    }
}