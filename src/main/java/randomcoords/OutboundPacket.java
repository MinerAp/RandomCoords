package randomcoords;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.comphenix.protocol.PacketType;

public enum OutboundPacket {

    UNKNOWN,
    SPAWN_POSITION(PacketType.Play.Server.SPAWN_POSITION),
    RESPAWN(PacketType.Play.Server.RESPAWN),
    POSITION(PacketType.Play.Server.POSITION),
    BED(PacketType.Play.Server.BED),
    NAMED_ENTITY_SPAWN(PacketType.Play.Server.NAMED_ENTITY_SPAWN),
    SPAWN_ENTITY(PacketType.Play.Server.SPAWN_ENTITY),
    SPAWN_ENTITY_LIVING(PacketType.Play.Server.SPAWN_ENTITY_LIVING),
    SPAWN_ENTITY_PAINTING(PacketType.Play.Server.SPAWN_ENTITY_PAINTING),
    SPAWN_ENTITY_EXPERIENCE_ORB(PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB),
    ENTITY_TELEPORT(PacketType.Play.Server.ENTITY_TELEPORT),
    MAP_CHUNK(PacketType.Play.Server.MAP_CHUNK),
    MULTI_BLOCK_CHANGE(PacketType.Play.Server.MULTI_BLOCK_CHANGE),
    BLOCK_CHANGE(PacketType.Play.Server.BLOCK_CHANGE),
    BLOCK_ACTION(PacketType.Play.Server.BLOCK_ACTION),
    BLOCK_BREAK_ANIMATION(PacketType.Play.Server.BLOCK_BREAK_ANIMATION),
    MAP_CHUNK_BULK(PacketType.Play.Server.MAP_CHUNK_BULK),
    EXPLOSION(PacketType.Play.Server.EXPLOSION),
    WORLD_EVENT(PacketType.Play.Server.WORLD_EVENT),
    NAMED_SOUND_EFFECT(PacketType.Play.Server.NAMED_SOUND_EFFECT),
    WORLD_PARTICLES(PacketType.Play.Server.WORLD_PARTICLES),
    SPAWN_ENTITY_WEATHER(PacketType.Play.Server.SPAWN_ENTITY_WEATHER),
    UPDATE_SIGN(PacketType.Play.Server.UPDATE_SIGN),
    TILE_ENTITY_DATA(PacketType.Play.Server.TILE_ENTITY_DATA),
    OPEN_SIGN_ENTITY(PacketType.Play.Server.OPEN_SIGN_ENTITY),
    ;

    private static final Map<PacketType, OutboundPacket> m = new HashMap<PacketType, OutboundPacket>();
    private final PacketType type;

    static {
        for (OutboundPacket packet : values()) {
            if (packet.type != null) {
                m.put(packet.type, packet);
            }
        }
    }

    OutboundPacket() {
        this.type = null;
    }

    OutboundPacket(PacketType type) {
        this.type = type;
    }

    public static Set<PacketType> getTypes() {
        return m.keySet();
    }

    public static OutboundPacket getPacketType(PacketType type) {
        return m.containsKey(type) ? m.get(type) : UNKNOWN;
    }
}
