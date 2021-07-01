package org.mahefa.data;

import org.mahefa.common.utils.ComputationUtils;

public class OrbitalCharacteristic {

    private final double aphelion;
    private final double perihelion;
    private final double semiMajorAxis;
    private final double semiMinorAxis;

    private final double inclination;

    private final double eccentricity;

    private final double orbitalSpeed;
    private final double orbitalPeriod;
    private final double orbitalDegreePerSec;
    private final double synodicPeriod;

    private OrbitalCharacteristic(Builder builder) {
        this.aphelion = builder.aphelion;
        this.perihelion = builder.perihelion;
        this.semiMajorAxis = builder.semiMajorAxis;
        this.semiMinorAxis = builder.semiMinorAxis;

        this.inclination = builder.inclination;

        this.eccentricity = builder.eccentricity;

        this.orbitalSpeed = builder.orbitalSpeed;
        this.orbitalPeriod = builder.orbitalPeriod;
        this.orbitalDegreePerSec = builder.orbitalDegreePerSec;
        this.synodicPeriod = builder.synodicPeriod;
    }

    public double getAphelion() {
        return aphelion;
    }

    public double getPerihelion() {
        return perihelion;
    }

    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double getSemiMinorAxis() {
        return semiMinorAxis;
    }

    public double getInclination() {
        return inclination;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public double getOrbitalSpeed() {
        return orbitalSpeed;
    }

    public double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public double getOrbitalDegreePerSec() {
        return orbitalDegreePerSec;
    }

    public double getSynodicPeriod() {
        return synodicPeriod;
    }

    public static class Builder {
        private final double aphelion;
        private final double perihelion;
        private double semiMajorAxis;
        private final double semiMinorAxis;

        private double inclination;

        private double eccentricity;

        private double orbitalSpeed;
        private double orbitalPeriod;
        private double orbitalDegreePerSec;
        private double synodicPeriod;

        public Builder(double aphelion, double perihelion) {
            this.aphelion = aphelion;
            this.perihelion = perihelion;
            this.semiMajorAxis = ComputationUtils.arithmeticMean(aphelion, perihelion);
            this.semiMinorAxis = ComputationUtils.geometricMean(aphelion, perihelion);

            this.eccentricity = ComputationUtils.eccentricity(semiMajorAxis, semiMinorAxis);
        }

        public Builder setInclination(double inclination) {
            this.inclination = inclination;
            return this;
        }

        public Builder setOrbitalPeriod(boolean retrograde, boolean isEarth) {
            this.orbitalPeriod = ComputationUtils.orbitalPeriod(semiMajorAxis);
            this.synodicPeriod = ComputationUtils.synedicPeriod(this.orbitalPeriod);
            this.orbitalSpeed = ComputationUtils.orbitalSpeed(semiMajorAxis, this.orbitalPeriod);
            this.orbitalDegreePerSec = ComputationUtils.rotationDegree((!isEarth) ? this.synodicPeriod : this.orbitalPeriod);
            return this;
        }

        public Builder setOrbitalDegreePerSec(double orbitalDegreePerSec) {
            this.orbitalDegreePerSec = orbitalDegreePerSec;
            return this;
        }

        public Builder setSynediPeriod(double siderialPeriod)  {
            this.synodicPeriod = ComputationUtils.synedicPeriod(siderialPeriod);
            this.orbitalDegreePerSec = ComputationUtils.rotationDegree(this.synodicPeriod);
            return this;
        }

        public OrbitalCharacteristic build() {
            return new OrbitalCharacteristic(this);
        }
    }
}
