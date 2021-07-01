package org.mahefa.common.utils;

import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;

public class TextureUtils {

    private static final String BASE_PATH_IMAGE_V2 = "/v2/planets";

    private PhongMaterial phongMaterial;
    private String colorMap;
    private String specularMap;
    private String bumpMap;
    private String illuminationImage;

    public TextureUtils() {
        this.phongMaterial = new PhongMaterial();
    }

    public PhongMaterial getPhongMaterial() {
        Image diffuseMap = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, this.colorMap)));
        phongMaterial.setDiffuseMap(diffuseMap);

        if(this.specularMap != null) {
            Image specularMap = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, this.specularMap)));
            phongMaterial.setSpecularMap(specularMap);
        }

        if(this.bumpMap != null) {
            Image bumpMap = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, this.bumpMap)));
            phongMaterial.setBumpMap(bumpMap);
        }

        if(this.illuminationImage != null) {
            Image IlluminationMap = new Image(getClass().getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE_V2, this.illuminationImage)));
            phongMaterial.setSelfIlluminationMap(IlluminationMap);
        }

        return phongMaterial;
    }

    public void setColorMap(String colorMap) {
        this.colorMap = colorMap;
    }

    public void setSpecularMap(String specularMap) {
        this.specularMap = specularMap;
    }

    public void setBumpMap(String bumpMap) {
        this.bumpMap = bumpMap;
    }

    public void setIlluminationImage(String illuminationImage) {
        this.illuminationImage = illuminationImage;
    }
}
