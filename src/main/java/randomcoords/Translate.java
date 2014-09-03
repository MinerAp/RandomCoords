package randomcoords;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.ChunkPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import java.util.List;
import org.bukkit.entity.Player;

public class Translate {
    public static void outgoing(PacketEvent event) {
        switch (event.getPacketType().getCurrentId()) {
            case 5:
                sendInt(event, 0);
                break;
            case 7:
                RandomOffset.newOffset(event.getPlayer());
                break;
            case 8:
                PrecisionFix.onServerChangePos(event, 0);
                sendDouble(event, 0);
                break;
            case 10:
                sendInt(event, 1);
                break;
            case 12:
            case 14:
                sendInt32(event, 1);
                break;
            case 15:
                sendInt32(event, 2);
                break;
            case 16:
                sendInt(event, 1);
                break;
            case 17:
            case 24:
                sendInt32(event, 1);
                break;
            case 33:
                sendChunk(event, 0);
                break;
            case 34:
                sendChunkUpdate(event, 0);
                break;
            case 35:
            case 36:
                sendInt(event, 0);
                break;
            case 37:
                sendInt(event, 1);
                break;
            case 38:
                sendChunkBulk(event);
                break;
            case 39:
                sendExplosion(event);
                break;
            case 40:
                sendInt(event, 2);
                break;
            case 41:
                sendInt8(event, 0);
                break;
            case 42:
                sendFloat(event, 1);
                break;
            case 44:
                sendInt32(event, 1);
                break;
            case 51:
                sendInt(event, 0);
                break;
            case 53:
                sendTileEntityData(event);
                break;
            case 54:
                sendInt(event, 0);
                break;
        }
    }

    public static void incoming(PacketEvent event) {
        switch (event.getPacketType().getCurrentId()) {
            case 4:
                recvDouble(event, 0);
                break;
            case 6:
                if (!isSpecialMove(event)) {
                    recvDouble(event, 0);
                    PrecisionFix.onClientChangePos(event, 0);
                }
                break;
            case 7:
                recvInt(event, 0);
                break;
            case 8:
                if (!isSpecialPlace(event)) {
                    recvInt(event, 0);
                }
                break;
            case 18:
                recvInt(event, 0);
                break;
        }
    }

