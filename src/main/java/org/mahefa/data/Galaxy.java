package org.mahefa.data;

import java.util.ArrayList;
import java.util.List;

public class Galaxy {

    private final String designation;

    private final List<CelestialBody> planets;
    private final List<CelestialBody> stars;

    private Galaxy(Builder builder) {
        this.designation = builder.designation;

        this.planets = builder.planets;
        this.stars = builder.stars;
    }

    public String getDesignation() {
        return designation;
    }

    public List<CelestialBody> getPlanets() {
        return planets;
    }

    public List<CelestialBody> getStars() {
        return stars;
    }

    public static class Builder {

        private final String designation;

        private List<CelestialBody> planets = new ArrayList<>();
        private List<CelestialBody> stars = new ArrayList<>();

        public Builder(String designation) {
            this.designation = designation;
        }

        public Builder addPlanets(List<CelestialBody> planets) {
            this.planets = planets;
            return this;
        }

        public Builder addStars(List<CelestialBody> stars) {
            this.stars = stars;
            return this;
        }

        public Galaxy build() {
            return new Galaxy(this);
        }
    }
}
