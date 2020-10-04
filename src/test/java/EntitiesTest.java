import assets.Assets;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.entities.Entities;
import world.entities.components.InputComponent;
import world.entities.components.LocationComponent;

import java.util.ArrayList;
import java.util.Set;

public class EntitiesTest {

    private static JSONObject testEntityData;
    private static Entities entities = new Entities();
    private static int randomID;
    private static ArrayList<Integer> validSearchFilter = new ArrayList(), invalidSearchFilter = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All");
        randomID = (int)MiscMath.random(0, 1000);
        validSearchFilter.add(randomID);
        invalidSearchFilter.add(1001);
        testEntityData = Assets.json("definitions/entities/test/testEntity.json", true);
        entities.putEntity(randomID, testEntityData);
    }

    @BeforeEach
    void setUp() {
        System.out.println("Before Each");
    }

    @Test
    void testCreation() {
        Assertions.assertNotNull(entities);
    }

    @Test
    void testInsertion() {
        Assertions.assertTrue(entities.exists(randomID));
        Assertions.assertFalse(entities.exists(1001));
        Assertions.assertEquals(1, entities.count());
    }

    @Test
    void testFindByComponent() {
        Set<Integer> found = entities.getEntitiesWith(LocationComponent.class);
        Assertions.assertEquals(1, entities.count());
        Assertions.assertEquals(1, found.size());
    }

    @Test
    void testFindNonExistentByComponent() {
        Set<Integer> found = entities.getEntitiesWith(InputComponent.class);
        Assertions.assertEquals(1, entities.count());
        Assertions.assertEquals(0, found.size());
    }

    @Test
    void testFindByComponentFromSet() {
        Set<Integer> found = entities.getEntitiesWith(validSearchFilter, LocationComponent.class);
        Assertions.assertEquals(1, entities.count());
        Assertions.assertEquals(1, found.size());
    }

    @Test
    void testFindNonExistentByComponentFromSet() {
        Set<Integer> found = entities.getEntitiesWith(invalidSearchFilter, InputComponent.class);
        Assertions.assertEquals(1, entities.count());
        Assertions.assertEquals(0, found.size());
    }

}
