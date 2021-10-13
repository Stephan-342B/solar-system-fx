package org.mahefa.common.utils.calendar.julian_day;

public final class JulianDay {

    /**
     * Calculate the Julian date
     * Valid only from March 1900 to February 2100
     *
     * @param y year
     * @param m month
     * @param d date
     *
     * @return julian day
     */
    public static double getJulianDay(int y, int m, double d) {
        return JulianDayImpl.getJulianDay(y, m, d);
    }

    /**
     *
     * @param y
     * @param m
     * @param d
     * @param h
     * @param mm
     * @param s
     * @return
     */
    public static double getJulianDay(int y, int m, int d, int h, int mm, int s) {
        return JulianDayImpl.getJulianDay(y, m, d, h, mm, s);
    }

    /**
     * Calculate T, time in Julian centuries of 36525 ephemeris days from the epoch 2000 January 1.5
     *
     * @param JD is the Julian Day
     *
     * @return time measured in Julian centuries of 36525
     */
    public static double inJulianCenturies(final double JD) {
        return JulianDayImpl.inJulianCenturies(JD);
    }

    /**
     *
     * @param JDE is the Julian Day
     * @return
     */
    public static double inJulianMillennia(final double JDE) {
        return JulianDayImpl.inJulianMillennia(JDE);
    }

    /**
     *
     * @param t
     * @return
     */
    public static double getJulianEphemerisDay(final double t) {
        return JulianDayImpl.getJulianEphemerisDay(t);
    }
}
