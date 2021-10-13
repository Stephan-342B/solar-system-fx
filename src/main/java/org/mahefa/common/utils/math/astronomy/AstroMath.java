package org.mahefa.common.utils.math.astronomy;

public final class AstroMath {

    /**
     * Universal Gravitational Constant: 6.67*10^-11 Newtons kg^⁻2 m^2
     */
    public static final double G = 6.67e-11;

    /**
     * Time: Day in second = 1[day] * 24[hr/day] * 60[min/hr] * 60[sec/min]
     */
    public static final double T = 1 * 24 * 60 * 60;

    /**
     * Astronomical unit in Meter
     */
    public static final double AU = 1.495978707e11;

    /**
     * Compute the eccentric anomaly E from the mean anomaly M and from the eccentricity e
     * to an accuracy of 0.000001
     *
     * @param e eccentricity
     * @param M mean anomaly in radians
     * @param eccentricityAnomaly eccentricity anomaly
     *
     * @return
     */
    public static double getEccentricAnomaly(final double e, final double M, double... eccentricityAnomaly) {
        return AstroMathImpl.getEccentricAnomaly(e, M, eccentricityAnomaly);
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double distanceToEarth(double x, double y, double z) {
        return AstroMathImpl.distanceToEarth(x, y, z);
    }

    /**
     * Use the Horner's method to compute a polynomial form
     *
     * y = A + x (B + x (c + x (d + xE)))
     *
     * @param x
     * @param coefficients
     * @return
     */
    public static double horner(double x, double[] coefficients) {
        return AstroMathImpl.horner(x, coefficients);
    }

    /**
     *
     * @param value
     * @param decimal
     * @return
     */
    public static double round(double value, double decimal) {
        return AstroMathImpl.round(value, decimal);
    }
}
