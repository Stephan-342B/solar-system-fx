package org.mahefa.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Diffuse: is the texture that defines the object's color
 * Specular/bump: is a texture that defines how the light reflect the object.
 * Bump/Normal: is a texture that add small details to the object
 *
 * Planet like Jupiter does not have a specular or either bump map by the fact
 * that he's just a giant gas.
 */
public enum SolarSystemTextures {
    SUN ("sun", "#F7FD04", "sun.jpg", null, null, "sun.jpg", null),
    MERCURY ("mercury", "#DDDDDD", "mercury/color.jpg", "mercury/specular.jpg", "mercury/normal.jpg",null, null),
    VENUS ("venus", "#FB9300", "venus/color.jpg", "venus/specular.jpg", "venus/normal.jpg", null, null),
    EARTH ("earth", "#125D98", "earth/color.jpg", "earth/specular.jpg", "earth/normal.jpg", null, null),
    MARS ("mars", "#C84B31", "mars/color.jpg", "mars/specular.jpg", "mars/normal.jpg", null, null),
    JUPITER ("jupiter", "#A45C40", "jupiter/color.jpg", null, null, null, null),
    SATURN ("saturn", "#D8B384", "saturn/color.jpg", null, null, null, "saturn/saturn_ring_color.jpg"),
    URANUS ("uranus", "#CEE5D0", "uranus/color.jpg", null, null, null, "uranus/uranus_ring_color.jpg"),
    NEPTUNE ("neptune", "#3C8DAD", "neptune/color.jpg", null, null, null, null),
    MOON ("moon", "#C2B8A3", "earth/satellites/moon/color.jpg", "earth/satellites/moon/specular.jpg", "earth/satellites/moon/normal.jpg", null, null),
    DEIMOS ("deimos", "#C2B8A3", null, null, "mars/deimos_bump.jpg", null, null),
    PHOBOS ("phobos", "#C2B8A3", null, null, "mars/phobos_bump.jpg", null, null);

    private static final Map<String, SolarSystemTextures> BY_LABEL = new HashMap<>();

    private String designation;
    private String color;
    private String diffuseMap;
    private String specularMap;
    private String bumpMap;
    private String illuminationMap;
    private String ring;

    static {
        for(SolarSystemTextures solarSystemTextures : values()) {
            BY_LABEL.put(solarSystemTextures.designation, solarSystemTextures);
        }
    }

    SolarSystemTextures(String designation, String color, String diffuseMap, String specularMap, String bumpMap, String illuminationMap, String ring) {
        this.designation = designation;
        this.color = color;
        this.diffuseMap = diffuseMap;
        this.specularMap = specularMap;
        this.bumpMap = bumpMap;
        this.illuminationMap = illuminationMap;
        this.ring = ring;
    }

    public static String getColor(String designation) { return  BY_LABEL.get(designation).color; }

    public static String getDiffuseMap(String designation) {
        return BY_LABEL.get(designation).diffuseMap;
    }

    public static String getSpecularMap(String designation) {
        return BY_LABEL.get(designation).specularMap;
    }

    public static String getBumpMap(String designation) {
        return BY_LABEL.get(designation).bumpMap;
    }

    public static String getIlluminationMap(String designation) { return BY_LABEL.get(designation).illuminationMap; }

    public static String getRing(String designation) {
        return BY_LABEL.get(designation).ring;
    }
}