package org.mahefa.common.utils.math.astronomy;

public final class AstroMathImpl {

    public static double getEccentricAnomaly(double e, double M, double... eccentricityAnomaly) {
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

    public static double distanceToEarth(double x, double y, double z) {
        return Math.sqrt(
                (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))
        );
    }

    public static double horner(double x, double[] coefficients) {
        final int length = coefficients.length;
        double y = coefficients[length - 1];

        for(int i = length - 2; i >= 0; i--) {
            y = (y * x) + coefficients[i];
        }

        return y;
    }

    public static double round(double value, double decimal) {
        return (double) Math.round(value * decimal) / decimal;
    }
}
