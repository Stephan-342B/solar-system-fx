package org.mahefa.common.constants;

import java.util.HashMap;
import java.util.Map;

public enum SolarSystemTextures {
    SUN ("sun", "#F7FD04", "sun.jpg", null, null, null),
    MERCURY ("mercury", "#DDDDDD", "mercury/mercury_color_map.jpg", null, "mercury/mercury_bump_map.jpg", null),
    VENUS ("venus", "#FB9300", "venus/venus_color_map.jpg", null, "venus/venus_bump_map.jpg", null),
    EARTH ("earth", "#125D98", "earth/earth_color_map.jpg", "earth/earth_specular_map.jpg", "earth/earth_bump_map.jpg", null),
    MARS ("mars", "#C84B31", "mars/mars_color_map_v2.jpg", null, "mars/mars_bump_map_v2.jpg", null),
    JUPITER ("jupiter", "#A45C40", "jupiter/jupiter_color_map.jpg", null, null, null),
    SATURN ("saturn", "#D8B384", "saturn/saturn_color_map.jpg", null, null, "saturn/saturn_ring_color.jpg"),
    URANUS ("uranus", "#CEE5D0", "uranus/uranus_color_map.jpg", null, null, "uranus/uranus_ring_color.jpg"),
    NEPTUNE ("neptune", "#3C8DAD", "neptune/neptune_color_map.jpg", null, null, null),
    MOON ("moon", "#C2B8A3", "earth/moon/moon.jpg", null, null, null),
    DEIMOS ("deimos", "#C2B8A3", null, null, "mars/deimos_bump.jpg", null),
    PHOBOS ("phobos", "#C2B8A3", null, null, "mars/phobos_bump.jpg", null);

    private static final Map<String, SolarSystemTextures> BY_LABEL = new HashMap<>();

    private String designation;
    private String color;
    private String diffuseMap;
    private String specularMap;
    private String bumpMap;
    private String ring;

    static {
        for(SolarSystemTextures solarSystemTextures : values()) {
            BY_LABEL.put(solarSystemTextures.designation, solarSystemTextures);
        }
    }

    SolarSystemTextures(String designation, String color, String diffuseMap, String specularMap, String bumpMap, String ring) {
        this.designation = designation;
        this.color = color;
        this.diffuseMap = diffuseMap;
        this.specularMap = specularMap;
        this.bumpMap = bumpMap;
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

    public static String getRing(String designation) {
        return BY_LABEL.get(designation).ring;
    }
}