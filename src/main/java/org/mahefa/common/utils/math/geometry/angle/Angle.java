package org.mahefa.common.utils.math.geometry.angle;

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

}
