package world.entities.systems;

import misc.MiscMath;
import world.World;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

import java.util.Set;

public class MovementSystem {

    public static void update(World world) {
        //TODO: update all regions with players in them
        Set<Integer> entities = world.getEntities()
                .getEntitiesWith(world.getRegion("world").getEntityIDs(), LocationComponent.class, VelocityComponent.class, HitboxComponent.class);
        for (Integer entity: entities) {
            VelocityComponent vc = (VelocityComponent)world.getEntities().getComponent(VelocityComponent.class, entity);
            LocationComponent lc = (LocationComponent)world.getEntities().getComponent(LocationComponent.class, entity);
            HitboxComponent hc = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, entity);
            double[] dir = vc.calculateVector(world.getCurrentTime());
            double[] newPos = new double[]{lc.getLocation().getCoordinates()[0] + dir[0], lc.getLocation().getCoordinates()[1] + dir[1]};
            dir[0] = MiscMath.getConstant(dir[0], 1);
            dir[1] = MiscMath.getConstant(dir[1], 1);
            lc.getLocation().addCoordinates(dir[0], dir[1]);
        }
    }

}
