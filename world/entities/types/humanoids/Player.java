package world.entities.types.humanoids;

import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.Region;
import world.World;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.CastSpellAction;
import world.entities.actions.action.MoveAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MouseReleaseEvent;

public class Player extends HumanoidEntity {

    public Player() {

        super();

        this.setMaxHP(10);
        this.setMaxMana(10);
        this.setHP(10);
        this.setMana(5);
        this.setMaxStamina(10);

        this.addAnimation("torso", "default", new Animation("humanoid/torso_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("head", "default", new Animation("humanoid/head_idle.png", 2, 1, 16, true, true, Color.white));
        this.addAnimation("legs", "default", new Animation("humanoid/legs_idle.png", 2, 1, 16, true, true, Color.orange));
        this.addAnimation("arms", "default", new Animation("humanoid/arms_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "walking", new Animation("humanoid/legs_walking.png", 2, 4, 16, true, true, Color.orange));
        this.addAnimation("arms", "walking", new Animation("humanoid/arms_walking.png", 2, 4, 16, true, true, Color.red));
        this.addAnimation("arms", "casting", new Animation("humanoid/arms_idle.png", 10, 1, 16, false, true, Color.red));

        Spell testSpell = new Spell();
        testSpell.addTechnique(Technique.create(TechniqueName.PROPEL));
        testSpell.addTechnique(Technique.create(TechniqueName.RADIATE));
        this.getSpellbook().addSpell(testSpell);

        Player that = this;
        EventDispatcher.register(new EventListener()
                .on(MouseReleaseEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        MouseReleaseEvent mce = (MouseReleaseEvent)e;
                        ActionGroup actions = new ActionGroup();
                        if (getSpellbook().getParent().getMana() >= 1 && mce.getButton() == 0) {
                            actions.add(new SetAnimationAction("arms", "casting", true));
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new SetAnimationAction("arms", "default", false));
                            getSpellbook().getParent().queueActions(actions);
                        }
                        if (mce.getButton() == 1) {
//                            getLocation().setLookDirection((int)MiscMath.angleBetween(
//                                    getLocation().getCoordinates()[0] + 0.5,
//                                    getLocation().getCoordinates()[1] + 0.5,
//                                    mce.getX(),
//                                    mce.getY()));
                            that.queueAction(new MoveAction(mce.getX() - 0.5, mce.getY() - 0.5));
                        }
                    }
                })
                .on(KeyUpEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        int key = ((KeyUpEvent)e).getKey();
                        if (key == Input.KEY_W || key == Input.KEY_A || key == Input.KEY_S || key == Input.KEY_D)
                            that.queueAction(new SetAnimationAction("arms", "default", false));
                            that.queueAction(new SetAnimationAction("legs", "default", false));
                    }
                })
        );

    }

    public void update() {
        super.update();

        if (GameScreen.debugModeEnabled()) setMoveSpeed(6.5f); else setMoveSpeed(3);

        int dx = 0, dy = 0;
        if (GameScreen.getInput().isKeyDown(Input.KEY_W)) {
            dy = -1;
        } else if (GameScreen.getInput().isKeyDown(Input.KEY_A)) {
            dx = -1;
        } else if (GameScreen.getInput().isKeyDown(Input.KEY_S)) {
            dy = 1;
        } else if (GameScreen.getInput().isKeyDown(Input.KEY_D)) {
            dx = 1;
        }

        if ((dx != 0 || dy != 0) && World.getPlayer().getActionQueue().isEmpty()) {
            World.getPlayer().queueAction(new SetAnimationAction("arms", "walking", false));
            World.getPlayer().queueAction(new SetAnimationAction("legs", "walking", false));
            World.getPlayer().move(dx, dy);
        }

    }

}
