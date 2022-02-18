package org.mahefa.common.utils;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.fxyz3d.tools.NormalMap;
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
            phongMaterial.setDiffuseMap(getImage(diffuseMap, false));

        if(StringUtils.isNotBlank(specularMap)) {
            phongMaterial.setSpecularMap(getImage(specularMap, false));
        } else {
            phongMaterial.setSpecularColor(Color.WHITE);
        }

        if(StringUtils.isNotBlank(bumpMap)) {
            phongMaterial.setBumpMap(getImage(bumpMap, false));
        } else {
            if(StringUtils.isNotBlank(specularMap)) {
                NormalMap normalMap = new NormalMap(getImage(specularMap, true));
                normalMap.setIntensity(250);
                normalMap.setIntensityScale(500);
                phongMaterial.setBumpMap(normalMap);
            }
        }

        if(StringUtils.isNotBlank(illuminationMap))
            phongMaterial.setSelfIlluminationMap(getImage(illuminationMap, false));

        phongMaterial.setSpecularPower(5e7);

        return phongMaterial;
    }

    static Image getImage(final String filepath, final boolean applyBlur) {
        Image image = new Image(TextureUtils.class.getResourceAsStream(String.format("%s/%s", BASE_PATH_IMAGE, filepath)));

        if(applyBlur) {
            ImageView blurredImage = new ImageView(image);
            GaussianBlur blur = new GaussianBlur(20);
            blurredImage.setEffect(blur);
            blurredImage.setPreserveRatio(true);
            return blurredImage.getImage();
        }

        return image;
    }
}
