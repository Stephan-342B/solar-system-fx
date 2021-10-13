package org.mahefa.service.application.meeus.jean.algorithm.planetary_position;

import javafx.scene.Node;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.meeus.jean.Coordinates;
import org.mahefa.data.meeus.jean.Nutation;
import org.mahefa.service.application.meeus.jean.algorithm.coordinates.CoordinatesAppService;
import org.mahefa.service.application.meeus.jean.algorithm.nutations.NutationAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlanetaryPositionAppServiceImpl implements PlanetaryPositionAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetaryPositionAppServiceImpl.class);

    @Autowired
    CoordinatesAppService coordinatesAppService;

    @Autowired
    NutationAppService nutationAppService;

    @Override
    public Node minorPlanet(OrbitalCharacteristic orbitalCharacteristic, double radius, double axialTilt, String designation) {
        final LocalDateTime localDateTime = LocalDateTime.of(1990, 4, 19, 0, 0, 0);
        final double JDE = JulianDay.getJulianDay(
                localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        final double T = JulianDay.inJulianCenturies(JDE);

        // Next we compute the mean orbital elements of the planet on a given date
        final double L = Angle.normalize(AstroMath.horner(T, orbitalCharacteristic.getMeanLongitudes()));
        final double a = AstroMath.horner(T, orbitalCharacteristic.getSemiMajorAxis());
        final double e = AstroMath.horner(T, orbitalCharacteristic.getEccentricities());
        final double i = Angle.normalize(AstroMath.horner(T, orbitalCharacteristic.getInclinations()));
        final double N = Angle.normalize(AstroMath.horner(T, orbitalCharacteristic.getLongitudeAscendingNodes()));
        final double wl = Angle.normalize(AstroMath.horner(T, orbitalCharacteristic.getLongitudesPerihelion()));

        // Mean anomaly is given by: M = L - wl
        final double M = Angle.normalize(L - wl);

        // Argument of perihelion is given by: w = wl - N
        final double w = Angle.normalize(wl - N);

        // Mean motion: n = 0.9856076686/a*sqrt(a)
        final double n = Angle.normalize(0.9856076686 / (a * Math.sqrt(a)));

        // Eccentricity anomaly E
        final double E = AstroMath.getEccentricAnomaly(e, Math.toRadians(M));

        // TODO

        return null;
    }

    @Override
    public Coordinates majorPlanet(CelestialBody celestialBody, double t) {
        try {
            // Find heliocentric coordinates
            final Coordinates coordinates = coordinatesAppService.findHeliocentricCoordinates(celestialBody.getDesignation(), t);

            final double x = coordinates.getX();
            final double y = coordinates.getY();
            final double z = coordinates.getZ();

            // Calculate the longitude and latitude
            double λ = Math.atan2(y, x);
            double β = Math.atan2(z, Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2))));

            // Corrections for reduction to the FK5
            final double[] polynomials = { λ, Math.toRadians(-1.397), -0.00031 };
            final double l = AstroMath.horner(t, polynomials);
            final double Δλ = 0 +
                    (Angle.dmsToRadian(0, 0, -0.09033)) +
                    (Angle.dmsToRadian(0, 0, 0.03916) * (Math.cos(l) + Math.sin(l)) * Math.tan(β));
            final double Δβ = Angle.dmsToRadian(0, 0, 0.03916) * (Math.cos(l) - Math.sin(l));

            λ += Δλ;
            β += Δβ;

            // Nutation in longitude and obliquity
            final Nutation nutation = nutationAppService.find(t);
            final double Δψ = nutation.getLongitude();
            final double Δε = nutation.getObliquity();
            final double ε0 = nutationAppService.meanObliquityLaskar(t);
            final double ε = ε0 + Δε;

            λ += Δψ;

            /**
             * Transform from ecliptical to geocentric coordinates
             * Set apparent right ascension and declination
             */
            final double α = Math.atan2(((Math.sin(λ) * Math.cos(ε)) - (Math.tan(β) * Math.sin(ε))), Math.cos(λ));
            final double δ = Math.asin((Math.sin(β) * Math.cos(ε)) + (Math.cos(β) * Math.sin(ε) * Math.sin(λ)));

            coordinates.setRightAscension(Angle.normalize(Math.toDegrees(AstroMath.round(α, 1e6))) / 15);
            coordinates.setDeclination(Math.toDegrees(AstroMath.round(δ, 1e6)));

            return coordinates;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("An error occurred while calculating the planet's position", e);
        }

        return new Coordinates();
    }
}
