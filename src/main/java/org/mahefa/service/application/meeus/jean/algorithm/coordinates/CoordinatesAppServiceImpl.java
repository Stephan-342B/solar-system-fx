package org.mahefa.service.application.meeus.jean.algorithm.coordinates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.data.meeus.jean.Coordinates;
import org.mahefa.data.meeus.jean.PeriodicTerm;
import org.mahefa.data.meeus.jean.Term;
import org.mahefa.service.business.galaxy.DataBusinessService;
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
public class CoordinatesAppServiceImpl implements CoordinatesAppService {

    @Autowired
    DataBusinessService dataBusinessService;

    @Value("${periodic.term.location.file}")
    String periodicTermLocationFile;

    static List<PeriodicTerm> periodicTerms;

    @PostConstruct
    private void init() {
        ObjectMapper objectMapper = new ObjectMapper();

        try(InputStream inputStream = CoordinatesAppServiceImpl.class.getResourceAsStream(periodicTermLocationFile)) {
            periodicTerms = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, PeriodicTerm.class));
        } catch (IOException e) {
            e.printStackTrace();
            periodicTerms = new ArrayList<>();
        }
    }

    @Override
    public Coordinates findHeliocentricCoordinates(String designation, double t) throws Exception {
        final PeriodicTerm periodicTerm = findPeriodicTerm(designation);
        final double JDE = JulianDay.getJulianEphemerisDay(t);
        Coordinates coordinates = new Coordinates();

        // Convert time in julian centuries to julian millennia
        t *= 1e-1;

        double L = computeTerm(periodicTerm.getL(), t);
        double B = computeTerm(periodicTerm.getB(), t);
        double R = computeTerm(periodicTerm.getR(), t);

        final PeriodicTerm earthPeriodicTerm = findPeriodicTerm("earth");

        // Calculates the Earth's coordinates
        double L0 = computeTerm(earthPeriodicTerm.getL(), t);
        double B0 = computeTerm(earthPeriodicTerm.getB(), t);
        double R0 = computeTerm(earthPeriodicTerm.getR(), t);

        // Combine value
        coordinates = getCoordinates(L, B, R, L0, B0, R0);

        // Correct effect of light-time and aberration
        final double Δ = AstroMath.distanceToEarth(coordinates.getX(), coordinates.getY(), coordinates.getZ());
        final double τ = 0.0057755183 * Δ;
        final double instant = JulianDay.inJulianMillennia(JDE - τ);

        // Recalculate at T - τ
        L = computeTerm(periodicTerm.getL(), instant);
        B = computeTerm(periodicTerm.getB(), instant);
        R = computeTerm(periodicTerm.getR(), instant);
        L0 = computeTerm(earthPeriodicTerm.getL(), instant);
        B0 = computeTerm(earthPeriodicTerm.getB(), instant);
        R0 = computeTerm(earthPeriodicTerm.getR(), instant);

        coordinates = getCoordinates(L, B, R, L0, B0, R0);

        coordinates.setDesignation(designation);
        coordinates.setL(AstroMath.round(Angle.normalize(Math.toDegrees(L)), 1e6));
        coordinates.setB(AstroMath.round(Math.toDegrees(B), 1e6));
        coordinates.setB(AstroMath.round(R, 1e6));

        return coordinates;
    }

    /**
     * Calculate the value of each term by this formulae
     * A cos (b + Ct)
     *
     * the quantities B and C are expressed in radians. The coefficients A are in units of 10^~8 radian in the
     * case of the longitude and the latitude, in units of 10^8 astronomical unit for the radius vector.
     *
     * @param termList
     * @param t
     *
     * @return
     */
    double computeTerm(final List<Term> termList, final double t) {
        int i = 0;
        double[] rows = new double[termList.size()];

        for(Term term: termList) {
            final double length = term.getLength();
            double row = 0d;

            for(int j = 0; j < length; j++) {
                final double A = term.getA().get(j);
                final double B = term.getB().get(j);
                final double C = (term.getC().get(j) * t);

                row += (A * Math.cos(B + C));
            }

            rows[i] = row;
            i++;
        }

        return AstroMath.horner(t, rows) / Math.pow(10, 8);
    }

    PeriodicTerm findPeriodicTerm(String designation) throws Exception {
        Optional<PeriodicTerm> optionalPeriodicTerm = periodicTerms.stream().filter(term -> term.getDesignation().equalsIgnoreCase(designation)).findFirst();

        if(!optionalPeriodicTerm.isPresent()) {
            throw new Exception("Periodic terms not found");
        }

        return optionalPeriodicTerm.get();
    }

    Coordinates getCoordinates(final double L, final double B, final double R, final double L0, final double B0, final double R0) {
        final double x = (R * Math.cos(B) * Math.cos(L)) - (R0 * Math.cos(B0) * Math.cos(L0));
        final double y = (R * Math.cos(B) * Math.sin(L)) - (R0 * Math.cos(B0) * Math.sin(L0));
        final double z = (R * Math.sin(B)) - (R0 * Math.sin(B0));

        return new Coordinates(
                AstroMath.round(x, 1e6),
                AstroMath.round(y, 1e6),
                AstroMath.round(z, 1e6)
        );
    }
}