    private static void sendInt(PacketEvent event, int index) {
        Player p = event.getPlayer();
        int curr_x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 0)).intValue();
        int curr_z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 2)).intValue();
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 0, Integer.valueOf(curr_x + RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 2, Integer.valueOf(curr_z + RandomOffset.getZ(p).intValue()));
    }

    private static void sendDouble(PacketEvent event, int index) {
        Player p = event.getPlayer();
        double curr_x = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 0)).doubleValue();
        double curr_z = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 2)).doubleValue();
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 0, Double.valueOf(curr_x + RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 2, Double.valueOf(curr_z + RandomOffset.getZ(p).intValue()));
    }

    private static void sendFloat(PacketEvent event, int index) {
        Player p = event.getPlayer();
        float curr_x = ((Float) event.getPacket().getSpecificModifier(Float.TYPE).read(index + 0)).floatValue();
        float curr_z = ((Float) event.getPacket().getSpecificModifier(Float.TYPE).read(index + 2)).floatValue();
        event.getPacket().getSpecificModifier(Float.TYPE).write(index + 0, Float.valueOf(curr_x + RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Float.TYPE).write(index + 2, Float.valueOf(curr_z + RandomOffset.getZ(p).intValue()));
    }

    private static void sendChunk(PacketEvent event, int index) {
        Player p = event.getPlayer();

        int curr_x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 0)).intValue();
        int curr_z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 1)).intValue();
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 0, Integer.valueOf(curr_x + RandomOffset.getX(p).intValue() / 16));
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 1, Integer.valueOf(curr_z + RandomOffset.getZ(p).intValue() / 16));
    }

    private static void sendChunkUpdate(PacketEvent event, int index) {
        Player p = event.getPlayer();

        ChunkCoordIntPair curr_pos = (ChunkCoordIntPair) event.getPacket().getChunkCoordIntPairs().read(0);
        int curr_x = curr_pos.getChunkX();
        int curr_z = curr_pos.getChunkZ();

        ChunkCoordIntPair new_pos = new ChunkCoordIntPair(curr_x + RandomOffset.getX(p).intValue() / 16, curr_z + RandomOffset.getZ(p).intValue() / 16);
        event.getPacket().getChunkCoordIntPairs().write(0, new_pos);
    }

    private static void sendChunkBulk(PacketEvent event) {
        Player p = event.getPlayer();

        int[] curr_x = (int[]) ((int[]) event.getPacket().getSpecificModifier(int[].class).read(0)).clone();
        int[] curr_z = (int[]) ((int[]) event.getPacket().getSpecificModifier(int[].class).read(1)).clone();
        for (int c = 0; c < curr_x.length; c++) {
            curr_x[c] += RandomOffset.getX(p).intValue() / 16;
            curr_z[c] += RandomOffset.getZ(p).intValue() / 16;
        }
        event.getPacket().getSpecificModifier(int[].class).write(0, curr_x);
        event.getPacket().getSpecificModifier(int[].class).write(1, curr_z);
    }

    private static void sendInt8(PacketEvent event, int index) {
        Player p = event.getPlayer();
        int curr_x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 0)).intValue();
        int curr_z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 2)).intValue();
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 0, Integer.valueOf(curr_x + 8 * RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 2, Integer.valueOf(curr_z + 8 * RandomOffset.getZ(p).intValue()));
    }

    private static void sendInt32(PacketEvent event, int index) {
        Player p = event.getPlayer();
        int curr_x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 0)).intValue();
        int curr_z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 2)).intValue();
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 0, Integer.valueOf(curr_x + 32 * RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 2, Integer.valueOf(curr_z + 32 * RandomOffset.getZ(p).intValue()));
    }

    private static void sendExplosion(PacketEvent event) {
        sendDouble(event, 0);

        List<ChunkPosition> lst = (List<ChunkPosition>) event.getPacket().getPositionCollectionModifier().read(0);
        for (int i = 0; i < lst.size(); i++) {
            ChunkPosition curr = (ChunkPosition) lst.get(i);
            ChunkPosition next = new ChunkPosition(
                                                   curr.getX() + RandomOffset.getX(event.getPlayer()).intValue(),
                                                   curr.getY(),
                                                   curr.getZ() + RandomOffset.getZ(event.getPlayer()).intValue());

            lst.set(i, next);
        }
        event.getPacket().getPositionCollectionModifier().write(0, lst);
    }

    private static void sendTileEntityData(PacketEvent event) {
        sendInt(event, 0);

        Player p = event.getPlayer();
        NbtCompound nbt = (NbtCompound) event.getPacket().getNbtModifier().read(0);
        nbt.put("x", nbt.getInteger("x") + RandomOffset.getX(p).intValue());
        nbt.put("z", nbt.getInteger("z") + RandomOffset.getZ(p).intValue());
    }

    private static void recvInt(PacketEvent event, int index) {
        Player p = event.getPlayer();
        int curr_x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 0)).intValue();
        int curr_z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(index + 2)).intValue();
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 0, Integer.valueOf(curr_x - RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Integer.TYPE).write(index + 2, Integer.valueOf(curr_z - RandomOffset.getZ(p).intValue()));
    }

    private static void recvDouble(PacketEvent event, int index) {
        Player p = event.getPlayer();
        double curr_x = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 0)).doubleValue();
        double curr_z = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(index + 2)).doubleValue();
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 0, Double.valueOf(curr_x - RandomOffset.getX(p).intValue()));
        event.getPacket().getSpecificModifier(Double.TYPE).write(index + 2, Double.valueOf(curr_z - RandomOffset.getZ(p).intValue()));
    }

    private static boolean isSpecialMove(PacketEvent event) {
        double y = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(1)).doubleValue();
        double s = ((Double) event.getPacket().getSpecificModifier(Double.TYPE).read(3)).doubleValue();
        if ((y == -999.0D) && (s == -999.0D)) {
            return true;
        }
        return false;
    }

    private static boolean isSpecialPlace(PacketEvent event) {
        int x = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(0)).intValue();
        int y = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(1)).intValue();
        int z = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(2)).intValue();
        int d = ((Integer) event.getPacket().getSpecificModifier(Integer.TYPE).read(3)).intValue();
        if ((x == -1) && (y == 255) && (z == -1) && (d == 255)) {
            return true;
        }
        return false;
    }
}
