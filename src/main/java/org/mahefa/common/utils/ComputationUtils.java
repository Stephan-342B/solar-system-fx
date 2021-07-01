package org.mahefa.common.utils;

public abstract class ComputationUtils {

    /**
     * Universal Gravitational Constant: 6.67*10^{-11} N-m^2/kg^2
     */
    private static final double G = 6.67e-11;

    /**
     * Time
     * Day in second = 1[day] * 24[hr/day] * 60[min/hr] * 60[sec/min]
     */
    private static final double T = 1 * 24 * 60 * 60;

    /**
     * Astronomical Unit conversion
     */
    private static final double ASTRONOMICAL_UNIT = 6.68459e-9;

    public static double orbitalPeriod(final double semiMajorAxis, double mass1, double... masses) {
        return 2 * Math.PI * Math.sqrt(Math.pow(semiMajorAxis, 3) / standardGravitationalParameter(mass1, masses));
    }

    /**
     * Using kepler's third law
     * P^2 = a^3
     *
     * with a: semi major-axis
     *
     * @param semiMajorAxis
     * @return
     */
    public static double orbitalPeriod(final double semiMajorAxis) {
        double semiMajorAxisInAU = semiMajorAxis * ASTRONOMICAL_UNIT;

        return Math.sqrt(Math.pow(semiMajorAxisInAU, 3) );
    }

    public static double orbitalPeriodTwoBodie(double a, final double M1, final double M2) {
        return 2 * Math.PI * Math.sqrt(Math.pow(a, 3) / G * (M1 + M2));
    }

    /**
     * v = 2 * PI * r / t
     *
     * r is the orbital radius
     * t is the time period
     *
     * @param semiMajorAxis
     * @param orbitalPeriod
     * @return
     */
    public static double orbitalSpeed(final double semiMajorAxis, final double orbitalPeriod) {
        return 2 * Math.PI * (semiMajorAxis / orbitalPeriod);
    }

    public static double orbitalEccentricity(double perihelion, double aphelion) {
        return aphelion - perihelion / aphelion + perihelion;
    }

    public static double eccentricity(double semiMajorAxis, double semiMinorAxis) {
        return Math.sqrt(1 - (Math.pow(semiMinorAxis, 2) / Math.pow(semiMajorAxis, 2)));
    }

    private static double standardGravitationalParameter(double mass1, double... masses) {
        double totalMass = mass1 * 0.001;

        for(double mass : masses) {
            totalMass += (mass * 0.001);
        }

        return G * totalMass;
    }

    public static double semiMajorAxis(double mass, double orbitalPeriod) {
        final double GM = standardGravitationalParameter(mass);
        return Math.cbrt(
                (Math.pow(orbitalPeriod, 2) * GM) / (4 * Math.pow(Math.PI, 2))
        );
    }

    public static double arithmeticMean(double value1, double... values) {
        for(int v = 0; v < values.length; v++) {
            value1 += values[v];
        }

        return value1 / (values.length + 1);
    }

    public static double geometricMean(double value1, double... values) {
        double total = value1;

        for(double value : values) {
            total *= value;
        }

        return Math.sqrt(total);
    }

    /**
     * velocity =  D / T
     * D = distance traveled = circumference of a circle
     * T = time
     *
     * @param radius (km)
     * @param rotationPeriod (day)
     *
     * @return
     */
    public static double rotationVelocity(double radius, double rotationPeriod) {
        final double D = 2 * Math.PI * radius;


        return D / (T * rotationPeriod);
    }

    /**
     * A planet P perform a complete rotation, about 360, in one rotation period
     *
     * We need to figure out how many degree a planet do in an hour
     * So, we split a planet into 24 parts
     * then calculate Rotation Period / 24
     *
     * @return
     */
    public static double rotationDegree(double period) {
        final double value = (period == -243.025) ? period + 360 : period;
        return 360 / value * 0.01;
    }

    /**
     * 1/s = 1 - 1/p
     * s = 1 / [1 - 1/p]
     *
     * @param siderialPeriod
     * @return
     */
    public static double synedicPeriod(double siderialPeriod) {
        return 1 / (1 - (1 / siderialPeriod));
    }
}