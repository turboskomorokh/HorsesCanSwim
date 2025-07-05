package by.turboskomorokh;

import org.bukkit.Particle;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.util.Vector;

public class SwimUtils {

    public static void applySwimPhysics(AbstractHorse horse, int tickCounter) {
        Vector vel = horse.getVelocity();
        double waterSurfaceY = horse.getLocation().getBlockY() + 0.5;
        double currentY = horse.getLocation().getY();
        double diff = waterSurfaceY - currentY;

        double oscillation = 0.15 * Math.sin(tickCounter * 0.3);
        double finalY = computeVerticalVelocity(vel.getY(), diff, oscillation);

        Vector finalVel = new Vector(
                vel.getX() * 1.5,
                finalY,
                vel.getZ() * 1.5
        );
        horse.setVelocity(finalVel);

        if (tickCounter % 15 == 0) {
            spawnBubbles(horse);
        }
    }

    private static double computeVerticalVelocity(double currentVelY, double diff, double oscillation) {
        if (Math.abs(diff) > 0.05) {
            double lift = clamp(diff * 0.1, -0.1, 0.15);
            double finalY = currentVelY + lift + oscillation * 0.1;
            return clamp(finalY, -0.2, 0.3);
        } else {
            return currentVelY * 0.5 + oscillation * 0.05;
        }
    }

    private static void spawnBubbles(AbstractHorse horse) {
        double offsetX = (Math.random() - 0.5) * 0.5;
        double offsetZ = (Math.random() - 0.5) * 0.5;
        horse.getWorld().spawnParticle(
                Particle.BUBBLE,
                horse.getLocation().add(offsetX, 0.1, offsetZ),
                45, 0.2, 0.1, 0.2, 0.02
        );
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
