package randomcoords;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketEvent;

public class PrecisionFix {
    private static final Map<String, Offset<Double>> xz = new ConcurrentHashMap<String, Offset<Double>>();

    public static void onServerChangePos(PacketEvent event, int index) {
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        xz.put(event.getPlayer().getName(), new Offset<Double>(curr_x, curr_z));
    }

    public static void onClientChangePos(PacketEvent event, int index) {
        if (!xz.containsKey(event.getPlayer().getName())) {
            return;
        }
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        Offset<Double> p = xz.get(event.getPlayer().getName());
        if (Math.abs(p.getX() - curr_x) + Math.abs(p.getZ() - curr_z) > 0.00390625D) {
            return;
        }
        event.getPacket().getDoubles().write(index + 0, p.getX());
        event.getPacket().getDoubles().write(index + 2, p.getZ());

        xz.remove(event.getPlayer().getName());
    }

    public static void removePosition(Player player) {
        xz.remove(player.getName());
    }
}
