package org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position;

import javafx.scene.Node;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.meeus.jean.HeliocentricCoordinate;

public interface PlanetaryPositionAppService {

    Node minorPlanet(OrbitalCharacteristic orbitalCharacteristic, double radius, double axialTilt, String designation);

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
    HeliocentricCoordinate getHeliocentricCoordinates(String designation, double t) throws Exception;

}
