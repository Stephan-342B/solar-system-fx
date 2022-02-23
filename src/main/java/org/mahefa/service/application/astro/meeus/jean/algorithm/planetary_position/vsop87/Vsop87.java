package org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position.vsop87;

import org.mahefa.data.meeus.jean.GeocentricCoordinate;

public interface Vsop87 {

    /**
     * Based on the VSOP87 periodic terms, this method calculate the apparent right ascension
     * and declination of a major planet for a given instant.
     * The geocentric ecliptical longitude and latitude of a major planet (Mercury to Neptune)
     * are obtained from the heliocentric ecliptical coordinates (L, B et R) of the planet and of the Earth.
     *
     * L: is the ecliptical longitude
     * B: is the ecliptical latitude
     * R: is the radius vector (distance to the Sun)
     *
     * The correction for the light-time and the aberration has been done
     * at once by calculating the coordinates at the instant T - t (p Planet and Earth)
     *
     * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
     *          2009. 214. Print.
     *
     * @param planet is the celestial body
     * @param t is the julian date
     * @return the geocentric ecliptical longitude and latitude
     */
    GeocentricCoordinate getMajorPlanetCoord(String planet, double t) throws Exception;

    /**
     * Rising, Transit and Setting
     *
     * The interpolation formula is:
     * y = y2 + n/2(a + b + nc)
     *
     * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
     *          2009. 102. Print.
     *
     * @param planet
     * @return
     */
    double getRTS(String planet);

}
