package org.mahefa.common.constants;

import java.util.HashMap;
import java.util.Map;

public enum SolarSystemTextures {
    SUN ("sun", "#F7FD04", "sun.jpg", null, null, "sun.jpg", null),
    MERCURY ("mercury", "#DDDDDD", "mercury/map.jpg", null, "mercury/bump.jpg", null, null),
    VENUS ("venus", "#FB9300", "venus/map.jpg", null, "venus/bump.jpg", null, null),
    EARTH ("earth", "#125D98", "earth/map.jpg", "earth/specular.jpg", "earth/bump.jpg", null, null),
    MARS ("mars", "#C84B31", "mars/map.jpg", null, "mars/bump.jpg", null, null),
    JUPITER ("jupiter", "#A45C40", "jupiter/map.jpg", null, null, null, null),
    SATURN ("saturn", "#D8B384", "saturn/map.jpg", null, null, null, "saturn/saturn_ring_color.jpg"),
    URANUS ("uranus", "#CEE5D0", "uranus/map.jpg", null, null, null, "uranus/uranus_ring_color.jpg"),
    NEPTUNE ("neptune", "#3C8DAD", "neptune/map.jpg", null, null, null, null),
    MOON ("moon", "#C2B8A3", "earth/satellites/moon/map.jpg", null, null, null, null),
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