package org.mahefa.common.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.mahefa.common.constants.SolarSystemTextures;

public class TextureUtils {

    private static final String BASE_PATH_IMAGE = "/texture/v3";

    public static PhongMaterial getTextureColor(String id) {
        final String color = SolarSystemTextures.getColor(id);
        PhongMaterial phongMaterial = new PhongMaterial();

        if(StringUtils.isNotBlank(color))
            phongMaterial.setDiffuseColor(Color.valueOf(color));

        return phongMaterial;
    }

    public static PhongMaterial getTexture(String id) {
        final String diffuseMap = SolarSystemTextures.getDiffuseMap(id);
        final String specularMap = SolarSystemTextures.getSpecularMap(id);
        final String bumpMap = SolarSystemTextures.getBumpMap(id);
        final String illuminationMap = SolarSystemTextures.getIlluminationMap(id);

        PhongMaterial phongMaterial = new PhongMaterial();

        if(StringUtils.isNotBlank(diffuseMap))
            phongMaterial.setDiffuseMap(getImage(diffuseMap));

        if(StringUtils.isNotBlank(specularMap)) {
            phongMaterial.setSpecularMap(getImage(specularMap));
        } else {
            phongMaterial.setSpecularColor(Color.WHITE);
        }

        if(StringUtils.isNotBlank(bumpMap))
            phongMaterial.setBumpMap(getImage(bumpMap));

        if(StringUtils.isNotBlank(illuminationMap))
            phongMaterial.setSelfIlluminationMap(getImage(illuminationMap));

        phongMaterial.setSpecularPower(1e8);

        return phongMaterial;
    }

    static Image getImage(final String filepath) {
        return new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, filepath)));
    }
}
