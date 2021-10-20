package org.mahefa.common.utils.math.geometry.angle;

public final class AngleImpl {

    public static double convertArcSecondToDegree(double value) {
        return value * 0.00027778;
    }

    public static double convertArcSecondToRadians(double value) {
        return value * 0.00000485;
    }

    public static double toDecimalDegree(double degree, double minute, double second) {
        return degree + (minute / 60) + (second / 3600);
    }

    public static double dmsToRadian(double degree, double minute, double second) {
        return Math.toRadians(toDecimalDegree(degree, minute, second));
    }

    public static double normalize(double angle) {
        return (angle >= 0.0 && angle <= 360.0) ? angle : angle - Math.floor(angle / 360.0) * 360;
    }

    public static String convertDecimalDegreeToDMS(final double degree) {
        final int d = (int) degree;
        final double md = (degree - d) * 60;
        final int m = (int) md;
        final int s = (int) ((md - m) * 60);

        return String.format("%s° %s' %s\"", d, m, s);
    }
}