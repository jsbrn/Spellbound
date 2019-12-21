package world.entities;

import misc.Location;
import world.Chunk;
import world.entities.actions.Action;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.MoveAction;
import world.entities.animations.Animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity {

    private Location location;
    private float moveSpeed;

    private HashMap<String, Animation> animations;
    private Animation current_animation;

    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.moveSpeed = 3;
        this.action_queue = new ArrayList<>();
        this.animations = new HashMap<>();
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

    public void setAnimation(String name) {
        current_animation = animations.get(name);
        if (current_animation == null) setAnimation("idle");
        current_animation.reset();
    }

    public void addAnimation(String name, Animation a) {
        this.animations.put(name, a);
    }

    public Animation getAnimation(String name) { return this.animations.get(name); }

    public ArrayList<ActionGroup> getActionQueue() { return action_queue; }

    public void move(double tx, double ty) {
        queueAction(new MoveAction((int)location.getCoordinates()[0] + tx, (int)location.getCoordinates()[1] + ty));
    }

    public Location getLocation() { return location; }

    public void moveTo(Location new_) {
        if (location != null) location.getChunk().removeEntity(this);
        location = new_;
        location.getChunk().addEntity(this);
    }

    public float getMoveSpeed() { return this.moveSpeed; }
    public void setMoveSpeed(float moveSpeed) { this.moveSpeed = moveSpeed; }

    public void draw(float sx, float sy, float scale) {
        float ex = sx + ((float)location.getCoordinates()[0] * scale * Chunk.TILE_SIZE);
        float ey = sy + ((float)location.getCoordinates()[1] * scale * Chunk.TILE_SIZE);
        current_animation.draw(ex, ey, scale);
    }

    public String debug() {
        String d = "";
        for (ActionGroup ag: action_queue) d += ag.debug();
        return d;
    }

}
