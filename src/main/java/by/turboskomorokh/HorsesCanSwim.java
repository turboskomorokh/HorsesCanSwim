package by.turboskomorokh;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class HorsesCanSwim extends JavaPlugin {

    private BukkitTask swimTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!getConfig().getBoolean("enabled", true)) return;

        swimTask = new BukkitRunnable() {
            private int tick = 0;
            @Override
            public void run() {
                tick++;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Entity vehicle = player.getVehicle();
                    if (vehicle instanceof AbstractHorse horse && horse.isInWater()) {
                        SwimUtils.applySwimPhysics(horse, tick);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 3L);
    }


    @Override
    public void onDisable() {
        if (swimTask != null) {
            swimTask.cancel();
        }
    }
}
