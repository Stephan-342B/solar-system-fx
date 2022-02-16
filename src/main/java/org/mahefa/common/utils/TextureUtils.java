package org.mahefa.common.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class TextureUtils {

    private static final String BASE_PATH_IMAGE_V2 = "/v2/planets";

    public static PhongMaterial getTextureColor(String color) {
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setDiffuseColor(Color.valueOf(color));

        return phongMaterial;
    }

    public static PhongMaterial getTexture(String diffuseMap) {
        return get(diffuseMap, null, null, null);
    }

    public static PhongMaterial getTexture2(String diffuseMap, String bumpMap) {
        return get(diffuseMap, bumpMap, null, diffuseMap);
    }

    public static PhongMaterial getTexture(String diffuseMap, String illuminationMap) {
        return get(diffuseMap, null, null, diffuseMap);
    }

    public static PhongMaterial get(String diffuseMap, String specularMap, String bumpMap, String illuminationMap) {
        PhongMaterial phongMaterial = new PhongMaterial();

        if(StringUtils.isNotBlank(diffuseMap)) {
            final Image diffuseMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, diffuseMap)));
            phongMaterial.setDiffuseMap(diffuseMapImg);
        }

        if(StringUtils.isNotBlank(specularMap)) {
            final Image specularMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, specularMap)));
            phongMaterial.setSpecularMap(specularMapImg);
        } //else {
//            phongMaterial.setSpecularColor(Color.WHITE);
//        }

        if(StringUtils.isNotBlank(bumpMap)) {
            final Image bumpMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, bumpMap)));
            phongMaterial.setBumpMap(bumpMapImg);
        }

        if(StringUtils.isNotBlank(illuminationMap)) {
            final Image IlluminationMapImg = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, illuminationMap)));
            phongMaterial.setSelfIlluminationMap(IlluminationMapImg);
        }

        return phongMaterial;
    }
}
