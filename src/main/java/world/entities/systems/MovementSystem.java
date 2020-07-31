package world.entities.systems;

import misc.Location;
import misc.MiscMath;
import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import world.Chunk;
import world.Region;
import world.Tiles;
import world.World;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.PlayerComponent;
import world.entities.components.VelocityComponent;
import world.events.event.EntityEnteredChunkEvent;

import java.util.*;
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
            moveEntity(entity, world, false, false);
        }


    }

    @ServerClientExecution
    public static void moveEntity(int entity, World world, boolean constantsOnly, boolean backwards) {
        VelocityComponent vc = (VelocityComponent) world.getEntities().getComponent(VelocityComponent.class, entity);
        LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
        HitboxComponent hc = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, entity);

        Location old = new Location(lc.getLocation());

        double[] dir = vc.calculateVector(constantsOnly, backwards);
        dir[0] = MiscMath.getConstant(dir[0], 1);
        dir[1] = MiscMath.getConstant(dir[1], 1);
        double[] newPos = applyCollisionRules(lc, hc, dir, world);

        lc.getLocation().setCoordinates(
                MiscMath.clamp(old.getCoordinates()[0] + dir[0], old.getCoordinates()[0], newPos[0]),
                MiscMath.clamp(old.getCoordinates()[1] + dir[1], old.getCoordinates()[1], newPos[1]));

        //sort in region entity list
//        if (oldPos[0] != newPosInt[0] || oldPos[1] != newPosInt[1]) {
//            Region reg = world.getRegion(lc.getLocation().getRegionName());
//            //re sort position in region (TODO maybe add a dedicated method for this?)
//            reg.removeEntity(entity);
//            reg.addEntity(entity);
//        }
    }

    @ServerExecution
    public static void backtrack(int entity, World world) {
        moveEntity(entity, world, true, true);
    }

    private static double[] applyCollisionRules(LocationComponent lc, HitboxComponent hc, double[] dir, World world) {
        int increments = (int)MiscMath.clamp(dir[0]/(1f/Chunk.TILE_SIZE), 1, Chunk.TILE_SIZE);

        double[] new_ = new double[]{lc.getLocation().getCoordinates()[0] + dir[0], lc.getLocation().getCoordinates()[1] + dir[1]};

        for (int a = 0; a < 2 && hc != null; a++) {
            Location l = new Location(lc.getLocation());
            for (int i = 0; i < increments; i++) {
                double toAdd = dir[a] == 0 ? 0 : ((1/16f) * (dir[a] > 0 ? 1 : -1));
                l.addCoordinates(a == 0 ? toAdd : 0, a == 1 ? toAdd: 0);
                if (collides(l, hc, world)) {
                    new_[a] = l.getCoordinates()[a];
                    break;
                }
            }
        }

        return new_;

    }

    private static boolean collides(Location location, HitboxComponent hc, World world) {
        List<Integer> entities = world
                .getRegion(location).getEntities(location.getCoordinates()[0], location.getCoordinates()[1], hc.getRadius())
                .stream().filter(e -> e.intValue() != hc.getParent())
                .collect(Collectors.toList());
        byte[] tile = world.getRegion(location).getTile((int)location.getCoordinates()[0], (int)location.getCoordinates()[1]);
        return !entities.isEmpty() || Tiles.collides(tile[1]) || Tiles.collides(tile[0]);
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
