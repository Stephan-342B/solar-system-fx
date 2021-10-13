package org.mahefa.service.application.meeus.jean.algorithm.planetary_position;

import javafx.scene.Node;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.meeus.jean.Coordinates;

public interface PlanetaryPositionAppService {

    Node minorPlanet(OrbitalCharacteristic orbitalCharacteristic, double radius, double axialTilt, String designation);
    Coordinates majorPlanet(CelestialBody celestialBody, double t);

}
