package world.entities;

import world.Chunk;
import world.entities.actions.Action;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.MoveAction;
import world.entities.animations.Animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity {

    private int[] chunk_coordinates;
    private double[] coordinates;
    private int moveSpeed;

    private HashMap<String, Animation> animations;
    private Animation current_animation;

    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.moveSpeed = 3;
        this.chunk_coordinates = new int[]{-1, -1};
        this.coordinates = new double[]{1, 1};
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
        queueAction(new MoveAction((int)coordinates[0] + tx, (int)coordinates[1] + ty, moveSpeed));
    }

    public int[] getChunkCoordinates() {
        return chunk_coordinates;
    }
    public double[] getCoordinates() { return coordinates; }
    public void setCoordinates(double x, double y) { this.coordinates[0] = x; this.coordinates[1] = y; }
    public void setChunkCoordinates(int x, int y) { this.chunk_coordinates[0] = x; this.chunk_coordinates[1] = y; }

    public void draw(float sx, float sy, float scale) {
        float ex = sx + ((float)coordinates[0] * scale * Chunk.TILE_SIZE);
        float ey = sy + ((float)coordinates[1] * scale * Chunk.TILE_SIZE);
        current_animation.draw(ex, ey, scale);
    }

}
