package by.turboskomorokh;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import org.bukkit.Particle;

public final class HorsesCanSwim extends JavaPlugin {

  private BukkitTask swimTask;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    if (!getConfig().getBoolean("enabled", true)) return;

    swimTask = new BukkitRunnable() {
      private int tickCounter = 0;

      @Override
      public void run() {
        tickCounter++;

        for (Player player : Bukkit.getOnlinePlayers()) {
          Entity vehicle = player.getVehicle();
          if (!(vehicle instanceof AbstractHorse horse)) {
            continue;
          }

          if (!horse.isInWater()) {
            continue;
          }

          double waterSurfaceY = horse.getLocation().getBlock().getY() + 0.6;
          double currentY = horse.getLocation().getY();
          double diff = waterSurfaceY - currentY;

          Vector vel = horse.getVelocity();

          double oscillation = 0.15 * Math.sin(tickCounter * 0.3);

          double finalY;
          if (Math.abs(diff) > 0.05) {
            double lift = diff * 0.1;
            lift = Math.max(Math.min(lift, 0.15), -0.1);
            finalY = vel.getY() + lift + oscillation * 0.1;
            finalY = Math.max(Math.min(finalY, 0.3), -0.2);
          } else {
            finalY = vel.getY() * 0.5 + oscillation * 0.05;
          }

          double slowX = vel.getX() * 1.35;
          double slowZ = vel.getZ() * 1.35;

          horse.setVelocity(new Vector(slowX, finalY, slowZ));

          if (tickCounter % 15 == 0) {
            double offsetX = (Math.random() - 0.5) * 0.5;
            double offsetZ = (Math.random() - 0.5) * 0.5;
            horse.getWorld().spawnParticle(Particle.BUBBLE, horse.getLocation().add(offsetX, 0.1, offsetZ), 30, 0.2, 0.1, 0.2, 0.02);
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
