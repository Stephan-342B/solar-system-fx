package org.mahefa.data.meeus.jean;

public class Coordinates {

    private double x = 0;
    private double y = 0;
    private double z = 0;

    private double Δ;
    private double τ;

    private double rightAscension;
    private double declination;

    private double L;
    private double B;
    private double R;

    public Coordinates() {
    }

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getΔ() {
        return Δ;
    }

    public void setΔ(double Δ) {
        Δ = Δ;
    }

    public double getτ() {
        return τ;
    }

    public void setτ(double τ) {
        this.τ = τ;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(double rightAscension) {
        this.rightAscension = rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    public void setDeclination(double declination) {
        this.declination = declination;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }
}