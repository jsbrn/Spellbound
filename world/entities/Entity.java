package world.entities;

import gui.states.GameScreen;
import misc.Location;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Chunk;
import world.Portal;
import world.World;
import world.entities.actions.Action;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.MoveAction;
import world.entities.animations.Animation;
import world.entities.animations.AnimationLayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Entity {

    private Location location;
    private float moveSpeed;

    private HashMap<String, AnimationLayer> animationLayers;

    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.moveSpeed = 3;
        this.action_queue = new ArrayList<>();
        this.animationLayers = new HashMap<>();
    }

    public void update() {
        if (action_queue.isEmpty()) return;
        action_queue.get(0).update();
        if (action_queue.get(0).finished()) action_queue.remove(0);
    }

    public void queueActions(ActionGroup actions) {
        this.action_queue.add(actions);
        actions.setParent(this);
    }

    public void queueAction(Action a) {
        System.out.println("Queueing "+a);
        queueActions(new ActionGroup(a));
    }

    public void skipCurrentAction() {
        if (action_queue.isEmpty()) return;
        action_queue.remove(0);
    }

    public void clearActions() {
        action_queue.clear();
    }

    public void addAnimation(String layer, String name, Animation a) {
        if (animationLayers.get(layer) == null)
            animationLayers.put(layer, new AnimationLayer());
        animationLayers.get(layer).addAnimation(name, a);
    }

    public AnimationLayer getAnimationLayer(String layer) { return animationLayers.get(layer); }
    public Collection<AnimationLayer> getAnimationLayers() { return animationLayers.values(); }

    public ArrayList<ActionGroup> getActionQueue() { return action_queue; }

    public void move(double tx, double ty) {
        int new_x = (int)(location.getCoordinates()[0] + tx);
        int new_y = (int)(location.getCoordinates()[1] + ty);
        if (new_x < 0 || new_x >= Chunk.CHUNK_SIZE || new_y < 0 || new_y >= Chunk.CHUNK_SIZE) return;
        Portal origin = location.getChunk().getPortal(new_x, new_y);
        if (origin != null) {
            Portal destination = origin.getDestination().findPortalTo(location.getRegion(), origin.getName());
            System.out.println(origin.getDestination()+", "+location.getRegion()+", "+origin.getName());
            moveTo(new Location(
                    origin.getDestination(),
                    destination.getChunk(),
                    destination.getTileCoordinates()[0],
                    destination.getTileCoordinates()[1]));
            queueAction(new MoveAction(
                    destination.getTileCoordinates()[0] + destination.getExitDirection()[0],
                    destination.getTileCoordinates()[1] + destination.getExitDirection()[1]));
            if (this.equals(World.getPlayer())) GameScreen.getGUI().setFade(1); //TODO: convert to event
            return;
        }
        queueAction(new MoveAction(new_x, new_y));
    }

    public Location getLocation() { return location; }

    public void moveTo(Location new_) {
        System.out.println("Setting "+this+" location to "+new_);
        if (location != null) location.getChunk().removeEntity(this);
        location = new_;
        location.getChunk().addEntity(this);
    }

    public float getMoveSpeed() { return this.moveSpeed; }
    public void setMoveSpeed(float moveSpeed) { this.moveSpeed = moveSpeed; }

    public void draw(float sx, float sy, float scale) {
        float ex = sx + ((float)location.getCoordinates()[0] * scale * Chunk.TILE_SIZE);
        float ey = sy + ((float)location.getCoordinates()[1] * scale * Chunk.TILE_SIZE);

        for (AnimationLayer animationLayer: animationLayers.values()) {
            Animation anim = animationLayer.getAnimationByName(animationLayer.getCurrentAnimation());
            if (anim != null)
                anim.draw(
                        ex,
                        ey,
                        scale,
                        (location.getLookDirection() + (360 / 16)) / (360 / 8));
        }


    }

    public void drawDebug(float sx, float sy, float scale, Graphics g) {
        float ex = sx + ((float)location.getCoordinates()[0] * scale * Chunk.TILE_SIZE);
        float ey = sy + ((float)location.getCoordinates()[1] * scale * Chunk.TILE_SIZE);
        g.setColor(Color.blue);
        g.drawOval(ex, ey, Chunk.TILE_SIZE * scale, Chunk.TILE_SIZE * scale);
        g.setColor(Color.red);
        double[] offsets = MiscMath.getRotatedOffset(0, -Chunk.TILE_SIZE * scale, location.getLookDirection());
        g.drawLine(
                ex + (Chunk.TILE_SIZE*scale/2),
                ey + (Chunk.TILE_SIZE*scale/2),
                (float)(ex + (Chunk.TILE_SIZE*scale/2) + offsets[0]),
                (float)(ey + (Chunk.TILE_SIZE*scale/2) + offsets[1]));
        g.setColor(Color.white);
    }

}
