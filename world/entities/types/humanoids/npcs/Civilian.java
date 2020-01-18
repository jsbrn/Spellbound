package world.entities.types.humanoids.npcs;

import org.newdawn.slick.Color;
import world.entities.states.IdleState;
import world.entities.types.humanoids.HumanoidEntity;

public class Civilian extends HumanoidEntity {

    public Civilian() {
        super();
        getAnimationLayer("torso").setColor(Color.cyan);
        addState("idle", new IdleState());
    }

}
