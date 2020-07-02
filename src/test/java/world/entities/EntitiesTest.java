package world.entities;

import assets.Assets;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import world.World;
import world.entities.components.Component;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntitiesTest {

    @BeforeAll
    public void setup() {
        System.out.println("Creating world...");
        World.init();
        World.generate(0);
        Entities.removeAll();
    }

    @AfterAll
    public void tearDown() {
        System.out.println("Removing all entities...");
        Entities.removeAll();
    }

    @Test
    @Order(1)
    public void createsEntity() {
        Integer newID = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        assertEquals(1, newID);
    }

    @Test
    @Order(2)
    public void entityExists() {
        Component c = Entities.getComponent(LocationComponent.class, 1);
        assertNotNull(c);
    }

    @Test
    @Order(3)
    public void getAllEntitiesWorks() {
        Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class).size());
        assertEquals(2, Entities.getEntitiesWith(VelocityComponent.class).size());
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class).size());
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class, HitboxComponent.class).size());
    }

    @Test
    @Order(4)
    public void nonSymmetricalGetAllEntitiesWorks() {
        Entities.removeComponent(VelocityComponent.class, 2);
        assertEquals(2, Entities.getEntitiesWith(LocationComponent.class).size());
        assertEquals(1, Entities.getEntitiesWith(VelocityComponent.class).size());
        assertEquals(1, Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class).size());
    }

    @Test
    @Order(5)
    public void entityRemoves() {
        Entities.removeEntity(1);
        assertNull(Entities.getComponent(LocationComponent.class, 1));
        assertNull(Entities.getComponent(VelocityComponent.class, 1));
        assertNotNull(Entities.getComponent(LocationComponent.class, 2));
    }

    @Test
    @Order(6)
    public void isRegionSpecific() {
        World.getRegion("player_home").addEntity(2);
        assertEquals(1, Entities.getEntitiesWith(World.getRegion("player_home").getEntities(), LocationComponent.class).size());
        assertEquals(0, Entities.getEntitiesWith(World.getRegion("world").getEntities(), LocationComponent.class).size());
    }

}