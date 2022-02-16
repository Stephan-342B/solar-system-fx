package org.mahefa.data.meeus.jean;

public class Coordinate {

    /**
     * Coordinates x, y and z are stored in astronomical units
     */
    private Double x = 0d;
    private Double y = 0d;
    private Double z = 0d;

    /**
     * Stored in decimal, minute, second
     */
    private Double rightAscension = 0d;
    private Double declination = 0d;

    public Coordinate() {}

    public Coordinate(Double rightAscension, Double declination) {
        this.rightAscension = rightAscension;
        this.declination = declination;
    }

    public Coordinate(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
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