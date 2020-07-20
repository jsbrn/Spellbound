package world.entities.systems;

import misc.MiscMath;
import misc.annotations.ClientExecution;
import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import world.Chunk;
import world.Region;
import world.World;
import world.entities.components.LocationComponent;
import world.entities.components.PlayerComponent;
import world.entities.components.VelocityComponent;
import world.events.event.EntityEnteredChunkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MovementSystem {

    @ServerClientExecution
    public static void update(World world, Collection<Integer> players) {

        Set<Integer> entitiesToMove = new HashSet<>();

        for (Integer player: players) {
            LocationComponent plc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, player);
            ArrayList<Integer> nearPlayer = world.getRegion(plc.getLocation().getRegionName()).getEntitiesNear(player, 1);
            Set<Integer> nearWithRequirements = world.getEntities().getEntitiesWith(LocationComponent.class, VelocityComponent.class);
            nearWithRequirements.retainAll(nearPlayer);
            entitiesToMove.addAll(nearWithRequirements);
            entitiesToMove = entitiesToMove.stream().distinct().collect(Collectors.toSet());
        }

        for (Integer entity : entitiesToMove) {
            moveEntity(entity, world);
        }


    }

    @ServerClientExecution
    public static void moveEntity(int entity, World world) {
        VelocityComponent vc = (VelocityComponent) world.getEntities().getComponent(VelocityComponent.class, entity);
        LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
        //HitboxComponent hc = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, entity);
        //TODO: if HC is null ignore collision
        //also TODO: implement collision
        double[] dir = vc.calculateVector();
        double[] oldPos = new double[]{Math.floor(lc.getLocation().getCoordinates()[0]), Math.floor(lc.getLocation().getCoordinates()[1])};
        dir[0] = MiscMath.getConstant(dir[0], 1);
        dir[1] = MiscMath.getConstant(dir[1], 1);
        lc.getLocation().addCoordinates(dir[0], dir[1]);
        double[] newPos = new double[]{Math.floor(lc.getLocation().getCoordinates()[0]), Math.floor(lc.getLocation().getCoordinates()[1])};
        if (oldPos[0] != newPos[0] || oldPos[1] != newPos[1]) {
            Region reg = world.getRegion(lc.getLocation().getRegionName());
            //re sort position in region (TODO maybe add a dedicated method for this?)
            reg.removeEntity(entity);
            reg.addEntity(entity);
        }
    }

    @ServerExecution
    public static void pollForMovementEvents(World world) {
        Set<Integer> players = world.getEntities().getEntitiesWith(PlayerComponent.class);
        for (Integer player: players) {
            LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, player);
            Region reg = world.getRegion(lc.getLocation().getRegionName());
            ArrayList<Integer> entitiesWithinRange = reg.getEntitiesNear(player, 1);
            for (Integer entity: entitiesWithinRange) {
                LocationComponent eLoc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
                Chunk newChunk = eLoc.hasEnteredNewChunk();
                if (newChunk != null) MPServer.getEventManager().invoke(new EntityEnteredChunkEvent(entity, newChunk));
            }
        }
    }

}
