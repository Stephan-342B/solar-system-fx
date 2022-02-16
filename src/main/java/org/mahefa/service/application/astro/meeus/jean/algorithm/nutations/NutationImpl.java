package org.mahefa.service.application.astro.meeus.jean.algorithm.nutations;

import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.springframework.stereotype.Service;

@Service
public class NutationImpl implements Nutation {

    private static final double[][] PERIODIC_TERMS = {
            /* D, M, M', F, Ω, sin 0, sin 1, cos 0, cos 1 */
            { 0, 0, 0, 0, 1, -171996, -174.2, 92025, 8.9 },
            { -2, 0, 0, 2, 2, -13187, -1.6, 5736, -3.1 },
            { 0, 0, 0, 2, 2, -2274, -0.2, 977, -0.5 },
            { 0, 0, 0, 0, 2, 2062, 0.2, -895, 0.5 },
            { 0, 1, 0, 0, 0, 1426, -3.4, 54, -0.1 },
            { 0, 0, 1, 0, 0, 712, 0.1, -7, 0 },
            { -2, 1, 0, 2, 2, -517, 1.2, 224, -0.6 },
            { 0, 0, 0, 2, 1, -386, -0.4, 200, 0 },
            { 0, 0, 1, 2, 2, -301, 0, 129, -0.1 },
            { -2, -1, 0, 2, 2, 217, -0.5, -95, 0.3 },
            { -2, 0, 1, 0, 0, -158, 0, 0, 0 },
            { -2, 0, 0, 2, 1, 129, 0.1, -70, 0 },
            { 0, 0, -1, 2, 2, 123, 0, -53, 0 },
            { 2, 0, 0, 0, 0, 63, 0, 0, 0 },
            { 0, 0, 1, 0, 1, 63, 0.1, -33, 0 },
            { 2, 0, -1, 2, 2, -59, 0, 26, 0 },
            { 0, 0, -1, 0, 1, -58, -0.1, 32, 0 },
            { 0, 0, 1, 2, 1, -51, 0, 27, 0 },
            { -2, 0, 2, 0, 0, 48, 0, 0, 0 },
            { 0, 0, -2, 2, 1, 46, 0, -24, 0 },
            { 2, 0, 0, 2, 2, -38, 0, 16, 0 },
            { 0, 0, 2, 2, 2, -31, 0, 13, 0 },
            { 0, 0, 2, 0, 0, 29, 0, 0, 0 },
            { -2, 0, 1, 2, 2, 29, 0, -12, 0 },
            { 0, 0, 0, 2, 0, 26, 0, 0, 0 },
            { -2, 0, 0, 2, 0, -22, 0, 0, 0 },
            { 0, 0, -1, 2, 1, 21, 0, -10, 0 },
            { 0, 2, 0, 0, 0, 17, -0.1, 0, 0 },
            { 2, 0, -1, 0, 1, 16, 0, -8, 0 },
            { -2, 2, 0, 2, 2, -16, 0.1, 7, 0 },
            { 0, 1, 0, 0, 1, -15, 0, 9, 0 },
            { -2, 0, 1, 0, 1, -13, 0, 7, 0 },
            { 0, -1, 0, 0, 1, -12, 0, 6, 0 },
            { 0, 0, 2, -2, 0, 11, 0, 0, 0 },
            { 2, 0, -1, 2, 1, -10, 0, 5, 0 },
            { 2, 0, 1, 2, 2, -8, 0, 3, 0 },
            { 0, 1, 0, 2, 2, 7, 0, -3, 0 },
            { -2, 1, 1, 0, 0, -7, 0, 0, 0 },
            { 0, -1, 0, 2, 2, -7, 0, 3, 0 },
            { 2, 0, 0, 2, 1, -7, 0, 3, 0 },
            { 2, 0, 1, 0, 0, 6, 0, 0, 0 },
            { -2, 0, 2, 2, 2, 6, 0, -3, 0 },
            { -2, 0, 1, 2, 1, 6, 0, -3, 0 },
            { 2, 0, -2, 0, 1, -6, 0, 3, 0 },
            { 2, 0, 0, 0, 1, -6, 0, 3, 0 },
            { 0, -1, 1, 0, 0, 5, 0, 0, 0 },
            { -2, -1, 0, 2, 1, -5, 0, 3, 0 },
            { -2, 0, 0, 0, 1, -5, 0, 3, 0 },
            { 0, 0, 2, 2, 1, -5, 0, 3, 0 },
            { -2, 0, 2, 0, 1, 4, 0, 0, 0 },
            { -2, 1, 0, 2, 1, 4, 0, 0, 0 },
            { 0, 0, 1, -2, 0, 4, 0, 0, 0 },
            { -1, 0, 1, 0, 0, -4, 0, 0, 0 },
            { -2, 1, 0, 0, 0, -4, 0, 0, 0 },
            { 1, 0, 0, 0, 0, -4, 0, 0, 0 },
            { 0, 0, 1, 2, 0, 3, 0, 0, 0 },
            { 0, 0, -2, 2, 2, -3, 0, 0, 0 },
            { -1, -1, 1, 0, 0, -3, 0, 0, 0 },
            { 0, 1, 1, 0, 0, -3, 0, 0, 0 },
            { 0, -1, 1, 2, 2, -3, 0, 0, 0 },
            { 2, -1, -1, 2, 2, -3, 0, 0, 0 },
            { 0, 0, 3, 2, 2, -3, 0, 0, 0 },
            { 2, -1, 0, 2, 2, -3, 0, 0, 0 }
    };

