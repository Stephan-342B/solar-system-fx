package org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.meeus.jean.HeliocentricCoordinate;
import org.mahefa.data.meeus.jean.Vsop87PeriodicTerm;
import org.mahefa.service.application.astro.meeus.jean.algorithm.nutations.Nutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetaryPositionAppServiceImpl implements PlanetaryPositionAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetaryPositionAppServiceImpl.class);

    @Autowired
    Nutation nutation;

    @Value("${periodic.term.location.file}")
    String periodicTermLocationFile;

    static List<Vsop87PeriodicTerm> vsop87PeriodicTerms;

    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        try(InputStream inputStream = PlanetaryPositionAppServiceImpl.class.getResourceAsStream(periodicTermLocationFile)) {
            vsop87PeriodicTerms = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Vsop87PeriodicTerm.class));
        } catch (IOException e) {
            e.printStackTrace();
            vsop87PeriodicTerms = new ArrayList<>();
        }
    }

    @Override
    public HeliocentricCoordinate getHeliocentricCoordinates(String designation, double t) throws Exception {
        if(designation.equalsIgnoreCase("earth"))
            throw new Exception("Cannot compute heliocentric position using this method");

        final Vsop87PeriodicTerm vsop87PeriodicTerm = findPeriodicTerm(designation);
        final double JDE = JulianDay.getJulianEphemerisDay(t);

        // Convert time in julian centuries to julian millennia
        t *= 1e-1;

        double L = AstroMath.computeTerm(vsop87PeriodicTerm.getL(), t);
        double B = AstroMath.computeTerm(vsop87PeriodicTerm.getB(), t);
        double R = AstroMath.computeTerm(vsop87PeriodicTerm.getR(), t);

        final Vsop87PeriodicTerm earthVsop87PeriodicTerm = findPeriodicTerm("earth");

        // Calculates the Earth's coordinates
        double L0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getL(), t);
        double B0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getB(), t);
        double R0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getR(), t);

        // Combine value
        Vector3D vector3D = AstroMath.getCoordinates(L, B, R, L0, B0, R0);

        // Correct effect of light-time and aberration
        final double Δ = AstroMath.distanceToEarth(vector3D.getX(), vector3D.getY(), vector3D.getZ());
        final double τ = 0.0057755183 * Δ;
        final double instant = JulianDay.inJulianMillennia(JDE - τ);

        // Recalculate at T - τ
        L = AstroMath.computeTerm(vsop87PeriodicTerm.getL(), instant);
        B = AstroMath.computeTerm(vsop87PeriodicTerm.getB(), instant);
        R = AstroMath.computeTerm(vsop87PeriodicTerm.getR(), instant);
        L0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getL(), instant);
        B0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getB(), instant);
        R0 = AstroMath.computeTerm(earthVsop87PeriodicTerm.getR(), instant);

        vector3D = AstroMath.getCoordinates(L, B, R, L0, B0, R0);

        return new HeliocentricCoordinate(vector3D.getX(), vector3D.getY(), vector3D.getZ());
    }

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



    Vsop87PeriodicTerm findPeriodicTerm(String designation) throws Exception {
        Optional<Vsop87PeriodicTerm> optionalPeriodicTerm = vsop87PeriodicTerms.stream().filter(term -> term.getLabel().equalsIgnoreCase(designation)).findFirst();

        if(!optionalPeriodicTerm.isPresent()) {
            throw new Exception("Periodic terms not found");
        }

        return optionalPeriodicTerm.get();
    }
}
