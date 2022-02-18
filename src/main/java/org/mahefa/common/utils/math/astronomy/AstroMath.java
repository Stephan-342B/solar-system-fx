package org.mahefa.common.utils.math.astronomy;

import org.fxyz3d.geometry.Vector3D;
import org.mahefa.data.meeus.jean.Term;

import java.util.List;

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
        double tmp = M;

        if(eccentricityAnomaly != null && eccentricityAnomaly.length > 0) {
            tmp = eccentricityAnomaly[0];
        }

        double E = M + (e * Math.sin(tmp));
        double Es = (double) round(E, 1e8); // precision at 12 decimal places
        double Tmps = (double) round(tmp, 1e8);

        if((Es - Tmps) == 0 || (Es - Tmps) == 1e-8) {
            return E;
        }

        return getEccentricAnomaly(e, M, E);
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double distanceToEarth(double x, double y, double z) {
        return Math.sqrt(
                (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))
        );
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
        final int length = coefficients.length;
        double y = coefficients[length - 1];

        for(int i = length - 2; i >= 0; i--) {
            y = (y * x) + coefficients[i];
        }

        return y;
    }

    /**
     *
     * @param value
     * @param decimal
     * @return
     */
    public static double round(double value, double decimal) {
        return (double) Math.round(value * decimal) / decimal;
    }

    /**
     * Calculate the value of each term by this formulae
     * A cos (b + Ct)
     *
     * the quantities B and C are expressed in radians. The coefficients A are in units of 10^~8 radian in the
     * case of the longitude and the latitude, in units of 10^8 astronomical unit for the radius vector.
     *
     * @param termList
     * @param t
     *
     * @return
     */
    public static double computeTerm(final List<Term> termList, final double t) {
        int i = 0;
        double[] rows = new double[termList.size()];

        for(Term term: termList) {
            final double length = term.getLength();
            double row = 0d;

            for(int j = 0; j < length; j++) {
                final double A = term.getA().get(j);
                final double B = term.getB().get(j);
                final double C = (term.getC().get(j) * t);

                row += (A * Math.cos(B + C));
            }

            rows[i] = row;
            i++;
        }

        return AstroMath.horner(t, rows) / Math.pow(10, 8);
    }

    public static Vector3D getCoordinates(final double L, final double B, final double R, final double L0, final double B0, final double R0) {
        final double x = (R * Math.cos(B) * Math.cos(L)) - (R0 * Math.cos(B0) * Math.cos(L0));
        final double y = (R * Math.cos(B) * Math.sin(L)) - (R0 * Math.cos(B0) * Math.sin(L0));
        final double z = (R * Math.sin(B)) - (R0 * Math.sin(B0));

        return new Vector3D(
                AstroMath.round(x, 1e6),
                AstroMath.round(y, 1e6),
                AstroMath.round(z, 1e6)
        );
    }
}
