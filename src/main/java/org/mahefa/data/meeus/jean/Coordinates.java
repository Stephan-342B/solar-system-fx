package org.mahefa.data.meeus.jean;

public class Coordinates {

    private String designation = "sun";

    private double x = 0d;
    private double y = 0d;
    private double z = 0d;

    private double Δ;
    private double τ;

    private double rightAscension = 0d;
    private double declination = 0d;

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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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