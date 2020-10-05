import assets.Assets;
import misc.Location;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.Chunk;
import world.Region;
import world.World;
import world.entities.components.LocationComponent;
import world.entities.systems.MovementSystem;
import world.events.EventManager;
import world.generation.region.OverworldGenerator;

import java.util.List;

public class GetEntitiesTest {

    private static JSONObject testEntityData;
    private static World world;

    private static LocationComponent testEntityLocation;

    @BeforeAll
    static void beforeAll() {
        testEntityData = Assets.json("definitions/entities/test/testEntity.json", true);
        world = new World();
        world.addRegion(new Region("world", new OverworldGenerator(0)));
        world.spawnEntity(0, testEntityData, null);
        testEntityLocation = (LocationComponent)world.getEntities().getComponent(LocationComponent.class, 0);
        testEntityLocation.getLocation().setCoordinates(MiscMath.random(0, Chunk.CHUNK_SIZE), MiscMath.random(0, Chunk.CHUNK_SIZE));
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void testExistence() {
        Assertions.assertTrue(world.getEntities().exists(0));
    }

    @Test
    void testChunkCacheExistence() {
        Chunk c = world.getRegion(testEntityLocation.getLocation()).getChunk(testEntityLocation.getLocation());
        Assertions.assertNotNull(c);
        Assertions.assertEquals(1, c.getCachedEntities().size());
    }

    @Test
    void testGetAllInRectangle() {
        List<Integer> found = world.getRegion("world").getEntities(0, 0, Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE);
        Assertions.assertEquals(1, found.size());
    }

    @Test
    void testGetChunks() {
        List<Chunk> gotChunks = world.getRegion(testEntityLocation.getLocation()).getChunks(testEntityLocation.getLocation().getChunkCoordinates()[0], testEntityLocation.getLocation().getChunkCoordinates()[1], 1);
        Assertions.assertNotNull(gotChunks);
        Assertions.assertEquals(9, gotChunks.size());
    }

}
