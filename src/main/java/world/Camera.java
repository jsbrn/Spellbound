package world;

import misc.MiscMath;
import misc.Window;

public class Camera {

    private static int speed = 10;
    private static boolean manualMode;
    private static double[] world_coords = new double[]{0, 0};
    private static Entity target;

    public static double[] getLocation() {
        return manualMode || target == null ? world_coords : target.getLocation().getCoordinates();
    }

    public static float[] getOnscreenCoordinates(double wx, double wy, double scale) {
        float osx = (Window.getWidth() / 2) - (float)((getLocation()[0] - wx) * scale * Chunk.TILE_SIZE);
        float osy = (Window.getHeight() / 2) - (float)((getLocation()[1] - wy) * scale * Chunk.TILE_SIZE);
        return new float[]{osx, osy};
    }

    public static double[] getWorldCoordinates(double osx, double osy, double scale) {
        double wx = getLocation()[0] + ((osx - (double)(Window.getWidth() / 2)) / scale / Chunk.TILE_SIZE);
        double wy = getLocation()[1] + ((osy - (double)(Window.getHeight() / 2)) / scale / Chunk.TILE_SIZE);
        return new double[]{wx, wy};
    }

    public static void move(int dx, int dy) {
        setTarget(world_coords[0] + MiscMath.getConstant(speed * dx, 1), world_coords[1] + MiscMath.getConstant(dy * speed, 1));
    }

    public static void setTarget(double wx, double wy) {
        world_coords = new double[]{wx, wy};
        manualMode = true;
    }

    public static void setSpeed(int speed) {
        Camera.speed = speed;
    }

    public static void setManualMode(boolean manualMode) {
        Camera.manualMode = manualMode;
        world_coords[0] = target.getLocation().getCoordinates()[0];
        world_coords[1] = target.getLocation().getCoordinates()[1];
    }

    public static boolean isManualMode() {
        return manualMode;
    }

    public static void setTarget(Entity e) {
        target = e; manualMode = false;
    }

}