    @Override
    public org.mahefa.data.meeus.jean.Nutation find(double t) {
        /**
         * Required terms:
         * D: Mean elongation of the Moon from the Sun
         * M: Mean anomaly of the Sun (Earth)
         * M': Mean anomaly of the Moon
         * F: Moon's argument of latitude
         * Ω: Longitude of the ascending node of the Moon's mean orbit on the ecliptic
         */
        final double[] coefficientD = { 297.85036, 445267.111480, -0.0019142, (double) 1/189474 };
        final double[] coefficientM = { 357.52772, 35999.050340, -0.0001603, (double) -1/300000 };
        final double[] coefficientM1 = { 134.96298, 477198.867398, 0.0086972, (double) 1/56250 };
        final double[] coefficientF = { 93.27191, 483202.017538, -0.0036825, (double) 1/327270 };
        final double[] coefficientΩ = { 125.04452, -1934.136261, 0.0020708, (double) 1/450000 };

        final double D = Angle.normalize(AstroMath.horner(t, coefficientD));
        final double M = Angle.normalize(AstroMath.horner(t, coefficientM));
        final double M1 = Angle.normalize(AstroMath.horner(t, coefficientM1));
        final double F = Angle.normalize(AstroMath.horner(t, coefficientF));
        final double Ω = Angle.normalize(AstroMath.horner(t, coefficientΩ));

        double Δψ = 0;
        double Δε = 0;

        for(int i = 0; i < PERIODIC_TERMS.length; i++) {
            final double[] row = PERIODIC_TERMS[i];
            final double arg = (row[0] * Math.toRadians(D))
                    + (row[1] * Math.toRadians(M))
                    + (row[2] * Math.toRadians(M1))
                    + (row[3] * Math.toRadians(F))
                    + (row[4] * Math.toRadians(Ω));

            // The unit is 0".0001
            Δψ += ((row[5] + (row[6] * t)) * Math.sin(arg) / 1e4);
            Δε += ((row[7] + (row[8] * t)) * Math.cos(arg) / 1e4);
        }

        return new org.mahefa.data.meeus.jean.Nutation(Angle.dmsToRadian(0,0, Δψ), Angle.dmsToRadian(0,0, Δε));
    }

    @Override
    public double meanObliquityIAU(double t) {
        final double[] polynomialTerms = {
                Angle.dmsToRadian(23, 26, 21.448),
                Angle.dmsToRadian(0, 0, 46.8150),
                Angle.dmsToRadian(0, 0, 0.00059),
                Angle.dmsToRadian(0, 0, 0.001813)
        };

        return AstroMath.horner(t, polynomialTerms);
    }

    @Override
    public double meanObliquityLaskar(double t) throws Exception {
        final double U = t * 1e-2;

        if(U >= 1)
            throw new Exception("date not valid");

        final double[] polynomialTerms = {
                Angle.dmsToRadian(23, 26, 21.448),
                Angle.dmsToRadian(0, 0, -4680.93),
                Angle.dmsToRadian(0, 0, -1.55),
                Angle.dmsToRadian(0, 0, 1999.25),
                Angle.dmsToRadian(0, 0, -51.38),
                Angle.dmsToRadian(0, 0, -249.67),
                Angle.dmsToRadian(0, 0, -39.05),
                Angle.dmsToRadian(0, 0, 7.12),
                Angle.dmsToRadian(0, 0, 27.87),
                Angle.dmsToRadian(0, 0, 5.79),
                Angle.dmsToRadian(0, 0, 2.45)
        };

        return AstroMath.horner(U, polynomialTerms);
    }
}