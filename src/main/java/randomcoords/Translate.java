package randomcoords;

import java.util.List;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.ChunkPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class Translate {
    public static void outgoing(PacketEvent event) {
        switch (event.getPacketType().getCurrentId()) {
            case 5:
                sendInt(event, 0);
                break;
            case 7:
                RandomOffset.generateOffset(event.getPlayer());
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
        int curr_x = event.getPacket().getIntegers().read(index + 0);
        int curr_z = event.getPacket().getIntegers().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getIntegers().write(index + 0, curr_x + offset.getX());
        event.getPacket().getIntegers().write(index + 2, curr_z + offset.getZ());
    }

    private static void sendDouble(PacketEvent event, int index) {
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getDoubles().write(index + 0, curr_x + offset.getX());
        event.getPacket().getDoubles().write(index + 2, curr_z + offset.getZ());
    }

    private static void sendFloat(PacketEvent event, int index) {
        float curr_x = event.getPacket().getFloat().read(index + 0);
        float curr_z = event.getPacket().getFloat().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getFloat().write(index + 0, curr_x + offset.getX());
        event.getPacket().getFloat().write(index + 2, curr_z + offset.getZ());
    }

    private static void sendChunk(PacketEvent event, int index) {
        int curr_x = event.getPacket().getIntegers().read(index + 0);
        int curr_z = event.getPacket().getIntegers().read(index + 1);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getIntegers().write(index + 0, curr_x + offset.getX() / 16);
        event.getPacket().getIntegers().write(index + 1, curr_z + offset.getZ() / 16);
    }

    private static void sendChunkUpdate(PacketEvent event, int index) {
        ChunkCoordIntPair curr_pos = (ChunkCoordIntPair) event.getPacket().getChunkCoordIntPairs().read(0);
        int curr_x = curr_pos.getChunkX();
        int curr_z = curr_pos.getChunkZ();

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        ChunkCoordIntPair new_pos = new ChunkCoordIntPair(curr_x + offset.getX() / 16, curr_z + offset.getZ() / 16);
        event.getPacket().getChunkCoordIntPairs().write(0, new_pos);
    }

    private static void sendChunkBulk(PacketEvent event) {
        int[] curr_x = (int[]) event.getPacket().getIntegerArrays().read(0).clone();
        int[] curr_z = (int[]) event.getPacket().getIntegerArrays().read(1).clone();

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        for (int c = 0; c < curr_x.length; c++) {
            curr_x[c] += offset.getX() / 16;
            curr_z[c] += offset.getZ() / 16;
        }
        event.getPacket().getIntegerArrays().write(0, curr_x);
        event.getPacket().getIntegerArrays().write(1, curr_z);
    }

    private static void sendInt8(PacketEvent event, int index) {
        int curr_x = event.getPacket().getIntegers().read(index + 0);
        int curr_z = event.getPacket().getIntegers().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getIntegers().write(index + 0, curr_x + 8 * offset.getX());
        event.getPacket().getIntegers().write(index + 2, curr_z + 8 * offset.getZ());
    }

    private static void sendInt32(PacketEvent event, int index) {
        int curr_x = event.getPacket().getIntegers().read(index + 0);
        int curr_z = event.getPacket().getIntegers().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getIntegers().write(index + 0, curr_x + 32 * offset.getX());
        event.getPacket().getIntegers().write(index + 2, curr_z + 32 * offset.getZ());
    }

    private static void sendExplosion(PacketEvent event) {
        sendDouble(event, 0);
        List<ChunkPosition> lst = (List<ChunkPosition>) event.getPacket().getPositionCollectionModifier().read(0);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        for (int i = 0; i < lst.size(); i++) {
            ChunkPosition curr = (ChunkPosition) lst.get(i);
            ChunkPosition next = new ChunkPosition(
                                                   curr.getX() + offset.getX(),
                                                   curr.getY(),
                                                   curr.getZ() + offset.getZ());

            lst.set(i, next);
        }
        event.getPacket().getPositionCollectionModifier().write(0, lst);
    }

    private static void sendTileEntityData(PacketEvent event) {
        sendInt(event, 0);
        NbtCompound nbt = (NbtCompound) event.getPacket().getNbtModifier().read(0);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        nbt.put("x", nbt.getInteger("x") + offset.getX());
        nbt.put("z", nbt.getInteger("z") + offset.getZ());
    }

    private static void recvInt(PacketEvent event, int index) {
        int curr_x = event.getPacket().getIntegers().read(index + 0);
        int curr_z = event.getPacket().getIntegers().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getIntegers().write(index + 0, curr_x - offset.getX());
        event.getPacket().getIntegers().write(index + 2, curr_z - offset.getZ());
    }

    private static void recvDouble(PacketEvent event, int index) {
        double curr_x = event.getPacket().getDoubles().read(index + 0);
        double curr_z = event.getPacket().getDoubles().read(index + 2);

        Offset<Integer> offset = RandomOffset.getOffset(event.getPlayer());
        event.getPacket().getDoubles().write(index + 0, curr_x - offset.getX());
        event.getPacket().getDoubles().write(index + 2, curr_z - offset.getZ());
    }

    private static boolean isSpecialMove(PacketEvent event) {
        double y = event.getPacket().getDoubles().read(1);
        double s = event.getPacket().getDoubles().read(3);

        return (y == -999.0D) && (s == -999.0D);
    }

    private static boolean isSpecialPlace(PacketEvent event) {
        int x = event.getPacket().getIntegers().read(0);
        int y = event.getPacket().getIntegers().read(1);
        int z = event.getPacket().getIntegers().read(2);
        int d = event.getPacket().getIntegers().read(3);

        return (x == -1) && (y == 255) && (z == -1) && (d == 255);
    }
}
