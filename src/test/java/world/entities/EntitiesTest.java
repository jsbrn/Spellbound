package world.entities;

import assets.Assets;
import org.junit.Before;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import world.World;
import world.entities.components.Component;
import world.entities.components.LocationComponent;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntitiesTest {

    @BeforeAll
    static void createWorld() {
        World.init(false);
        World.generate(0);
    }

    @Test
    @Order(1)
    void createsEntity() {
        Integer newID = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        assertEquals(1, newID);
    }

    @Test
    @Order(2)
    void entityExists() {
        Component c = Entities.getComponent(LocationComponent.class, 1);
        assertNotNull(c);
    }

    @Test
    @Order(3)
    void entityRemoves() {
        Entities.removeEntity(1);
        Component c = Entities.getComponent(LocationComponent.class, 1);
        assertNull(c);
    }

}