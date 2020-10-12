package world.entities.systems;

import com.github.mathiewz.slick.Input;
import misc.MiscMath;
import network.MPClient;
import world.Camera;
import world.World;
import world.entities.components.AnimatorComponent;
import world.entities.components.InputComponent;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

import java.util.Set;

public class InputProcessingSystem {

    public static void update(World world) {
        Set<Integer> inputs = world.getEntities().getEntitiesWith(InputComponent.class, LocationComponent.class, VelocityComponent.class);

        for (Integer entity: inputs) {
            InputComponent ic = (InputComponent)world.getEntities().getComponent(InputComponent.class, entity);
            VelocityComponent vc = (VelocityComponent)world.getEntities().getComponent(VelocityComponent.class, entity);
            LocationComponent lc = (LocationComponent) world.getEntities().getComponent(LocationComponent.class, entity);
            AnimatorComponent ac = (AnimatorComponent) world.getEntities().getComponent(AnimatorComponent.class, entity);

            double dx = 0, dy = 0;
            if (ic.getKey(Input.KEY_W)) dy--;
            if (ic.getKey(Input.KEY_A)) dx--;
            if (ic.getKey(Input.KEY_S)) dy++;
            if (ic.getKey(Input.KEY_D)) dx++;

            if (dx != 0 || dy != 0) {
                if (ac != null) ac.addContext("walking");
                double angle = MiscMath.angleBetween(0, 0, dx, dy);
                lc.getLocation().setLookDirection((int)angle);
                vc.setConstant(angle, vc.getBaseSpeed());
            } else {
                if (ac != null) ac.removeContext("walking");
                vc.setConstant(0, 0);
            }

        }

    }

    public static void updateLocalPlayer() {
        InputComponent ic = (InputComponent) MPClient.getWorld().getEntities().getComponent(InputComponent.class, Camera.getTargetEntity());
        VelocityComponent vc = (VelocityComponent)MPClient.getWorld().getEntities().getComponent(VelocityComponent.class, Camera.getTargetEntity());

        double dx = 0, dy = 0;
        if (ic.getKey(Input.KEY_W)) dy--;
        if (ic.getKey(Input.KEY_A)) dx--;
        if (ic.getKey(Input.KEY_S)) dy++;
        if (ic.getKey(Input.KEY_D)) dx++;

        if (dx != 0 || dy != 0)
            vc.setClientConstant(MiscMath.angleBetween(0, 0, dx, dy), vc.getBaseSpeed());
        else
            vc.setClientConstant(0, 0);

    }

}
