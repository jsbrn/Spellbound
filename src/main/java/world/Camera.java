package world;

import misc.Location;
import misc.MiscMath;
import misc.Window;
import network.MPClient;
import world.entities.Entities;
import world.entities.components.LocationComponent;

public class Camera {

    private static int speed = 4;
    private static int targetEntity = -1;
    private static Location location;

    public static Location getLocation() {
        if (location == null) location = new Location("world", 0, 0);
        return location;
    }

    private static Location getTargetLocation() {
        LocationComponent loc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, targetEntity);
        return loc == null ? null : loc.getLocation();
    }

    public static void update() {
        if (location == null || getTargetLocation() == null) return;
        double angle = MiscMath.angleBetween(
                location.getCoordinates()[0],
                location.getCoordinates()[1],
                getTargetLocation().getCoordinates()[0],
                getTargetLocation().getCoordinates()[1]);
        double dist = location.distanceTo(getTargetLocation());
        double[] dirs = MiscMath.getRotatedOffset(0, -1, angle);
        getLocation().addCoordinates(dirs[0] * MiscMath.getConstant(dist * speed, 1), dirs[1] * MiscMath.getConstant(dist * speed, 1));
    }

    public static int getTargetEntity() {
        return targetEntity;
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

    public static void setSpeed(int speed) {
        Camera.speed = speed;
    }

    public static void setTargetEntity(int e) {
        targetEntity = e;
    }

}
