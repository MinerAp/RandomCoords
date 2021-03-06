package randomcoords;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        final ProtocolManager pm = ProtocolLibrary.getProtocolManager();

        PacketAdapter.AdapterParameteters paramsServer = PacketAdapter.params();
        paramsServer.plugin(this);
        paramsServer.connectionSide(ConnectionSide.SERVER_SIDE);
        paramsServer.listenerPriority(ListenerPriority.HIGHEST);
        paramsServer.gamePhase(GamePhase.BOTH);
        paramsServer.types(OutboundPacket.getTypes());

        PacketAdapter.AdapterParameteters paramsClient = PacketAdapter.params();
        paramsClient.plugin(this);
        paramsClient.connectionSide(ConnectionSide.CLIENT_SIDE);
        paramsClient.listenerPriority(ListenerPriority.HIGHEST);
        paramsClient.gamePhase(GamePhase.BOTH);
        paramsClient.types(InboundPacket.getTypes());

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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        RandomOffset.removeOffset(event.getPlayer());
        PrecisionFix.removePosition(event.getPlayer());
    }
}
