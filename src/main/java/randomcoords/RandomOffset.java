package randomcoords;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

public class RandomOffset {
    private static Map<String, Offset<Integer>> xz = new ConcurrentHashMap<String, Offset<Integer>>();

    public static void generateOffset(Player player) {
        int x = -player.getLocation().getBlockX();
        int z = -player.getLocation().getBlockZ();

        if (player.hasPermission("randomcoords.bypass")) {
            x = 0;
            z = 0;
        }

        x -= x % 16;
        z -= z % 16;

        xz.put(player.getName(), new Offset<Integer>(x, z));
    }

    public static void removeOffset(Player player) {
        xz.remove(player.getName());
    }

    public static Offset<Integer> getOffset(Player player) {
        if (!xz.containsKey(player.getName())) {
            generateOffset(player);
        }

        return xz.get(player.getName());
    }
}
