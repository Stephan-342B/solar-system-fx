package org.mahefa.common.enumerator;

import java.util.HashMap;
import java.util.Map;

public enum SolarSystemTextures {
    SUN ("sun", "sun.jpg", null, null, null),
    MERCURY ("mercury", "mercury/mercury_color_map.jpg", null, "mercury/mercury_bump_map.jpg", null),
    VENUS ("venus", "venus/venus_color_map.jpg", null, "venus/venus_bump_map.jpg", null),
    EARTH ("earth", "earth/earth_color_map.jpg", "earth/earth_specular_map.jpg", "earth/earth_bump_map.jpg", null),
    MARS ("mars", "mars/mars_color_map_v2.jpg", null, "mars/mars_bump_map_v2.jpg", null),
    JUPITER ("jupiter", "jupiter/jupiter_color_map.jpg", null, null, null),
    SATURN ("saturn", "saturn/saturn_color_map.jpg", null, null, "saturn/saturn_ring_color.jpg"),
    URANUS ("uranus", "uranus/uranus_color_map.jpg", null, null, "uranus/uranus_ring_color.jpg"),
    NEPTUNE ("neptune", "neptune/neptune_color_map.jpg", null, null, null),
    MOON ("moon", "earth/moon/moon.jpg", null, null, null),
    DEIMOS ("deimos", null, null, "mars/deimos_bump.jpg", null),
    PHOBOS ("phobos", null, null, "mars/phobos_bump.jpg", null);

    private static final Map<String, SolarSystemTextures> BY_LABEL = new HashMap<>();

    private String designation;
    private String colorMap;
    private String specularMap;
    private String bumpMap;
    private String ring;

    static {
        for(SolarSystemTextures solarSystemTextures : values()) {
            BY_LABEL.put(solarSystemTextures.designation, solarSystemTextures);
        }
    }

    SolarSystemTextures(String designation, String colorMap, String specularMap, String bumpMap, String ring) {
        this.designation = designation;
        this.colorMap = colorMap;
        this.specularMap = specularMap;
        this.bumpMap = bumpMap;
        this.ring = ring;
    }

    public static String getColorMap(String designation) {
        return BY_LABEL.get(designation).colorMap;
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