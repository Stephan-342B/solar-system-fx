package org.mahefa.data.meeus.jean;

public class Nutation {

    private double longitude;
    private double obliquity;

    public Nutation(double longitude, double obliquity) {
        this.longitude = longitude;
        this.obliquity = obliquity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getObliquity() {
        return obliquity;
    }

    public void setObliquity(double obliquity) {
        this.obliquity = obliquity;
    }
}
