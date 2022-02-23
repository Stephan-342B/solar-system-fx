package org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position.vsop87;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.data.meeus.jean.GeocentricCoordinate;
import org.mahefa.data.meeus.jean.Vsop87Element;
import org.mahefa.service.application.astro.meeus.jean.algorithm.nutations.Nutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Vsop87Impl implements Vsop87 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vsop87Impl.class);

    @Autowired Nutation nutation;

    @Value("${periodic.term.location.file}")
    String periodicTermLocationFile;

    static List<Vsop87Element> vsop87Elements;

    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        try(InputStream inputStream = Vsop87Impl.class.getResourceAsStream(periodicTermLocationFile)) {
            vsop87Elements = objectMapper.readValue(inputStream, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, Vsop87Element.class));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            vsop87Elements = new ArrayList<>();
        }
    }

    @Override
    public GeocentricCoordinate getMajorPlanetCoord(String planet, double t) throws Exception {
        final Vector3D vector3d = getHeliocentricEclipticCoord(planet, t);
        final double x = vector3d.getX();
        final double y = vector3d.getY();
        final double z = vector3d.getZ();

        // Calculate the longitude and latitude
        double λ = Math.atan2(y, x);
        double β = Math.atan2(z, Math.sqrt((x * x) + (y * y)));

        // Corrections for reduction to the FK5
        final double[] polynomials = { λ, Math.toRadians(-1.397), -0.00031 };
        final double l = AstroMath.horner(t, polynomials);
        final double Δλ = 0
                + (Angle.dmsToRadian(0, 0, -0.09033))
                + (Angle.dmsToRadian(0, 0, 0.03916) * (Math.cos(l) + Math.sin(l)) * Math.tan(β));
        final double Δβ = Angle.dmsToRadian(0, 0, 0.03916) * (Math.cos(l) - Math.sin(l));

        λ += Δλ;
        β += Δβ;

        // Nutation in longitude and obliquity
        final org.mahefa.data.meeus.jean.Nutation nutation = this.nutation.find(t);
        final double Δψ = nutation.getLongitude();
        final double Δε = nutation.getObliquity();
        final double ε0 = this.nutation.meanObliquityLaskar(t);
        final double ε = ε0 + Δε;

        λ += Δψ;

        /**
         * Transform from ecliptical to geocentric coordinates
         * Set apparent right ascension and declination
         */
        final double α = Math.atan2(((Math.sin(λ) * Math.cos(ε)) - (Math.tan(β) * Math.sin(ε))), Math.cos(λ));
        final double δ = Math.asin((Math.sin(β) * Math.cos(ε)) + (Math.cos(β) * Math.sin(ε) * Math.sin(λ)));

        return new GeocentricCoordinate(
                (Angle.normalize(Math.toDegrees(AstroMath.round(α, 1e6))) / 15),
                Math.toDegrees(AstroMath.round(δ, 1e6))
        );
    }

    @Override
    public double getRTS(String planet) {
        return 0;
    }

    Vector3D getHeliocentricEclipticCoord(String planet, double t) throws Exception {
        if(planet.equalsIgnoreCase("earth"))
            throw new Exception("Cannot compute heliocentric position using this method");

        final Vsop87Element vsop87Element = findPeriodicTerm(planet);
        final double JDE = JulianDay.getJulianEphemerisDay(t);

        // Convert time in julian centuries to julian millennia
        t *= 1e-1;

        double L = AstroMath.computeTerm(vsop87Element.getL(), t);
        double B = AstroMath.computeTerm(vsop87Element.getB(), t);
        double R = AstroMath.computeTerm(vsop87Element.getR(), t);

        final Vsop87Element earthVsop87Element = findPeriodicTerm("earth");

        // Calculates the Earth's coordinates
        double L0 = AstroMath.computeTerm(earthVsop87Element.getL(), t);
        double B0 = AstroMath.computeTerm(earthVsop87Element.getB(), t);
        double R0 = AstroMath.computeTerm(earthVsop87Element.getR(), t);

        // Combine value
        Vector3D vector3D = AstroMath.getCoordinates(L, B, R, L0, B0, R0);

        // Correct effect of light-time and aberration
        final double Δ = AstroMath.distanceToEarth(vector3D.getX(), vector3D.getY(), vector3D.getZ());
        final double τ = 0.0057755183 * Δ;
        final double instant = JulianDay.inJulianMillennia(JDE - τ);

        // Recalculate at T - τ
        L = AstroMath.computeTerm(vsop87Element.getL(), instant);
        B = AstroMath.computeTerm(vsop87Element.getB(), instant);
        R = AstroMath.computeTerm(vsop87Element.getR(), instant);
        L0 = AstroMath.computeTerm(earthVsop87Element.getL(), instant);
        B0 = AstroMath.computeTerm(earthVsop87Element.getB(), instant);
        R0 = AstroMath.computeTerm(earthVsop87Element.getR(), instant);

        return AstroMath.getCoordinates(L, B, R, L0, B0, R0);
    }

    Vsop87Element findPeriodicTerm(String designation) throws Exception {
        Optional<Vsop87Element> optionalVsop87PeriodicTerm = vsop87Elements.stream()
                .filter(term -> term.getLabel().equalsIgnoreCase(designation))
                .findFirst();

        if(!optionalVsop87PeriodicTerm.isPresent())
            throw new Exception("No periodic terms not found for the given planet: " + designation);

        return optionalVsop87PeriodicTerm.get();
    }
}
