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
     * @param planet is the celestial body
     * @param t is the julian date
     * @return
     */
    GeocentricCoordinate getMajorPlanetCoord(String planet, double t) throws Exception;

}
