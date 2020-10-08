import assets.Assets;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.entities.Entities;
import world.entities.components.*;

import java.util.ArrayList;
import java.util.Set;

public class EntitiesTest {

    private static JSONObject exampleNonPlayer, examplePlayer;
    private static Entities entities = new Entities();
    private static int randomID;
    private static ArrayList<Integer> validSearchFilter = new ArrayList(), invalidSearchFilter = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        randomID = (int)MiscMath.random(0, 999);
        validSearchFilter.add(randomID);
        invalidSearchFilter.add(1001);
        exampleNonPlayer = Assets.json("definitions/entities/test/exampleNonPlayer.json", true);
        examplePlayer = Assets.json("definitions/entities/test/examplePlayer.json", true);
        entities.putEntity(randomID, exampleNonPlayer);
        entities.putEntity(randomID + 1, examplePlayer);
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreation() {
        Assertions.assertNotNull(entities);
    }

    @Test
    void testInsertion() {
        Assertions.assertTrue(entities.exists(randomID));
        Assertions.assertTrue(entities.exists(randomID+1));
        Assertions.assertFalse(entities.exists(1001));
        Assertions.assertEquals(2, entities.count());
    }

    @Test
    void testFindAllByComponent() {
        Set<Integer> found = entities.getEntitiesWith(LocationComponent.class);
        Assertions.assertEquals(2, found.size());
    }

    @Test
    void testFindOnlyOneByComponent() {
        Set<Integer> found = entities.getEntitiesWith(PlayerComponent.class);
        Assertions.assertEquals(1, found.size());
    }

    @Test
    void testFindByComponentFromSet() {
        Set<Integer> found = entities.getEntitiesWith(validSearchFilter, LocationComponent.class);
        Assertions.assertEquals(1, found.size());
    }

    @Test
    void testFindNonExistentByComponentFromSet() {
        Set<Integer> found = entities.getEntitiesWith(invalidSearchFilter, LocationComponent.class);
        Assertions.assertEquals(0, found.size());
    }

    @Test
    void testFindEntityWithMultipleComponents() {
        Set<Integer> found = entities.getEntitiesWith(SpellbookComponent.class, PlayerComponent.class);
        Assertions.assertEquals(1, found.size());
    }

}
