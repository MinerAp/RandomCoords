package randomcoords;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.comphenix.protocol.PacketType;

public enum InboundPacket {

    UNKNOWN,
    POSITION(PacketType.Play.Client.POSITION),
    POSITION_LOOK(PacketType.Play.Client.POSITION_LOOK),
    BLOCK_DIG(PacketType.Play.Client.BLOCK_DIG),
    BLOCK_PLACE(PacketType.Play.Client.BLOCK_PLACE),
    UPDATE_SIGN(PacketType.Play.Client.UPDATE_SIGN),
    ;

    private static final Map<PacketType, InboundPacket> m = new HashMap<PacketType, InboundPacket>();
    private final PacketType type;

    static {
        for (InboundPacket packet : values()) {
            if (packet.type != null) {
                m.put(packet.type, packet);
            }
        }
    }

    InboundPacket() {
        this.type = null;
    }

    InboundPacket(PacketType type) {
        this.type = type;
    }

    public static Set<PacketType> getTypes() {
        return m.keySet();
    }

    public static InboundPacket getPacketType(PacketType type) {
        return m.containsKey(type) ? m.get(type) : UNKNOWN;
    }
}
