package org.mahefa.common.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.mahefa.common.constants.SolarSystemTextures;

public class TextureUtils {

    private static final String BASE_PATH_IMAGE = "/texture/v3";

    public static PhongMaterial getTextureFromColor(String id) {
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(Color.valueOf(SolarSystemTextures.getColor(id)));

        return phongMaterial;
    }

    public static PhongMaterial getTexture(String id) {
        final String diffuseMap = SolarSystemTextures.getDiffuseMap(id);
        final String specularMap = SolarSystemTextures.getSpecularMap(id);
        final String bumpMap = SolarSystemTextures.getBumpMap(id);
        final String illuminationMap = SolarSystemTextures.getIlluminationMap(id);

        PhongMaterial phongMaterial = new PhongMaterial();

        if(StringUtils.isNotBlank(diffuseMap)) {
            final Image diffuseMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, diffuseMap)));
            phongMaterial.setDiffuseMap(diffuseMapImg);
        }

        if(StringUtils.isNotBlank(specularMap)) {
            final Image specularMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, specularMap)));
            phongMaterial.setSpecularMap(specularMapImg);
        } //else {
//            phongMaterial.setSpecularColor(Color.WHITE);
//        }

        if(StringUtils.isNotBlank(bumpMap)) {
            final Image bumpMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, bumpMap)));
            phongMaterial.setBumpMap(bumpMapImg);
        }

        if(StringUtils.isNotBlank(illuminationMap)) {
            final Image IlluminationMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, illuminationMap)));
            phongMaterial.setSelfIlluminationMap(IlluminationMapImg);
        }

        return phongMaterial;
    }
}
