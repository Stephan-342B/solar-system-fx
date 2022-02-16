package org.mahefa.service.application.astro.meeus.jean.algorithm.nutations;

public interface Nutation {

    /**
     * Calculate the nutation in longitude and obliquity
     *
     * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
     *         2009. Chapter 21. Print.
     *
     * @param t is the date to find the nutation for
     */
    org.mahefa.data.meeus.jean.Nutation find(double t);

    /**
     * Calculate ε0 using the International Astronomical Union's adopted formulae
     *
     * By using this method, the error in ε0 reaches 1" over a period of 2000
     * years, and about 10" over a period of 4000 years from the epoch J2000
     *
     * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
     *         2009. 135. Print.
     *
     * @param t is the date to find ε0 for
     * @return
     */
    double meanObliquityIAU(double t);

    /**
     * By using this method the accuracy is estimated to be at 0".01 after 1000
     * years and a few arc seconds after 10,000 years on either side of the
     * epoch J2000. Also, this method is only valid over a period of 10,000 years
     * on either side of J2000 and will throw an exception if attempted on out
     * of range dates
     *
     * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
     *         2009. 135. Print.
     *
     * @param t is the date to find ε0 for
     *
     * @return
     */
    double meanObliquityLaskar(double t) throws Exception;

}
