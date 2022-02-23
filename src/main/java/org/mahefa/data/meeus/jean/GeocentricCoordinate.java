package org.mahefa.data.meeus.jean;

public class GeocentricCoordinate {

    private Double rightAscension = 0d;
    private Double declination = 0d;

    public GeocentricCoordinate(Double rightAscension, Double declination) {
        this.rightAscension = rightAscension;
        this.declination = declination;
    }

    public Double getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(Double rightAscension) {
        this.rightAscension = rightAscension;
    }

    public Double getDeclination() {
        return declination;
    }

    public void setDeclination(Double declination) {
        this.declination = declination;
    }
}
