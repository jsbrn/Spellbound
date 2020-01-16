package world.entities.types.humanoids;

import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Input;
import world.Region;
import world.World;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.CastSpellAction;
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

        this.addAnimation("idle", new Animation("player_idle.png", 2, 1));
        this.addAnimation("walking", new Animation("player_walking.png", 4, 3));
        this.addAnimation("casting", new Animation("player_casting.png", 5, 4));
        this.setAnimation("idle");

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
                        if (getSpellbook().getParent().getMana() >= 1) {
                            actions.add(new SetAnimationAction("casting", true));
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new SetAnimationAction("idle", false));
                            getSpellbook().getParent().queueActions(actions);
                        }
                    }
                })
                .on(KeyUpEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        int key = ((KeyUpEvent)e).getKey();
                        if (key == Input.KEY_W || key == Input.KEY_A || key == Input.KEY_S || key == Input.KEY_D)
                            that.queueAction(new SetAnimationAction("idle", false));
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
            World.getPlayer().queueAction(new SetAnimationAction("walking", false));
            World.getPlayer().move(dx, dy);
        }

    }

}
