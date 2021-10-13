package org.mahefa.service.application.meeus.jean.algorithm.coordinates;

import org.mahefa.data.meeus.jean.Coordinates;

public interface CoordinatesAppService {

    /**
     *
     * Calculate the heliocentric coordinates of the planet.
     * The computation is based on the following elements L, B, R
     * such as:
     * L: is the ecliptical longitude
     * B: is the ecliptical latitude
     * R: is the radius vector (= distance to the Sun)
     *
     * The correction for the light-time and the aberration has been done
     * at once by calculating the coordinates at the instant T - t (p Planet and Earth)
     *
     * @param designation is the planet's name
     * @param t is the julian date in centuries
     *
     * @return
     *
     * @throws Exception
     */
    Coordinates findHeliocentricCoordinates(String designation, double t) throws Exception;
}