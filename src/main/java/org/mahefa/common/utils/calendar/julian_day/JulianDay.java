package org.mahefa.common.utils.calendar.julian_day;

import java.time.LocalDateTime;

public final class JulianDay {

    /**
     * Julian date at:
     * year = 2000, month = 1, day = 1.5
     */
    static final double J2000 = 2451545.0;

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
        // Compute the julian date corresponding to January 0.0 of a given year
        if(m == 1 && d == 0.0) {
            y -= 1;

            if(y >= 1901 && y < 2100) {
                return 1721409.5 + ((int) 365.25 * y);
            }

            final int A = (y / 100);
            return ((int) 365.25 * y) - A + (A / 4) + 1721424.5;
        }

        int b = 0;

        if(m == 1 || m == 2) {
            y -= 1;
            m += 12;
        }

        if(!(y <= 1582)) {
            // Gregorian calendar
            final int A = (y / 100);
            b = 2 - A + (A / 4);
        }

        return ((int) (365.25 * (y + 4716))) + ((int) (30.6001 * (m + 1))) + d + b - 1524.5;
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static double getJulianDay(int year, int month, int day, int hour, int minute, int second) {
        if(minute > 0)
            hour += (minute / 60);

        if(second > 0)
            hour += ((second / 60) / 60);

        if(hour > 0)
            day += (hour / 24);

        return getJulianDay(year, month, day);
    }

    /**
     * Calculate T, time in Julian centuries of 36525 ephemeris days from the epoch 2000 January 1.5
     *
     * @param JDE is the Julian Day
     *
     * @return time measured in Julian centuries of 36525
     */
    public static double inJulianCenturies(final double JDE) {
        return (JDE - J2000) / 36525;
    }

    /**
     *
     * @param JDE is the Julian Day
     * @return
     */
    public static double inJulianMillennia(final double JDE) {
        return (JDE - J2000) / 365250;
    }

    /**
     *
     * @param t
     * @return
     */
    public static double getJulianEphemerisDay(final double t) {
        return (36525 * t) + J2000;
    }

    public static double getJDEAt(LocalDateTime... localDateTime) {
        LocalDateTime localDateTime1 = (localDateTime != null && localDateTime.length > 0 && localDateTime[0] != null)
                ? localDateTime[0] : LocalDateTime.now();
        final double JD = JulianDay.getJulianDay(
                localDateTime1.getYear(), localDateTime1.getMonth().getValue(), localDateTime1.getDayOfMonth(),
                localDateTime1.getHour(), localDateTime1.getMonthValue(), localDateTime1.getSecond()
        );

        return inJulianCenturies(JD);
    }
}
