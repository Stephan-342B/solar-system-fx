package org.mahefa.data;

public class OrbitalCharacteristic {

    private final double[] semiMajorAxis;
    private final double[] meanLongitudes;
    private final double[] eccentricities;
    private final double[] inclinations;
    private final double[] longitudeAscendingNodes;
    private final double[] longitudesPerihelion;

    private OrbitalCharacteristic(Builder builder) {
        this.semiMajorAxis = builder.semiMajorAxis;
        this.meanLongitudes = builder.meanLongitudes;
        this.eccentricities = builder.eccentricities;
        this.inclinations = builder.inclinations;
        this.longitudeAscendingNodes = builder.longitudeAscendingNodes;
        this.longitudesPerihelion = builder.longitudesPerihelion;
    }

    public double[] getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double[] getMeanLongitudes() {
        return meanLongitudes;
    }

    public double[] getEccentricities() {
        return eccentricities;
    }

    public double[] getInclinations() {
        return inclinations;
    }

    public double[] getLongitudeAscendingNodes() {
        return longitudeAscendingNodes;
    }

    public double[] getLongitudesPerihelion() {
        return longitudesPerihelion;
    }

    public static class Builder {
        private final double[] semiMajorAxis;
        private double[] eccentricities;
        private double[] meanLongitudes;
        private double[] inclinations;
        private double[] longitudeAscendingNodes;
        private double[] longitudesPerihelion;

        public Builder(double[] semiMajorAxis) {
            this.semiMajorAxis = semiMajorAxis;
        }

        public Builder setEccentricities(double[] eccentricities) {
            this.eccentricities = eccentricities;
            return this;
        }

        public Builder setInclinations(double[] inclinations) {
            this.inclinations = inclinations;
            return this;
        }

        public Builder setLongitudeAscendingNodes(double[] longitudeAscendingNodes) {
            this.longitudeAscendingNodes = longitudeAscendingNodes;
            return this;
        }

        public Builder setMeanLongitudes(double[] meanLongitudes) {
            this.meanLongitudes = meanLongitudes;
            return this;
        }

        public Builder setLongitudesPerihelion(double[] longitudesPerihelion) {
            this.longitudesPerihelion = longitudesPerihelion;
            return this;
        }

        public OrbitalCharacteristic build() {
            return new OrbitalCharacteristic(this);
        }
    }
}
