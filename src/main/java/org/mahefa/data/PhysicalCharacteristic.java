package org.mahefa.data;

import org.mahefa.common.utils.ComputationUtils;

public class PhysicalCharacteristic {

    private final double radius;
    private final double mass;
    private final double axialTilt;

    private final double rotationDegreePerSec;
    private double siderealRotationPeriod;
    private final double equatorialRotationVelocity;

    private PhysicalCharacteristic(Builder builder) {
        this.radius = builder.radius;
        this.mass = builder.mass;
        this.axialTilt = builder.axialTilt;

        this.rotationDegreePerSec = builder.rotationDegreePerSec;
        this.siderealRotationPeriod = builder.siderealRotationPeriod;
        this.equatorialRotationVelocity = builder.equatorialRotationVelocity;
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

    public double getRotationDegreePerSec() {
        return rotationDegreePerSec;
    }

    public double getSiderealRotationPeriod() {
        return siderealRotationPeriod;
    }

    public double getEquatorialRotationVelocity() {
        return equatorialRotationVelocity;
    }

    public static class Builder {

        private final double radius;
        private final double mass;
        private double axialTilt;

        private double rotationDegreePerSec;
        private double siderealRotationPeriod;
        private final double equatorialRotationVelocity;

        public Builder(double radius, double mass) {
            this.radius = radius;
            this.mass = mass;

            this.equatorialRotationVelocity = ComputationUtils.rotationVelocity(radius, siderealRotationPeriod);
        }

        public Builder setAxialTilt(double axialTilt) {
            this.axialTilt = axialTilt;
            return this;
        }

        public Builder setSiderealRotationPeriod(double siderealRotationPeriod) {
            this.siderealRotationPeriod = siderealRotationPeriod;
            this.rotationDegreePerSec = ComputationUtils.rotationDegree(siderealRotationPeriod);
            return this;
        }

        public PhysicalCharacteristic build() {
            return new PhysicalCharacteristic(this);
        }
    }
}
