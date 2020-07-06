package world.events.event;

import world.Chunk;
import world.events.Event;

public class ChunkGeneratedEvent extends Event {

    private Chunk chunk;

    public ChunkGeneratedEvent(Chunk c) {
        this.chunk = c;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
