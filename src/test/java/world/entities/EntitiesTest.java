package world.entities;

import assets.Assets;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import world.World;
import world.entities.components.Component;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntitiesTest {

    @BeforeAll
    static void createWorld() {
        World.init();
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
    void getAllEntitiesWorks() {
        Integer newID = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        assertEquals(2, newID);
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class).size());
        assertEquals(2, Entities.getEntitiesWith(VelocityComponent.class).size());
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class).size());
    }

    @Test
    @Order(4)
    void nonSymmetricalGetAllEntitiesWorks() {
        Entities.removeComponent(VelocityComponent.class, 2);
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class).size());
        assertEquals(1, Entities.getEntitiesWith(VelocityComponent.class).size());
        assertEquals(1, Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class).size());
    }

    @Test
    @Order(5)
    void entityRemoves() {
        Entities.removeEntity(1);
        assertNull(Entities.getComponent(LocationComponent.class, 1));
        assertNull(Entities.getComponent(VelocityComponent.class, 1));
    }

}