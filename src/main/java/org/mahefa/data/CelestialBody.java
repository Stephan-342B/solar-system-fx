package org.mahefa.data;

import org.mahefa.common.enumerator.CelestialBodyCategory;

import java.util.List;

public class CelestialBody {

    private final String designation;
    private final CelestialBodyCategory celestialBodyCategory;

    private final OrbitalCharacteristic orbitalCharacteristic;
    private final PhysicalCharacteristic physicalCharacteristic;

    private final List<CelestialBody> celestialBodies;

    public CelestialBody(Builder builder) {
        this.designation = builder.designation;
        this.celestialBodyCategory = builder.celestialBodyCategory;

        this.orbitalCharacteristic = builder.orbitalCharacteristic;
        this.physicalCharacteristic = builder.physicalCharacteristic;

        this.celestialBodies = builder.celestialBodies;
    }

    public String getDesignation() {
        return designation;
    }

    public CelestialBodyCategory getCelestialBodyCategory() {
        return celestialBodyCategory;
    }

    public OrbitalCharacteristic getOrbitalCharacteristic() {
        return orbitalCharacteristic;
    }

    public PhysicalCharacteristic getPhysicalCharacteristic() {
        return physicalCharacteristic;
    }

    public List<CelestialBody> getCelestialBodies() {
        return celestialBodies;
    }

    public static class Builder {
        private final String designation;
        private CelestialBodyCategory celestialBodyCategory;

        private final OrbitalCharacteristic orbitalCharacteristic;
        private final PhysicalCharacteristic physicalCharacteristic;

        private List<CelestialBody> celestialBodies;

        public Builder(String designation, OrbitalCharacteristic orbitalCharacteristic, PhysicalCharacteristic physicalCharacteristic) {
            this.designation = designation;

            this.orbitalCharacteristic = orbitalCharacteristic;
            this.physicalCharacteristic = physicalCharacteristic;
        }

        public CelestialBody.Builder setCategory(String celestialBodyCategory) {
            this.celestialBodyCategory = CelestialBodyCategory.valueOf(celestialBodyCategory);
            return this;
        }

        public CelestialBody.Builder addCelestialBody(CelestialBody celestialBody) {
            this.celestialBodies.add(celestialBody);
            return this;
        }

        public CelestialBody.Builder addCelestialBodies(List<CelestialBody> celestialBodies) {
            this.celestialBodies = celestialBodies;
            return this;
        }

        public CelestialBody build() {
            return new CelestialBody(this);
        }
    }
}
