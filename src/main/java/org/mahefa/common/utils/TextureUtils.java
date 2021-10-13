package org.mahefa.common.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.springframework.stereotype.Component;

@Component
public class TextureUtils {

    private static final String BASE_PATH_IMAGE_V2 = "/v2/planets";

    String color, diffuseMap, specularMap, bumpMap, illuminationMap;

    public TextureUtils() {
    }

    public void setColor(String color) {
        this.color = color;

        this.diffuseMap = null;
        this.specularMap = null;
        this.bumpMap = null;
        this.illuminationMap = null;
    }

    public void setMaps(String diffuseMap, String specularMap, String bumpMap, String illuminationMap) {
        this.diffuseMap = diffuseMap;
        this.specularMap = specularMap;
        this.bumpMap = bumpMap;
        this.illuminationMap = illuminationMap;

        this.color = null;
    }

    public PhongMaterial getPhongMaterial(String illuminationMap) {
        Image IlluminationMapImg = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, illuminationMap)));
        PhongMaterial phongMaterial = new PhongMaterial();
        phongMaterial.setSelfIlluminationMap(IlluminationMapImg);

        return phongMaterial;
    }

    public PhongMaterial getPhongMaterial() {
        PhongMaterial phongMaterial = new PhongMaterial();

        if(color != null) {
            phongMaterial.setDiffuseColor(Color.valueOf(color));
        }

        if(diffuseMap != null) {
            Image diffuseMapImg = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, diffuseMap)));
            phongMaterial.setDiffuseMap(diffuseMapImg);
        }

        if(specularMap != null) {
            Image specularMapImg = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, specularMap)));
            phongMaterial.setSpecularMap(specularMapImg);
        }

        if(bumpMap != null) {
            Image bumpMapImg = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, bumpMap)));
            phongMaterial.setBumpMap(bumpMapImg);
        }

        if(illuminationMap != null) {
            Image IlluminationMapImg = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, illuminationMap)));
            phongMaterial.setSelfIlluminationMap(IlluminationMapImg);
        }

        return phongMaterial;
    }
}
