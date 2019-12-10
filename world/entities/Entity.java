package world.entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Chunk;
import world.entities.actions.Action;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.MoveAction;

import java.util.ArrayList;

public class Entity {

    private int[] chunk_coordinates;
    private double[] coordinates;
    private int walk_speed, height;
    private Image sprite;

    private ArrayList<ActionGroup> action_queue;

    public Entity() {
        this.walk_speed = 3;
        this.height = 1;
        this.chunk_coordinates = new int[]{0, 0};
        this.coordinates = new double[]{0, 0};
        this.action_queue = new ArrayList<>();
        try {
            this.sprite = new Image("assets/player.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (action_queue.isEmpty()) return;
        action_queue.get(0).update();
        if (action_queue.get(0).finished()) action_queue.remove(0);
    }

    public void queueAction(Action a) {
        a.setParent(this);
        a.init();
        action_queue.add(new ActionGroup(a));
    }

    public void stopAction() {
        if (action_queue.isEmpty()) return;
        action_queue.remove(0);
    }

    public void stopAllActions() {
        action_queue.clear();
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

    public String debug() {
        return "ACTION_GROUPS_COUNT = " + action_queue.size();
    }

    public void draw(float sx, float sy, float scale) {
        sprite.draw(
                sx + ((float)coordinates[0] * scale * Chunk.TILE_SIZE),
                sy + ((float)coordinates[1] * scale * Chunk.TILE_SIZE) - (height * Chunk.TILE_SIZE * scale / 2),
                Chunk.TILE_SIZE * scale,
                Chunk.TILE_SIZE * scale
        );
    }

}
