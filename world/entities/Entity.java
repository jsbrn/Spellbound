package world.entities;

import misc.MiscMath;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Chunk;

public class Entity {

    private int[] chunk_coordinates;
    private double[] coordinates, start, target;
    private int walk_speed, height;
    private Image sprite;

    public Entity() {
        this.walk_speed = 3;
        this.height = 1;
        this.chunk_coordinates = new int[]{0, 0};
        this.coordinates = new double[]{0, 0};
        this.start = new double[]{-1, -1};
        this.target = new double[]{0, 0};
        try {
            this.sprite = new Image("assets/player.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void move(double tx, double ty) {
        if (start[0] == -1) {
            target[0] = coordinates[0] + tx;
            start[0] = coordinates[0];
        }
        if (start[1] == -1) {
            target[1] = coordinates[1] + ty;
            start[1] = coordinates[1];
        }
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public String debug() {
        return "["+ start[0] +","+ start[1] +"] "+ "["+ coordinates[0] +","+ coordinates[1] +"] "+ "["+ target[0] +","+ target[1] +"]";
    }

    public void move() {
        if (coordinates[0] == target[0]) start[0] = -1;
        if (coordinates[1] == target[1]) start[1] = -1;

        if (start[0] != -1) {
            coordinates[0] = MiscMath.tween(start[0], coordinates[0], target[0], walk_speed, 1);
        }
        if (start[1] != -1) {
            coordinates[1] = MiscMath.tween(start[1], coordinates[1], target[1], walk_speed, 1);
        }

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
