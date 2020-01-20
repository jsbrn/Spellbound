package world.entities.types.humanoids.npcs;

import org.newdawn.slick.Color;
import world.entities.states.FollowPlayerState;
import world.entities.states.IdleState;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.Random;

public class Civilian extends HumanoidEntity {

    Random rng;

    public Civilian() {
        super();
        rng = new Random();
        Color shirt = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        getAnimationLayer("torso").setColor(shirt);
        getAnimationLayer("arms").setColor(shirt.darker());
        addState("idle", new FollowPlayerState());
    }

}
