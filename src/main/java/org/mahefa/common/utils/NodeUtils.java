package org.mahefa.common.utils;

import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.mahefa.common.constants.CelestialBodyCategory;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.PhysicalCharacteristic;
import org.mahefa.data.meeus.jean.Coordinate;

public class NodeUtils {

    public static Node buildSphere(CelestialBody celestialBody, Coordinate coordinate, double scaleDistanceValue) {
        final PhysicalCharacteristic physicalCharacteristic = celestialBody.getPhysicalCharacteristic();
        final String designation = celestialBody.getDesignation().toLowerCase();
        final double axialTilt = physicalCharacteristic.getAxialTilt();
        final boolean isAStar = celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.STAR);
        final double radius = (isAStar) ? 30d : 9d;
        final PhongMaterial phongMaterial = (isAStar) ? TextureUtils.getTexture(designation)
                : TextureUtils.getTextureColor(designation);

        Sphere sphere = new Sphere(radius);
        sphere.setId(designation);
        sphere.setTranslateX((coordinate.getX() * AstroMath.AU * 1e-3) * scaleDistanceValue);
        sphere.setTranslateY((coordinate.getZ() * AstroMath.AU * 1e-3) * scaleDistanceValue);
        sphere.setTranslateZ((coordinate.getY() * AstroMath.AU * 1e-3) * scaleDistanceValue);
        sphere.setMaterial(phongMaterial);
        sphere.setUserData(celestialBody);
        sphere.setDrawMode(DrawMode.FILL);
        sphere.setCullFace(CullFace.BACK);
        sphere.getTransforms().add(new Rotate(axialTilt));
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setCache(true);
        sphere.setCacheHint(CacheHint.QUALITY);
        sphere.setBlendMode(BlendMode.ADD);

        Lighting lighting = new Lighting();
        lighting.setLight(new Light.Distant());
        sphere.setEffect(lighting);

        return sphere;
    }

    public static Node createLightSource(double x, double y, double z) {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE);

        pointLight.getTransforms().add(new Translate(x, y, z));

        return pointLight;
    }
}
