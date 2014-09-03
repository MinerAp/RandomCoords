package randomcoords;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RandomOffset implements Listener {
    private static ConcurrentHashMap<String, Integer> x = new ConcurrentHashMap<String, Integer>();
    private static ConcurrentHashMap<String, Integer> z = new ConcurrentHashMap<String, Integer>();

    public static void newOffset(Player player) {
        int n = 65536;

        int min_x = -n - player.getLocation().getBlockX() / 16;
        int max_x = n - player.getLocation().getBlockX() / 16;
        int min_z = -n - player.getLocation().getBlockZ() / 16;
        int max_z = n - player.getLocation().getBlockZ() / 16;

        int x = min_x + (int) (Math.random() * (max_x - min_x + 1));
        int z = min_z + (int) (Math.random() * (max_z - min_z + 1));

        x *= 16;
        z *= 16;
        if (player.hasPermission("randomcoords.bypass")) {
            x = 0;
            z = 0;
        }
        RandomOffset.x.put(player.getName(), x);
        RandomOffset.z.put(player.getName(), z);
    }

    public static Integer getX(Player player) {
        if (!x.containsKey(player.getName())) {
            newOffset(player);
        }
        return (Integer) x.get(player.getName());
    }

    public static Integer getZ(Player player) {
        if (!z.containsKey(player.getName())) {
            newOffset(player);
        }
        return (Integer) z.get(player.getName());
    }

    public static void clean(Player player) {
        x.remove(player.getName());
        z.remove(player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clean(event.getPlayer());
    }
}
