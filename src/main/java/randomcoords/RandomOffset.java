package randomcoords;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

public class RandomOffset {
    private static Map<String, Offset<Integer>> xz = new ConcurrentHashMap<String, Offset<Integer>>();

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

        xz.put(player.getName(), new Offset<Integer>(x, z));
    }

    public static Integer getX(Player player) {
        if (!xz.containsKey(player.getName())) {
            newOffset(player);
        }
        return xz.get(player.getName()).getX();
    }

    public static Integer getZ(Player player) {
        if (!xz.containsKey(player.getName())) {
            newOffset(player);
        }
        return xz.get(player.getName()).getZ();
    }

    public static void clean(Player player) {
        xz.remove(player.getName());
    }
}
