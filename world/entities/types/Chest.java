package world.entities.types;

import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.animations.Animation;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;

import java.util.Random;

public class Chest extends Entity {

    public static int GOLD_LOOT = 0, CRYSTAL_LOOT = 1, DYE_LOOT = 2, TOME_LOOT = 3, ARTIFACT_LOOT = 4, KEY_LOOT = 5, RANDOM_LOOT = 6;
    private boolean looted;

    public Chest(int lootMultiplier, boolean locked, int lootType, float filledChance) {

        this.getMover().setCollidable(true);
        this.addAnimation("default", "closed", new Animation((locked ? "locked_" : "") + "chest.png", 1, 1, 16, true, false, Color.white));
        this.addAnimation("default", "opening", new Animation("chest_opening.png", 3, 3, 16, false, false, Color.white));
        this.addAnimation("default", "opened", new Animation("chest_opened.png", 1, 1, 16, true, false, Color.white));
        this.getAnimationLayer("default").setBaseAnimation("closed");

        Chest that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    Random rng = new Random();
                    EntityActivatedEvent eae = (EntityActivatedEvent)e;

                    if (looted) return;

                    if (eae.getEntity().equals(that)) {
                        if (eae.getActivatedBy() instanceof HumanoidEntity) {

                            if (locked && ((HumanoidEntity)eae.getActivatedBy()).getKeyCount() < 1) {
                                GameScreen.getGUI().floatText(
                                        eae.getEntity().getLocation(),
                                        "Locked",
                                        Color.gray, -2, 500, -0.5f, false);
                                return;
                            }

                            that.getAnimationLayer("default").setBaseAnimation("opened");
                            that.getActionQueue().queueAction(new ChangeAnimationAction("default", "opening", false, true));
                            HumanoidEntity human = (HumanoidEntity)eae.getActivatedBy();

                            boolean empty = true;
                            String type = "";
                            int amount = rng.nextInt(100) * lootMultiplier;

                            if (rng.nextFloat() < filledChance) {
                                if (lootType == GOLD_LOOT) { amount /= 10; human.addGold(amount); type = "Gold"; }
                                if (lootType == CRYSTAL_LOOT) { amount /= 20; human.addCrystals(amount); type = "Crystals"; }
                                if (lootType == DYE_LOOT) { amount /= 30; human.addDyes(amount); type = "Dyes"; }
                                if (lootType == TOME_LOOT) { amount = 1; human.addTomes(1); type = "Tome"; }
                                if (lootType == ARTIFACT_LOOT) { amount = 1; human.addArtifacts(1); type = "Artifact"; }
                                empty = amount <= 0;
                            }

                            if (empty) {
                                GameScreen.getGUI().floatText(eae.getEntity().getLocation(), "Empty", Color.gray, 2, 500, -0.5f, false);
                            } else {
                                GameScreen.getGUI().floatText(eae.getEntity().getLocation(), "+"+amount+" "+type, Color.yellow, 2, 750, -0.5f, false);
                            }

                            looted = true;
                        }
                    }
                }
            }
        ));

    }

}
