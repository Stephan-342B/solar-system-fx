package org.mahefa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.data.meeus.jean.Nutation;
import org.mahefa.service.application.meeus.jean.algorithm.nutations.NutationAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NutationTest {

    @Autowired
    NutationAppService nutationAppService;

    private double t;

    @Before
    public void init() {
        final LocalDateTime localDateTime = LocalDateTime.of(1987, 4, 10, 0, 0, 0);
        final double JDE = JulianDay.getJulianDay(
                localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        this.t = JulianDay.inJulianCenturies(JDE);
    }

    @Test
    public void checkNutation() {
        final Nutation nutation = nutationAppService.find(t);
        final double Δψ = AstroMath.round(nutation.getLongitude(), 1e6);
        final double Δε = AstroMath.round(nutation.getObliquity(), 1e6);
        final double rΔψ = AstroMath.round(Angle.dmsToRadian(0, 0, -3.788), 1e6);
        final double rΔε = AstroMath.round(Angle.dmsToRadian(0, 0, 9.443), 1e6);

        Assert.assertTrue(Δψ == rΔψ && Δε == rΔε);
    }

    @Test
    public void checkMeanObliquityLaskar() {
        try {
            final double ε0 = AstroMath.round(nutationAppService.meanObliquityLaskar(t), 1e6);
            final double rε0 = Angle.toDecimalDegree(23, 26, 27.407);

            Assert.assertTrue(ε0 == AstroMath.round(Math.toRadians(rε0), 1e6));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
