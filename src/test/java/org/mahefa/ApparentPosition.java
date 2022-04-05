package org.mahefa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.data.meeus.jean.GeocentricCoordinate;
import org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position.vsop87.Vsop87;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApparentPosition {

    @Autowired
    Vsop87 vsop87;

    private double t;

    @Before
    public void init() {
        final LocalDateTime localDateTime = LocalDateTime.of(1992, 12, 20, 0, 0, 0);
        final double JDE = JulianDay.getJulianDay(
                localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        this.t = JulianDay.inJulianCenturies(JDE);
    }

    @Test
    public void venus() throws Exception {
        final GeocentricCoordinate geocentricCoordinate = vsop87.getMajorPlanetCoord("Venus", t);
//        Assert.assertTrue(
//                geocentricCoordinate.getRightAscension() == 21.078194
//                        && geocentricCoordinate.getDeclination() == -18.88802
//        );
    }
}
