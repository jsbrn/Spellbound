package world.events.event;

import world.Chunk;
import world.events.Event;

public class EntityEnteredChunkEvent extends Event {

    private Chunk chunk;
    private Integer entityID;

    public EntityEnteredChunkEvent(Integer entityID, Chunk c) {
        this.entityID = entityID;
        this.chunk = c;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
