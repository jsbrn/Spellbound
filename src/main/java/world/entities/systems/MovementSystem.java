package world.entities.systems;

import world.entities.Entities;
import world.entities.components.Component;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

import java.util.Collection;
import java.util.Map.*;
import java.util.Set;
import java.util.stream.Collectors;

public class MovementSystem implements System {

    @Override
    public void update() {
        Entities.getEntitiesWith(LocationComponent.class, VelocityComponent.class, HitboxComponent.class);
    }

}
