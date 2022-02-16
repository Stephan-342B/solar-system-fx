package org.mahefa.common.utils.math.geometry.angle;

import javax.vecmath.Vector3d;

public final class Angle {

    /**
     *
     * @param value
     * @return
     */
    public static double convertArcSecondToDegree(double value) {
        return value * 0.00027778;
    }

    /**
     *
     * @param value
     * @return
     */
    public static double convertArcSecondToRadians(double value) {
        return value * 0.00000485;
    }

    /**
     * Convert degree, minute and second to decimal degree
     *
     * Formulae
     * dd = d + m/60 + s/3600
     *
     *
     * @param degree
     * @param minute
     * @param second
     * @return
     */
    public static double toDecimalDegree(double degree, double minute, double second) {
        return degree + (minute / 60) + (second / 3600);
    }

    /**
     *
     * @param degree
     * @param minute
     * @param second
     * @return
     */
    public static double dmsToRadian(double degree, double minute, double second) {
        return Math.toRadians(toDecimalDegree(degree, minute, second));
    }

    /**
     * Normalize an angle so that it is between 0 and 360.
     *
     * @param angle is the degree to normalize
     *
     * @return angle between 0 - 360 degree
     */
    public static double normalize(double angle) {
        return (angle >= 0.0 && angle <= 360.0) ? angle : angle - Math.floor(angle / 360.0) * 360;
    }

    /**
     *
     * @param degree
     * @return
     */
    public static String convertDecimalDegreeToDMS(final double degree) {
        final int d = (int) degree;
        final double md = (degree - d) * 60;
        final int m = (int) md;
        final int s = (int) ((md - m) * 60);

        return String.format("%s° %s' %s\"", d, m, s);
    }

    /**
     * Compute the angle between two points in 3 dimensional space
     * angle = arccos[(xa * xb + ya * yb + za * zb) / (√(xa2 + ya2 + za2) * √(xb2 + yb2 + zb2))]
     *
     * @param vector3dFrom
     * @param vector3dTo
     * @return angle in degree
     */
    public static double getAngleFromPoints(Vector3d vector3dFrom, Vector3d vector3dTo) {
        final double x1 = vector3dFrom.getX();
        final double y1 = vector3dFrom.getY();
        final double z1 = vector3dFrom.getZ();

        final double x2 = vector3dTo.getX();
        final double y2 = vector3dTo.getY();
        final double z2 = vector3dTo.getZ();

        final double numerator = (x1 * x2) + (y1 * y2) + (z1 * z2);
        final double denominator = Math.abs(Math.sqrt((Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2))))
                * Math.abs(Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2) + Math.pow(z2, 2)));

        return Math.toDegrees(Math.acos(numerator / denominator));
    }

    /**
     * calculate the measure of the distance along the curved line making up the arc (a segment of a circle).
     * In simple words, the distance that runs through the curved line of the circle making up the arc is known as the arc length.
     * It should be noted that the arc length is longer than the straight line distance between its endpoints.
     * Formulae:
     * if angle is in degree
     * s = 2πr * (θ/360°)
     *
     * if angle is in radians
     * s = ϴ × r
     *
     * @param ϴ is the central angle of the arc
     * @param r is the radius of the circle
     * @return arc length
     */
    public static double getArcLength(final double ϴ, final double r, final boolean isAngleInDegree) {
        return (isAngleInDegree) ? (2 * Math.PI - r) * (ϴ / 360) : ϴ * r;
    }
}
