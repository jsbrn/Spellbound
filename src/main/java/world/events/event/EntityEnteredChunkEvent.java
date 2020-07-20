package world.events.event;

import world.Chunk;
import world.events.Event;

public class EntityEnteredChunkEvent extends Event {

    private int entityID;
    private Chunk chunk;

    public EntityEnteredChunkEvent(int entityID, Chunk chunk) {
        this.entityID = entityID;
        this.chunk = chunk;
    }

    public int getEntityID() {
        return entityID;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
