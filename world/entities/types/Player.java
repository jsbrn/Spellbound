package world.entities.types;

import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spellbook;

public class Player extends Entity {

    private Spellbook spellbook;

    public Player() {
        super();
        this.addAnimation("idle", new Animation("player_idle.png", 2, 1));
        this.addAnimation("walking", new Animation("player_walking.png", 4, 3));
        this.addAnimation("casting", new Animation("player_casting.png", 5, 2));
        this.setAnimation("idle");
    }

}
