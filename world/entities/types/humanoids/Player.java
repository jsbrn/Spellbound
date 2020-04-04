package world.entities.types.humanoids;

import assets.definitions.Definitions;
import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.Chunk;
import world.World;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.ActivateAction;
import world.entities.actions.action.CastSpellAction;
import world.entities.actions.action.ChangeAnimationAction;
import world.entities.magic.Spell;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.*;

public class Player extends HumanoidEntity {

    private double[] moveTarget;
    private boolean allowUserMovement;

    public Player() {

        super();

        this.allowUserMovement = true;

        this.setMaxMana(10);
        this.setMana(10);

        this.addCrystals(1000);
        this.setMaxMana(1000);
        this.setMana(1000);
        this.addCrystals((int)MiscMath.random(250, 750));

        this.getMover().setIndependent(true);
        this.getMover().setLookTowardsTarget(false);

        Spell kb = new Spell();
        kb.addTechnique("physical_weight");
        kb.addTechnique("movement_directional");
        kb.addTechnique("physical_speed");
        kb.addTechnique("physical_collision");
        getSpellbook().addSpell(kb);

        this.getAnimationLayer("head").setColor(SKIN_COLORS[0]);

        Player that = this;
        EventDispatcher.register(new EventListener()
                .on(MousePressedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        MousePressedEvent mce = (MousePressedEvent)e;
                        ActionGroup actions = new ActionGroup();
                        if (getSpellbook().getParent().getMana() >= 1 && mce.getButton() == 0
                            && that.getActionQueue().isEmpty()) {
                            getLocation().lookAt(mce.getX(), mce.getY());
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new ChangeAnimationAction("arms", "casting", true, true));
                            getSpellbook().getParent().queueActions(actions);
                        }
                    }
                })
                .on(KeyDownEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        KeyDownEvent kde = (KeyDownEvent)e;
                        if (kde.getKey() == Input.KEY_E && that.getActionQueue().isEmpty()) {
                            that.queueAction(new ActivateAction());
                        }
                    }
                })
                .on(ConversationStartedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        ConversationStartedEvent cse = (ConversationStartedEvent)e;
                        if (cse.getPlayer().equals(that)) {
                            that.allowUserMovement = false;
                            that.getLocation().lookAt(cse.getNPC().getLocation());
                        }
                    }
                })
                .on(ConversationEndedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        ConversationEndedEvent cse = (ConversationEndedEvent)e;
                        if (cse.getPlayer().equals(that)) {
                            that.allowUserMovement = true;
                        }
                    }
                })
        );

    }

    public void update() {
        super.update();

        int dx = 0, dy = 0;

        if (GameScreen.getInput().isKeyDown(Input.KEY_W)) dy -= 1;
        if (GameScreen.getInput().isKeyDown(Input.KEY_A)) dx -= 1;
        if (GameScreen.getInput().isKeyDown(Input.KEY_S)) dy += 1;
        if (GameScreen.getInput().isKeyDown(Input.KEY_D)) dx += 1;

        double targetX = getMover().findMoveTarget(dx, 0, Chunk.CHUNK_SIZE)[0];
        double targetY = getMover().findMoveTarget(0, dy, Chunk.CHUNK_SIZE)[1];
        if (getActionQueue().isEmpty()) {
            if ((dx != 0 || dy != 0) && allowUserMovement) {
                getAnimationLayer("arms").setBaseAnimation("walking");
                getAnimationLayer("legs").setBaseAnimation("walking");
                getMover().setTarget(targetX, targetY);
                getMover().setLookTowardsTarget(false);
                getLocation().setLookDirection((int)MiscMath.angleBetween(0, 0, dx, dy));
            } else {
                getAnimationLayer("arms").setBaseAnimation("default");
                getAnimationLayer("legs").setBaseAnimation("default");
                getMover().stop();
            }
        }

    }

}
