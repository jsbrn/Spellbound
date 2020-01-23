package world.entities;

import assets.definitions.Definitions;
import assets.definitions.TileDefinition;
import gui.states.GameScreen;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Camera;
import world.Chunk;
import world.Portal;
import world.World;
import world.entities.actions.Action;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.MoveAction;
import world.entities.animations.Animation;
import world.entities.animations.AnimationLayer;
import world.entities.states.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Entity {

    private Mover mover;
    private Location location;
    private State currentState;

    private String conversationStartingPoint;

    private HashMap<String, AnimationLayer> animationLayers;
    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.action_queue = new ArrayList<>();
        this.animationLayers = new HashMap<>();
        this.mover = new Mover();
        this.mover.setParent(this);
        this.conversationStartingPoint = "greeting";
    }

    public void update() {
        if (currentState != null) currentState.update();
        mover.update();
        if (action_queue.isEmpty()) return;
        action_queue.get(0).update();
        if (action_queue.get(0).finished()) action_queue.remove(0);
    }

    public void setConversationStartingPoint(String dialogue_id) {
        this.conversationStartingPoint = dialogue_id;
    }

    public String getConversationStartingPoint() { return conversationStartingPoint; }

    public void queueActions(ActionGroup actions) {
        this.action_queue.add(actions);
        actions.setParent(this);
    }

    public void queueAction(Action a) {
        queueActions(new ActionGroup(a));
    }

    public Action getCurrentAction() { return action_queue.isEmpty() ? null : action_queue.get(0).getCurrentAction(); }

    public void cancelCurrentAction() {
        if (action_queue.isEmpty()) return;
        action_queue.remove(0).getCurrentAction().onCancel();
    }

    public void clearActions() {
        cancelCurrentAction();
        action_queue.clear();
    }

    public void enterState(State state) {
        exitState();
        currentState = state;
        if (currentState != null) {
            currentState.setParent(this);
            currentState.onEnter();
            clearActions();
        }
    }

    public State getCurrentState() { return currentState; }

    public void exitState() {
        if (currentState != null) currentState.onExit();
        currentState = null;
    }

    public void addAnimation(String layer, String name, Animation a) {
        if (animationLayers.get(layer) == null)
            animationLayers.put(layer, new AnimationLayer());
        animationLayers.get(layer).addAnimation(name, a);
    }

    public AnimationLayer getAnimationLayer(String layer) { return animationLayers.get(layer); }
    public Collection<AnimationLayer> getAnimationLayers() { return animationLayers.values(); }

    public ArrayList<ActionGroup> getActionQueue() { return action_queue; }

    public Mover getMover() {
        return mover;
    }

    public Location getLocation() { return location; }

    public double canSee(Entity e) { return canSee(
            (int)e.getLocation().getCoordinates()[0],
            (int)e.getLocation().getCoordinates()[1]);
    }

    public double canSee(int wx, int wy) {
        double[] coords = getLocation().getCoordinates();
        int dist = 1;
        int range = 1 + (int)MiscMath.distance(coords[0], coords[1], wx, wy);
        double visibility = 1;
        double angle = MiscMath.angleBetween((int)coords[0], (int)coords[1], wx, wy);
        double[] offset;
        while (dist < range) {
            offset = MiscMath.getRotatedOffset(0, -dist, angle);
            byte[] tile = getLocation().getRegion().getTile((int)(coords[0] + offset[0]), (int)(coords[1] + offset[1]));
            TileDefinition base = Definitions.getTile(tile[0]);
            TileDefinition top = Definitions.getTile(tile[1]);
            if ((int)(coords[0] + offset[0]) == (int)wx && (int)(coords[1] + offset[1]) == (int)wy) break;
            if (base.getTransparency() == 0 || top.getTransparency() == 0) { visibility = 0; break; }
            visibility *= (base.getTransparency() * top.getTransparency());
            dist++;
        }
        return visibility;
    }

    public void moveTo(Location new_) {
        if (location != null && location.getRegion() != null) location.getRegion().removeEntity(this);
        location = new_;
        location.getRegion().addEntity(this);
    }

    public void draw(float osx, float osy, float scale) {
        for (AnimationLayer animationLayer: animationLayers.values()) {
            Animation anim = animationLayer.getAnimationByName(animationLayer.getCurrentAnimation());
            if (anim != null)
                anim.draw(
                        osx,
                        osy,
                        scale,
                        (location.getLookDirection() + (360 / 16)) / (360 / 8));
        }

    }

    public void drawDebug(float scale, Graphics g) {

        float osc[] = Camera.getOnscreenCoordinates(getLocation().getCoordinates()[0], getLocation().getCoordinates()[1], scale);

        float tosc[] = Camera.getOnscreenCoordinates(getMover().getTarget()[0], getMover().getTarget()[1], scale);

        g.setColor(Color.blue);
        g.drawOval(osc[0]- (Chunk.TILE_SIZE * scale / 2), osc[1]- (Chunk.TILE_SIZE * scale / 2), Chunk.TILE_SIZE * scale, Chunk.TILE_SIZE * scale);

        g.setColor(Color.cyan);
        if (!getMover().isIndependent()) {
            g.drawLine( osc[0], osc[1], tosc[0], tosc[1]);
        } else {
            g.drawLine(osc[0], osc[1], tosc[0], osc[1]);
            g.drawLine(osc[0], osc[1], osc[0], tosc[1]);
        }

        g.setColor(Color.red);
        double[] offsets = MiscMath.getRotatedOffset(0, -Chunk.TILE_SIZE * scale, location.getLookDirection());
        g.drawLine(
                osc[0],
                osc[1],
                (float)(osc[0] + offsets[0]),
                (float)(osc[1] + offsets[1]));

        g.setColor(Color.yellow);
        for (Location l: getMover().getPath()) {
            float[] losc = Camera.getOnscreenCoordinates(l.getCoordinates()[0], l.getCoordinates()[1], Window.getScale());
            g.drawRect(losc[0] - Window.getScale(), losc[1] - Window.getScale(), 2*Window.getScale(), 2*Window.getScale());
        }

        g.setColor(Color.white);
    }

}
