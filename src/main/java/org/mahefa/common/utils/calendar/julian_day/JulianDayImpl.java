package org.mahefa.common.utils.calendar.julian_day;

public final class JulianDayImpl {

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

    public static double getJulianDay(int y, int m, int d, int h, int mm, int s) {
        double day = d;
        double hour = h;

        if(mm > 0) {
            hour += (mm / 60);
        }

        if(s > 0) {
            hour += ((s / 60) / 60);
        }

        if(h > 0) {
            day += (hour / 24);
        }

        return getJulianDay(y, m, day);
    }

    public static double inJulianCenturies(final double JDE) {
        return (JDE - getJ2000()) / 36525;
    }

    public static double inJulianMillennia(final double JDE) {
        return (JDE - getJ2000()) / 365250;
    }

    public static double getJulianEphemerisDay(double t) {
        return (36525 * t) + getJ2000();
    }

    static double getJ2000() {
        return getJulianDay(2000, 1, 1.5);
    }
}
