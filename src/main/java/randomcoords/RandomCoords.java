package randomcoords;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
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

public class RandomCoords extends JavaPlugin {
    private static RandomCoords self = null;

    public void onEnable() {
        self = this;

        Bukkit.getPluginManager().registerEvents(new RandomOffset(), this);
        Bukkit.getPluginManager().registerEvents(new PrecisionFix(), this);
        final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        PacketAdapter.AdapterParameteters paramsServer = PacketAdapter.params();
        paramsServer.plugin(this);
        paramsServer.connectionSide(ConnectionSide.SERVER_SIDE);
        paramsServer.listenerPriority(ListenerPriority.HIGHEST);
        paramsServer.gamePhase(GamePhase.BOTH);

        HashSet<Integer> packetTypes = new HashSet<Integer>();
        packetTypes.addAll(Arrays.asList(new Integer[] { Integer.valueOf(5), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(12), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(24), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(44), Integer.valueOf(51), Integer.valueOf(53), Integer.valueOf(54) }));
        HashSet<PacketType> packets = new HashSet<PacketType>();
        for (PacketType t : PacketType.values()) {
            if ((t.isServer()) && (packetTypes.contains(Integer.valueOf(t.getCurrentId())))) {
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
        packetTypes.addAll(Arrays.asList(new Integer[] { Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(18) }));
        for (PacketType t : PacketType.values()) {
            if ((t.isClient()) && (packetTypes.contains(Integer.valueOf(t.getCurrentId())))) {
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
}
