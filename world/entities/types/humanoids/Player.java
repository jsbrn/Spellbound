package world.entities.types.humanoids;

import gui.states.GameState;
import main.Game;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.Camera;
import world.Chunk;
import world.World;
import world.entities.actions.ActionGroup;
import world.entities.actions.types.ActivateAction;
import world.entities.actions.types.CastSpellAction;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.magic.Spell;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.*;

import java.util.ArrayList;

public class Player extends HumanoidEntity {

    private double[] moveTarget;
    private boolean allowUserMovement, godMode;

    public Player() {

        super();

        this.allowUserMovement = true;
        this.setAllegiance("players");
        this.getMover().setIndependent(true);
        this.getMover().setLookTowardsTarget(false);

        this.setMaxMana(100);
        this.setMana(100);

        Spell kb = new Spell();
        kb.addTechnique("physical_weight");
        kb.addTechnique("movement_directional");
        kb.addTechnique("physical_speed");
        kb.addTechnique("physical_collision");
        kb.addTechnique("physical_energy");
        kb.setLevel("physical_energy", 4);
        kb.setLevel("physical_speed", 2);
        kb.setName("Green Ball of Fury");
        kb.setColor(Color.green);
        getSpellbook().addSpell(kb);

        this.getAnimationLayer("head").setColor(SKIN_COLORS[0]);

        Player that = this;
        EventDispatcher.register(new EventListener()
                .on(MousePressedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        if (isDead() || !getActionQueue("arms").isEmpty()) return;
                        MousePressedEvent mce = (MousePressedEvent)e;
                        ActionGroup actions = new ActionGroup();
                        if (getSpellbook().getParent().getMana() >= 1 && mce.getButton() == 0
                            && that.getActionQueue().isEmpty()) {
                            getLocation().lookAt(mce.getX(), mce.getY());
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new ChangeAnimationAction("arms", "casting", true, true));
                            getSpellbook().getParent().getActionQueue("arms").queueActions(actions);
                        }
                    }
                })
                .on(KeyDownEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        if (isDead()) return;
                        KeyDownEvent kde = (KeyDownEvent)e;
                        if (kde.getKey() == Input.KEY_E && that.getActionQueue().isEmpty()) {
                            that.getActionQueue().queueAction(new ActivateAction());
                        }
                    }
                })
                .on(ConversationStartedEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        if (isDead()) return;
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
                        if (isDead()) return;
                        ConversationEndedEvent cse = (ConversationEndedEvent)e;
                        if (cse.getPlayer().equals(that)) {
                            that.allowUserMovement = true;
                        }
                    }
                })
        );

    }

    public void update() {

        if (isDead()) return;

        if (godMode) {
            this.getSpellbook().discoverAllTechniques();
            this.getMover().setSpeed(10);
            this.setHP(1000, true);
            this.getMover().setCollidable(false);
            this.getMover().setIgnoreCollision(true);
        }

        super.update();

        int dx = 0, dy = 0;

        if (Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_W)) dy -= 1;
        if (Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_A)) dx -= 1;
        if (Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_S)) dy += 1;
        if (Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_D)) dx += 1;

        double targetX = getMover().findMoveTarget(dx, 0, Chunk.CHUNK_SIZE)[0];
        double targetY = getMover().findMoveTarget(0, dy, Chunk.CHUNK_SIZE)[1];
        if (getActionQueue().isEmpty()) {

            double[] mouse_wc = Camera.getWorldCoordinates(
                    Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().getMouseX(),
                    Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().getMouseY(), Window.getScale());
            if (Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_LCONTROL))
                getLocation().setLookDirection((int)MiscMath.angleBetween(
                        getLocation().getCoordinates()[0],
                        getLocation().getCoordinates()[1],
                        mouse_wc[0], mouse_wc[1]));

            getMover().setSpeed(4);
            getMover().setIndependent(true);
            if ((dx != 0 || dy != 0) && allowUserMovement) {
                getAnimationLayer("arms").setBaseAnimation("walking");
                getAnimationLayer("legs").setBaseAnimation("walking");
                getMover().setTarget(targetX, targetY);
                getMover().setLookTowardsTarget(false);
                if (getActionQueue("arms").isEmpty() && !Game.getGameState(GameState.GAME_SCREEN).getGUI().getParent().getInput().isKeyDown(Input.KEY_LCONTROL))
                    getLocation().setLookDirection((int)MiscMath.angleBetween(0, 0, dx, dy));
            } else {
                getAnimationLayer("arms").setBaseAnimation("default");
                getAnimationLayer("legs").setBaseAnimation("default");
                getMover().stop();
            }
        }

    }

    public void activateGodMode() { godMode = true; }

}
