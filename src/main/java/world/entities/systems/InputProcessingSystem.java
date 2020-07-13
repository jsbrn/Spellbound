package world.entities.systems;

import com.github.mathiewz.slick.Input;
import misc.MiscMath;
import world.World;
import world.entities.components.InputComponent;
import world.entities.components.VelocityComponent;

import java.util.Set;

public class InputProcessingSystem {

    public static void update(World world) {
        Set<Integer> inputs = world.getEntities().getEntitiesWith(InputComponent.class, VelocityComponent.class);

        for (Integer entity: inputs) {
            InputComponent ic = (InputComponent)world.getEntities().getComponent(InputComponent.class, entity);
            VelocityComponent vc = (VelocityComponent)world.getEntities().getComponent(VelocityComponent.class, entity);

            double dx = 0, dy = 0;
            if (ic.getKey(Input.KEY_W)) dy--;
            if (ic.getKey(Input.KEY_A)) dx--;
            if (ic.getKey(Input.KEY_S)) dy++;
            if (ic.getKey(Input.KEY_D)) dx++;

            if (dx != 0 || dy != 0)
                vc.setConstant(MiscMath.angleBetween(0, 0, dx, dy), vc.getBaseSpeed());
            else
                vc.setConstant(0, 0);

        }

    }

}
