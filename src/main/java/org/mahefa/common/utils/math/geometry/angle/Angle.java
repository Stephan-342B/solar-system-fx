package org.mahefa.common.utils.math.geometry.angle;

public final class Angle {

    /**
     *
     * @param value
     * @return
     */
    public static double convertArcSecondToDegree(double value) {
        return AngleImpl.convertArcSecondToDegree(value);
    }

    /**
     *
     * @param value
     * @return
     */
    public static double convertArcSecondToRadians(double value) {
        return AngleImpl.convertArcSecondToRadians(value);
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
        return AngleImpl.toDecimalDegree(degree, minute, second);
    }

    /**
     *
     * @param degree
     * @param minute
     * @param second
     * @return
     */
    public static double dmsToRadian(double degree, double minute, double second) {
        return AngleImpl.dmsToRadian(degree, minute, second);
    }

    /**
     * Normalize an angle so that it is between 0 and 360.
     *
     * @param angle is the degree to normalize
     *
     * @return angle between 0 - 360 degree
     */
    public static double normalize(double angle) {
        return AngleImpl.normalize(angle);
    }
}
