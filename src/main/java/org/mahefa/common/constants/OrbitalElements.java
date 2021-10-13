package org.mahefa.common.constants;

public enum OrbitalElements {

    SEMI_MAJOR_AXIS("a"),
    ECCENTRICITY("e"),
    INCLINATION("i"),
    LONGITUDE_OF_ASCENDING_NODE("Ω"),
    LONGITUDE_OF_PERIHELION("Π"),
    MEAN_LONGITUDE("L");

    final String element;

    OrbitalElements(String element) {
        this.element = element;
    }

    public String toValue() {
        return element;
    }
}