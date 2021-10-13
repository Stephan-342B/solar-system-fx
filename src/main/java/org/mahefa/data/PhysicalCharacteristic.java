package org.mahefa.data;

public class PhysicalCharacteristic {

    private final double radius;
    private final double mass;
    private final double axialTilt;
    private final boolean isRingSystem;

    private PhysicalCharacteristic(Builder builder) {
        this.radius = builder.radius;
        this.mass = builder.mass;
        this.axialTilt = builder.axialTilt;
        this.isRingSystem = builder.isRingSystem;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getAxialTilt() {
        return axialTilt;
    }

    public boolean isRingSystem() {
        return isRingSystem;
    }

    public static class Builder {
        private final double radius;
        private final double mass;
        private double axialTilt;
        private boolean isRingSystem = false;

        public Builder(double radius, double mass) {
            this.radius = radius;
            this.mass = mass;
        }

        public Builder setAxialTilt(double axialTilt) {
            this.axialTilt = axialTilt;
            return this;
        }

        public Builder isRingSystem(boolean isRingSystem) {
            this.isRingSystem = isRingSystem;
            return this;
        }

        public PhysicalCharacteristic build() {
            return new PhysicalCharacteristic(this);
        }
    }
}
