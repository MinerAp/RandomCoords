package randomcoords;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketEvent;

public class PrecisionFix {
    private static final ConcurrentHashMap<String, Double> x = new ConcurrentHashMap<String, Double>();
    private static final ConcurrentHashMap<String, Double> z = new ConcurrentHashMap<String, Double>();

    public static void onServerChangePos(PacketEvent event, int index) {
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        x.put(event.getPlayer().getName(), curr_x);
        z.put(event.getPlayer().getName(), curr_z);
    }

    public static void onClientChangePos(PacketEvent event, int index) {
        if (!x.containsKey(event.getPlayer().getName())) {
            return;
        }
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        double dx = x.get(event.getPlayer().getName()) - curr_x;
        double dz = z.get(event.getPlayer().getName()) - curr_z;
        if (Math.abs(dx) + Math.abs(dz) > 0.00390625D) {
            return;
        }
        event.getPacket().getDoubles().write(index + 0, x.get(event.getPlayer().getName()));
        event.getPacket().getDoubles().write(index + 2, z.get(event.getPlayer().getName()));

        x.remove(event.getPlayer().getName());
        z.remove(event.getPlayer().getName());
    }

    public static void clean(Player player) {
        x.remove(player.getName());
        z.remove(player.getName());
    }
}
