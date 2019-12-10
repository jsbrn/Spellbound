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
    private int walk_speed, height;

    private HashMap<String, Animation> animations;
    private Animation current_animation;

    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.walk_speed = 3;
        this.height = 1;
        this.chunk_coordinates = new int[]{0, 0};
        this.coordinates = new double[]{1, 1};
        this.action_queue = new ArrayList<>();
        this.animations = new HashMap<>();
    }

    public void update() {
        if (action_queue.isEmpty()) return;
        action_queue.get(0).update();
        if (action_queue.get(0).finished()) action_queue.remove(0);
    }

    public void queueAction(Action a) {
        a.setParent(this);
        action_queue.add(new ActionGroup(a));
    }

    public void stopAction() {
        if (action_queue.isEmpty()) return;
        action_queue.remove(0);
    }

    public void stopAllActions() {
        action_queue.clear();
    }

    public void setAnimation(String name) {
        current_animation = animations.get(name);
        if (current_animation == null) setAnimation("idle");
    }

    public void addAnimation(String name, Animation a) {
        this.animations.put(name, a);
    }

    public ArrayList<ActionGroup> getActionQueue() { return action_queue; }

    public void move(double tx, double ty) {
        queueAction(new MoveAction((int)coordinates[0] + tx, (int)coordinates[1] + ty, walk_speed));
    }

    public int[] getChunkCoordinates() {
        return chunk_coordinates;
    }
    public double[] getCoordinates() { return coordinates; }
    public void setCoordinates(double x, double y) { this.coordinates[0] = x; this.coordinates[1] = y; }
    public void setChunkCoordinates(int x, int y) { this.chunk_coordinates[0] = x; this.chunk_coordinates[1] = y; }

    public String debug() {
        return "ACTION_GROUPS_COUNT = " + action_queue.size();
    }

    public void draw(float sx, float sy, float scale) {
        float ex = sx + ((float)coordinates[0] * scale * Chunk.TILE_SIZE);
        float ey = sy + ((float)coordinates[1] * scale * Chunk.TILE_SIZE);
        current_animation.draw(ex, ey, scale);
    }

}
