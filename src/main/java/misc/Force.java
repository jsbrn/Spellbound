package misc;

public class Force {

    private double direction, magnitude, deceleration;
    private double[] directions;

    public Force(double direction, double magnitude, double deceleration) {
        this.magnitude = magnitude;
        this.direction = direction;
        this.directions = MiscMath.getRotatedOffset(0, -1, direction);
        this.deceleration = deceleration;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void updateMagnitude() {
        magnitude -= MiscMath.getConstant(deceleration, 1);
        magnitude = MiscMath.clamp(magnitude, 0, Double.MAX_VALUE);
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double getDirection() {
        return direction;
    }

    public double[] getDirections() {
        return directions;
    }

}
