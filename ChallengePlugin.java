package dev.challengeplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChallengePlugin extends JavaPlugin {

    private boolean running = false;
    private int seconds = 0;
    private BukkitRunnable timerTask;

    @Override
    public void onEnable() {
        getLogger().info("ChallengePlugin aktiviert.");
    }

    @Override
    public void onDisable() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        getLogger().info("ChallengePlugin deaktiviert.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("challenge")) return false;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Benutzung: /challenge <start|stop|reset>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                startTimer();
                Bukkit.broadcastMessage(ChatColor.GREEN + "Challenge gestartet!");
                break;
            case "stop":
                stopTimer();
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Challenge gestoppt!");
                break;
            case "reset":
                stopTimer();
                seconds = 0;
                Bukkit.broadcastMessage(ChatColor.RED + "Challenge zurückgesetzt!");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unbekannter Befehl.");
                break;
        }

        return true;
    }

    private void startTimer() {
        if (running) return;

        running = true;
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                seconds++;
                if (seconds % 60 == 0) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Challenge läuft: " + (seconds / 60) + " Minute(n).");
                }
            }
        };
        timerTask.runTaskTimer(this, 20, 20); // 1 Sekunde Takt
    }

    private void stopTimer() {
        if (!running) return;
        if (timerTask != null) timerTask.cancel();
        running = false;
    }
}