package randomcoords;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.events.PacketEvent;

public class PrecisionFix implements Listener {
    private static final ConcurrentHashMap<String, Double> x = new ConcurrentHashMap<String, Double>();
    private static final ConcurrentHashMap<String, Double> z = new ConcurrentHashMap<String, Double>();

    public static void onServerChangePos(PacketEvent event, int index) {
        double curr_x = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 0)).doubleValue();
        double curr_z = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 2)).doubleValue();

        x.put(event.getPlayer().getName(), Double.valueOf(curr_x));
        z.put(event.getPlayer().getName(), Double.valueOf(curr_z));
    }

    public static void onClientChangePos(PacketEvent event, int index) {
        if (!x.containsKey(event.getPlayer().getName())) {
            return;
        }
        double curr_x = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 0)).doubleValue();
        double curr_z = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 2)).doubleValue();

        double dx = ((Double) x.get(event.getPlayer().getName())).doubleValue() - curr_x;
        double dz = ((Double) z.get(event.getPlayer().getName())).doubleValue() - curr_z;
        if (Math.abs(dx) + Math.abs(dz) > 0.00390625D) {
            return;
        }
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 0, (Double) x.get(event.getPlayer().getName()));
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 2, (Double) z.get(event.getPlayer().getName()));

        x.remove(event.getPlayer().getName());
        z.remove(event.getPlayer().getName());
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
