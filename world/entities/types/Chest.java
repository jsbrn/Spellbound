package world.entities.types;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.actions.action.ChangeAnimationAction;
import world.entities.animations.Animation;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;

public class Chest extends Entity {

    private boolean looted;

    public Chest(int lootMultiplier) {

        this.getMover().setCollidable(true);
        this.addAnimation("default", "closed", new Animation("chest.png", 1, 1, 16, true, false, Color.white));
        this.addAnimation("default", "opening", new Animation("chest_opening.png", 3, 3, 16, false, false, Color.white));
        this.addAnimation("default", "opened", new Animation("chest_opened.png", 1, 1, 16, true, false, Color.white));
        this.getAnimationLayer("default").setBaseAnimation("closed");

        Chest that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    if (looted) return;
                    EntityActivatedEvent eae = (EntityActivatedEvent)e;
                    if (eae.getEntity().equals(that)) {
                        if (eae.getActivatedBy() instanceof HumanoidEntity) {
                            that.getAnimationLayer("default").setBaseAnimation("opened");
                            that.queueAction(new ChangeAnimationAction("default", "opening", false, true));
                            HumanoidEntity human = (HumanoidEntity)eae.getActivatedBy();
                            human.addGold((int)MiscMath.random(0, lootMultiplier * 10));
                            human.addCrystals((int)MiscMath.random(0, lootMultiplier * 4));
                            human.addDyes((int)MiscMath.random(0, lootMultiplier * 2));
                            looted = true;
                        }
                    }
                }
            }
        ));

    }

}
