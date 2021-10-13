package org.mahefa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * See Meeus, Jean. Astronomical Algorithms. Richmond, Virg.: Willmann-Bell,
 *         2009. 62. Print.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JulianDayTest {

    @Test
    public void checkJulianDate1() {
        Assert.assertTrue(JulianDay.getJulianDay(2000, 1, 1.5) == 2451545.0);
    }

    @Test
    public void checkJulianDate2() {
        Assert.assertTrue(JulianDay.getJulianDay(1987, 1, 27.0) == 2446822.5);
    }

    @Test
    public void checkJulianDate3() {
        Assert.assertTrue(JulianDay.getJulianDay(1987, 6, 19.5) == 2446966.0);
    }

    @Test
    public void checkJulianDate4() {
        Assert.assertTrue(JulianDay.getJulianDay(1988, 1, 27.0) == 2447187.5);
    }

    @Test
    public void checkJulianDate5() {
        Assert.assertTrue(JulianDay.getJulianDay(1988, 6, 19.5) == 2447332.0);
    }

    @Test
    public void checkJulianDate6() {
        Assert.assertTrue(JulianDay.getJulianDay(1600, 1, 1.0) == 2305447.5);
    }

    @Test
    public void checkJulianDate7() {
        Assert.assertTrue(JulianDay.getJulianDay(1600, 12, 31.0) == 2305812.5);
    }

    @Test
    public void checkJulianDate8() {
        Assert.assertTrue(JulianDay.getJulianDay(837, 4, 10.3) == 2026871.8);
    }

    @Test
    public void checkJulianDate9() {
        Assert.assertTrue(JulianDay.getJulianDay(-1000, 7, 12.5) == 1356001.0);
    }

    @Test
    public void checkJulianDate10() {
        Assert.assertTrue(JulianDay.getJulianDay(-1000, 2, 29.0) == 1355866.5);
    }

    @Test
    public void checkJulianDate11() {
        Assert.assertTrue(JulianDay.getJulianDay(-1001, 8, 17.9) == 1355671.4);
    }

    @Test
    public void checkJulianDate12() {
        Assert.assertTrue(JulianDay.getJulianDay(-4712, 1, 1.5) == 0.0);
    }
}

