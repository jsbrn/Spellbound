package world;

import assets.Assets;
import misc.Location;
import org.junit.Before;
import org.junit.jupiter.api.*;
import world.entities.Entities;
import world.entities.components.LocationComponent;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RegionTest {

    @BeforeAll
    static void setup() {
        World.init();
        World.generate(0);
        Region overworld = World.getRegion("world");
        Integer entity1 = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        Integer entity2 = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        ((LocationComponent)Entities.getComponent(LocationComponent.class, entity1)).setLocation(
                new Location(overworld, 0, 0)
        );
        ((LocationComponent)Entities.getComponent(LocationComponent.class, entity2)).setLocation(
                new Location(overworld, 10, 10)
        );
    }

    @Test
    @Order(1)
    void addEntity() {
        Region overworld = World.getRegion("world");
        int index2 = overworld.addEntity(2);
        int index1 = overworld.addEntity(1);
        assertEquals(0, index2);
        assertEquals(0, index1);
    }

    @Test
    @Order(2)
    void getEntitiesInRectangle() {
        ArrayList<Integer> entities = World.getRegion("world").getEntities(0, 0, 2, 3);
        assertEquals(1, entities.size());
        assertEquals(1, entities.get(0));
    }

    @AfterAll
    static void tearDown() {
        Entities.removeAll();
    }

}