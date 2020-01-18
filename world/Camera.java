package world;

import misc.Location;
import misc.Window;
import world.entities.Entity;

public class Camera {

    private static Entity target;

    public static float[] getOnscreenCoordinates(double wx, double wy, double scale) {
        float osx = (Window.getWidth() / 2) - (float)((target.getLocation().getCoordinates()[0] - wx) * scale * Chunk.TILE_SIZE);
        float osy = (Window.getHeight() / 2) - (float)((target.getLocation().getCoordinates()[1] - wy) * scale * Chunk.TILE_SIZE);
        return new float[]{osx, osy};
    }

    public static double[] getWorldCoordinates(double osx, double osy, double scale) {
        double wx = target.getLocation().getCoordinates()[0] + ((osx - (double)(Window.getWidth() / 2)) / scale / Chunk.TILE_SIZE);
        double wy = target.getLocation().getCoordinates()[1] + ((osy - (double)(Window.getHeight() / 2)) / scale / Chunk.TILE_SIZE);
        return new double[]{wx, wy};
    }

    public static void setTarget(Entity e) {
        target = e;
    }

}
