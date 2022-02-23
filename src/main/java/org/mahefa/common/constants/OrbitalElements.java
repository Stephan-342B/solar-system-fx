package org.mahefa.common.constants;

/**
 *
 * L: is the ecliptic longitude at which an orbiting body could be found if its orbit were circular and free of perturbations
 *
 * a: is half the major axis (distance between apoapsis and periapsis) of an elliptical orbit.
 * It is defined as the mean distance between either focus of the orbit and any point on the orbit
 * and equals the radius if the orbit is circular.
 *
 * e: is a dimensionless parameter that determines the amount by which its orbit around another body deviates
 * from a perfect circle. The value of 0 is a circular orbit, values between 0 and 1 form an elliptic orbit,
 * 1 is a parabolic escape orbit, and greater than 1 is a hyperbola
 *
 * i: is the tilt of an object's orbit around a celestial body
 *
 * Ω: is the longitude of ascending node. This orbital element used to specify the orbit of an object in space. It is the angle
 * from a specified reference direction, called the origin of longitude, to the direction of the ascending node, as measured
 * in a specified reference plane
 *
 * Π: longitude of the perihelion.
 *
 * Orbital elements for the standard equinox J2000.0 are used here
 *
 */
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