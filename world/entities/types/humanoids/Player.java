package world.entities.types.humanoids;

import assets.definitions.Definitions;
import assets.definitions.TileDefinition;
import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.Chunk;
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

    private double[] moveTarget;

    public Player() {

        super();

        this.setMaxHP(10);
        this.setMaxMana(10);
        this.setHP(10);
        this.setMana(5);
        this.setMaxStamina(10);

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
                            getLocation().lookAt(mce.getX(), mce.getY());
                            actions.add(new SetAnimationAction("arms", "casting", true));
                            actions.add(new CastSpellAction(mce.getX(), mce.getY()));
                            actions.add(new SetAnimationAction("arms", "default", false));
                            getSpellbook().getParent().queueActions(actions);
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

        double targetX = findMoveTarget(dx, 0)[0];
        double targetY = findMoveTarget(0, dy)[1];
        if (getActionQueue().isEmpty()) {
            if (dx != 0 || dy != 0) {
                getAnimationLayer("arms").setAnimation("walking");
                getAnimationLayer("legs").setAnimation("walking");
                getMover().setTargetX(targetX);
                getMover().setTargetY(targetY);
                getLocation().setLookDirection((int)MiscMath.angleBetween(0, 0, dx, dy));
            } else {
                getAnimationLayer("arms").setAnimation("default");
                getAnimationLayer("legs").setAnimation("default");
                getMover().stop();
            }
        }

    }

    private double[] findMoveTarget(int dx, int dy) {
        double[] coordinates = getLocation().getCoordinates();
        double[] potentialTarget = new double[]{ coordinates[0], coordinates[1] };
        for (double i = 0.5; i < Chunk.CHUNK_SIZE; i += 0.5) {
            double tx = coordinates[0] + (i * dx);
            double ty = coordinates[1] + (i * dy);
            byte[] tile = World.getRegion().getTile((int)tx, (int)ty);
            boolean collides = Definitions.getTile(tile[0]).collides() || Definitions.getTile(tile[1]).collides();
            if (collides) break;
            potentialTarget[0] = tx;
            potentialTarget[1] = ty;
        }
        return potentialTarget;
    }

}
