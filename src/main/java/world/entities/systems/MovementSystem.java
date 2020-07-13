package world.entities.systems;

import misc.Location;
import misc.MiscMath;
import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import world.Chunk;
import world.Region;
import world.World;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.PlayerComponent;
import world.entities.components.VelocityComponent;
import world.events.event.EntityNearPlayerEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        }

        for (Integer entity : entitiesToMove) {
            VelocityComponent vc = (VelocityComponent) world.getEntities().getComponent(VelocityComponent.class, entity);
            LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
            //HitboxComponent hc = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, entity);
            //TODO: if HC is null ignore collision
            //also TODO: implement collision
            double[] dir = vc.calculateVector(world.getCurrentTime());
            double[] newPos = new double[]{lc.getLocation().getCoordinates()[0] + dir[0], lc.getLocation().getCoordinates()[1] + dir[1]};
            dir[0] = MiscMath.getConstant(dir[0], 1);
            dir[1] = MiscMath.getConstant(dir[1], 1);
            lc.getLocation().addCoordinates(dir[0], dir[1]);
        }


    }

    @ServerExecution
    public static void pollForPlayerApproaches(World world) {
        Set<Integer> players = world.getEntities().getEntitiesWith(PlayerComponent.class);
        for (Integer player: players) {
            LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, player);
            Region reg = world.getRegion(lc.getLocation().getRegionName());
            ArrayList<Integer> entitiesWithinRange = reg.getEntitiesNear(player, 1);
            for (Integer entity: entitiesWithinRange) {
                LocationComponent eLoc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
                boolean hasApproached = eLoc.hasApproachedPlayer(player);
                if (hasApproached) MPServer.getEventManager().invoke(new EntityNearPlayerEvent(entity, player));
            }
        }
    }

}
