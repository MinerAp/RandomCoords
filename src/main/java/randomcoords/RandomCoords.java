package randomcoords;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.reflect.StructureModifier;

public class RandomCoords extends JavaPlugin implements Listener {
    private static RandomCoords self = null;

    public void onEnable() {
        self = this;

        Bukkit.getPluginManager().registerEvents(this, this);

        final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        PacketAdapter.AdapterParameteters paramsServer = PacketAdapter.params();
        paramsServer.plugin(this);
        paramsServer.connectionSide(ConnectionSide.SERVER_SIDE);
        paramsServer.listenerPriority(ListenerPriority.HIGHEST);
        paramsServer.gamePhase(GamePhase.BOTH);

        HashSet<Integer> packetTypes = new HashSet<Integer>();
        packetTypes.addAll(Arrays.asList(new Integer[] { 5, 7, 8, 10, 12, 14, 15, 16, 17, 24, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 44, 51, 53, 54 }));
        HashSet<PacketType> packets = new HashSet<PacketType>();
        for (PacketType t : PacketType.values()) {
            if ((t.isServer()) && (packetTypes.contains(t.getCurrentId()))) {
                packets.add(t);
            }
        }
        paramsServer.types(packets);

        PacketAdapter.AdapterParameteters paramsClient = PacketAdapter.params();
        paramsClient.plugin(this);
        paramsClient.connectionSide(ConnectionSide.CLIENT_SIDE);
        paramsClient.listenerPriority(ListenerPriority.HIGHEST);
        paramsClient.gamePhase(GamePhase.BOTH);

        packetTypes.clear();
        packets.clear();
        packetTypes.addAll(Arrays.asList(new Integer[] { 4, 6, 7, 8, 18 }));
        for (PacketType t : PacketType.values()) {
            if ((t.isClient()) && (packetTypes.contains(t.getCurrentId()))) {
                packets.add(t);
            }
        }
        paramsClient.types(packets);

        pm.addPacketListener(new PacketAdapter(paramsServer) {
            public void onPacketSending(PacketEvent event) {
                event.setPacket(clone(event.getPacket()));
                Translate.outgoing(event);
            }

            private PacketContainer clone(PacketContainer packet) {
                PacketContainer copy = pm.createPacket(packet.getType());
                StructureModifier<Object> src = packet.getModifier();
                StructureModifier<Object> dest = copy.getModifier();
                for (int i = 0; i < src.size(); i++) {
                    dest.write(i, src.read(i));
                }
                return copy;
            }
        });
        pm.addPacketListener(
          new PacketAdapter(paramsClient) {
              public void onPacketReceiving(PacketEvent event) {
                  try {
                      Translate.incoming(event);
                  }
                  catch (UnsupportedOperationException e) {
                      event.setCancelled(true);
                  }
              }
          });
    }

    public static RandomCoords getInstance() {
        return self;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        RandomOffset.removeOffset(event.getPlayer());
        PrecisionFix.removePosition(event.getPlayer());
    }
}
