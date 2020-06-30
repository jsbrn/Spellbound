package world;

import misc.Location;
import misc.MiscMath;
import misc.Window;
import world.entities.Entities;
import world.entities.components.LocationComponent;

public class Camera {

    private static int speed = 10, targetEntity;
    private static Location location;

    public static Location getLocation() {
        return location;
    }

    public static float[] getOnscreenCoordinates(double wx, double wy, double scale) {
        float osx = (Window.getWidth() / 2) - (float)((getLocation().getCoordinates()[0] - wx) * scale * Chunk.TILE_SIZE);
        float osy = (Window.getHeight() / 2) - (float)((getLocation().getCoordinates()[1] - wy) * scale * Chunk.TILE_SIZE);
        return new float[]{osx, osy};
    }

    public static double[] getWorldCoordinates(double osx, double osy, double scale) {
        double wx = getLocation().getCoordinates()[0] + ((osx - (double)(Window.getWidth() / 2)) / scale / Chunk.TILE_SIZE);
        double wy = getLocation().getCoordinates()[1] + ((osy - (double)(Window.getHeight() / 2)) / scale / Chunk.TILE_SIZE);
        return new double[]{wx, wy};
    }

    public static void move(int dx, int dy) {
        double[] world_coords = location.getCoordinates();
        setTarget(world_coords[0] + MiscMath.getConstant(speed * dx, 1), world_coords[1] + MiscMath.getConstant(dy * speed, 1));
    }

    public static void setTarget(double wx, double wy) {
        location.setCoordinates(wx, wy);
    }

    public static void setSpeed(int speed) {
        Camera.speed = speed;
    }

    public static void setTargetEntity(Integer e) {
        targetEntity = e;
    }

}
