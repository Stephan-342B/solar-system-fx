package org.mahefa.service.application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.shapes.primitives.SegmentedSphereMesh;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mahefa.common.enumerator.CelestialBodyCategory;
import org.mahefa.common.enumerator.SolarSystemTextures;
import org.mahefa.common.utils.TextureUtils;
import org.mahefa.data.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class GalaxyApplication {

    private List<Node> nodes;
    private Xform galaxyGroup;

    private static final double SCALE_SIZE_VALUE = 0.0001;
    private static final double SCALE_DIST_VALUE = 0.000001;

    private static final String JSON_FILE = "/json/galaxies.json";
    private static final String BACKGROUND_IMAGE = "/v2/milky_way.jpg";

    public GalaxyApplication() {
        galaxyGroup = new Xform();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void buildGalaxy(Xform world) {
        Xform galaxyXform = new Xform();
        Xform starXform = new Xform();
        Xform starLightXform = new Xform();
        Xform planetXform = new Xform();
        Xform nameXform = new Xform();
        Xform ellipseXform = new Xform();
        Xform satelliteXform = new Xform();

        // Init hierarchy
        galaxyXform.getChildren().add(planetXform);
        galaxyXform.getChildren().addAll(starXform);
        galaxyXform.getChildren().add(satelliteXform);
        galaxyXform.getChildren().add(ellipseXform);
        starXform.getChildren().add(starLightXform);
        planetXform.getChildren().add(nameXform);

        // Load galaxies
        nodes = new ArrayList<>();
        List<Galaxy> galaxies = loadGalaxies();
        List<Sphere> sun = new ArrayList<>();

        galaxies.forEach(galaxy -> {
            SegmentedSphereMesh segmentedSphereMesh = new SegmentedSphereMesh(3285.0);
            segmentedSphereMesh.setCullFace(CullFace.NONE);
            //segmentedSphereMesh.setTextureModeImage(getClass().getResource(BACKGROUND_IMAGE).toExternalForm());

            final PhongMaterial phongMaterial = new PhongMaterial();
            Image image = new Image(getClass().getResourceAsStream(BACKGROUND_IMAGE));
            phongMaterial.setSelfIlluminationMap(image);

            Glow glow = new Glow();
            glow.setLevel(0.9);

            segmentedSphereMesh.setMaterial(phongMaterial);
            segmentedSphereMesh.setEffect(glow);

            Rotate rotate = new Rotate(60.0);

            segmentedSphereMesh.getTransforms().add(rotate);
            segmentedSphereMesh.setRotationAxis(Rotate.Y_AXIS);

            galaxyXform.getChildren().add(segmentedSphereMesh);

            galaxy.getStars().forEach(star -> {
                final Sphere sphere = createAndAnimateSphere(star, null, null);

                starXform.getChildren().add(sphere);
                starLightXform.getChildren().add(createLightSource(sphere.getTranslateX(), sphere.getTranslateY(), sphere.getTranslateZ()));

                nodes.add(sphere);
                sun.add(sphere);
            });

            galaxy.getPlanets().forEach(planet -> {
                final Sphere parent = createAndAnimateSphere(planet, null, sun.get(0));

                planetXform.getChildren().add(parent);
                nameXform.getChildren().add(createLabel(planet.getDesignation(), parent));

                nodes.add(parent);

                List<Node> moons = new ArrayList<>();

                planet.getCelestialBodies().forEach(satellite -> {
                    final Sphere child = createAndAnimateSphere(satellite, parent, null);

                    nodes.add(child);
                    moons.add(child);

                    satelliteXform.getChildren().add(child);
                });
            });
        });

        galaxyGroup.getChildren().add(galaxyXform);
        world.getChildren().addAll(galaxyGroup);
    }

    private PointLight createLightSource(double x, double y, double z) {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE);

        pointLight.getTransforms().add(new Translate(x, y, z));

        return pointLight;
    }

    private Sphere createAndAnimateSphere(CelestialBody celestialBody, Sphere parent, Sphere sun) {
        final boolean isPlanet = (celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.PLANET));
        final PhysicalCharacteristic physicalCharacteristic = celestialBody.getPhysicalCharacteristic();
        final OrbitalCharacteristic orbitalCharacteristic = celestialBody.getOrbitalCharacteristic();
        final double aphelion = orbitalCharacteristic.getAphelion() * SCALE_DIST_VALUE;
        final double perihelion = orbitalCharacteristic.getPerihelion() * SCALE_DIST_VALUE;
        final double inclination = orbitalCharacteristic.getInclination();
        final double orbitalDegreePerSecond = orbitalCharacteristic.getOrbitalDegreePerSec();

        Bounds bounds = sun != null ? sun.localToScene(sun.getBoundsInLocal()) : null;
        double width = bounds != null ? (bounds.getWidth() / 2) : 0;

        final double radius = physicalCharacteristic.getRadius() * SCALE_SIZE_VALUE;
        final double pos_x = (parent == null) ? perihelion + width : parent.getTranslateX();
        final double pos_y = 0.0;
        final double pos_z = (parent == null) ? 0.0 : perihelion;
        final double axialTilt = physicalCharacteristic.getAxialTilt();
        final String designation = celestialBody.getDesignation().toLowerCase();
        final String texturePath = SolarSystemTextures.getColorMap(designation);
        final PhongMaterial phongMaterial = (isPlanet)
                ? addTexture(texturePath) : addTexture(texturePath, texturePath);
        final Sphere sphere = createSphere(radius, pos_x, pos_y, pos_z, axialTilt, phongMaterial);

        // Animate sphere
        if(isPlanet) {
            rotate(sphere, physicalCharacteristic.getRotationDegreePerSec());

            if(parent == null) {
                orbit(sphere, aphelion, perihelion, orbitalDegreePerSecond, inclination);
            } else {
                orbit(parent, sphere, aphelion, perihelion, inclination);
            }
        }

        return sphere;
    }

    private Sphere createSphere(double radius, double x, double y, double z, double axialTilt, PhongMaterial phongMaterial) {
        Sphere sphere = new Sphere(radius);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        sphere.setMaterial(phongMaterial);
        sphere.setDrawMode(DrawMode.FILL);
        sphere.setCullFace(CullFace.BACK);

        Rotate rotate = new Rotate(axialTilt);

        sphere.getTransforms().add(rotate);
        sphere.setRotationAxis(Rotate.Y_AXIS);

        return sphere;
    }

    private Label createLabel(String designation, Node node) {
        Bounds bounds = node.localToScene(node.getBoundsInLocal());

        Label label = new Label(designation);
        label.setTranslateX(node.getTranslateX());
        label.setTranslateY(node.getTranslateY());
        label.setTranslateZ(node.getTranslateZ());
        //label.setMaxWidth(90.0);
        label.setMinWidth(50.0);
        label.setTranslateX(label.getTranslateX() + bounds.getWidth() / 2);
        label.setTranslateY(label.getTranslateY() - bounds.getHeight() / 2);
        label.setRotationAxis(Rotate.Z_AXIS);
        label.setLabelFor(node);
        label.setTextFill(Color.WHITE);

        return label;
    }

    private PhongMaterial addTexture(String colorMapPath) {
        return addTexture(colorMapPath, null, null, null);
    }

    private PhongMaterial addTexture(String colorMapPath, String illuminationImagePath) {
        return addTexture(colorMapPath, null, null, illuminationImagePath);
    }

    private PhongMaterial addTexture(String colorMapPath, String specularMapPath, String bumpMapPath, String illuminationImagePath) {
        TextureUtils textureUtils = new TextureUtils();
        textureUtils.setColorMap(colorMapPath);
        textureUtils.setSpecularMap(specularMapPath);
        textureUtils.setBumpMap(bumpMapPath);
        textureUtils.setIlluminationImage(illuminationImagePath);

        return textureUtils.getPhongMaterial();
    }

    private void rotate(Node node, final double degreePerSecond) {
        AnimationTimer rotationAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                node.rotateProperty().set(node.getRotate() + degreePerSecond * -1);
                node.setRotationAxis(new Point3D(0, 1, 0));
            }
        };

        rotationAnimationTimer.start();
    }

    /**
     * Parametric equation ellipse centered at the origin
     * x(α) = Rx cos(α)
     * y(α) = Ry sin(α)
     *
     * If not centered
     * x(a) = h + Rx cos(a)
     * y(a) = k + Ry sin(a)
     *
     * where h and k are coordinates
     *
     * Rotate
     * x = a cos(t) + h sin(t)
     * y = b sin(t) + h cos(t)
     *
     * for 0 <= t <= 2PI
     *
     * @param node
     * @param radiusX
     * @param radiusY
     * @param rotationDegree
     * @param inclination
     */
    public void orbit(Node node,  double radiusX, double radiusY, double rotationDegree, double inclination) {
        AnimationTimer revolutionTimer = new AnimationTimer() {
            double t = 0.0;
            double rd = rotationDegree * 0.0003;

            @Override
            public void handle(long now) {
                t += rd;

                node.translateXProperty().set((radiusY * Math.cos(t)) + (inclination * Math.sin(t)));
                node.translateYProperty().set(Math.cos(t) * inclination);
                node.translateZProperty().set((radiusX * Math.sin(t) + (inclination * Math.cos(t))));
            }
        };

        revolutionTimer.start();
    }

    public void orbit(Node parentNode, Node node, double radiusX, double radiusY, double inclination) {
        AnimationTimer animationTimer = new AnimationTimer() {
            double originalX = 0.0 , originalZ = 0.0;

            @Override
            public void handle(long now) {
                double xPrime = parentNode.getTranslateX();
                double zPrime = parentNode.getTranslateZ();
                double tan = Math.atan2(originalX - xPrime, originalZ - zPrime);
                double t = 180 - Math.toDegrees(tan);

                node.translateXProperty().set((parentNode.getTranslateX() + ((radiusY * SCALE_DIST_VALUE) * Math.cos(t))) + (inclination * Math.sin(t)));
                node.translateYProperty().set(parentNode.getTranslateY() + (Math.cos(t) * inclination));
                node.translateZProperty().set((parentNode.getTranslateZ() + ((radiusX * SCALE_DIST_VALUE) * Math.sin(t)) + (inclination * Math.cos(t))));

                originalX = xPrime;
                originalZ = zPrime;
            }
        };

        animationTimer.start();
    }

    private List<Galaxy> loadGalaxies() {
        JSONObject jsonObject = readJsonFile();
        List<Galaxy> galaxies = new ArrayList<>();

        if(jsonObject != null) {
            JSONArray galaxyJsons = (JSONArray)jsonObject.get("galaxies");

            // Load galaxy
            for(int i = 0; i < galaxyJsons.length(); i++) {
                JSONObject galaxyJson = galaxyJsons.getJSONObject(i);
                Galaxy.Builder galaxy = new Galaxy.Builder(galaxyJson.getString("designation"));

                // Load star
                galaxy.addStars(loadCelestialBodies((JSONArray) galaxyJson.getJSONArray("stars")));

                // Load planet
                galaxy.addPlanets(loadCelestialBodies((JSONArray) galaxyJson.getJSONArray("planets")));

                galaxies.add(galaxy.build());
            }
        }

        return galaxies;
    }

    private List<CelestialBody> loadCelestialBodies(JSONArray celestialBodiesJSON) {
        List<CelestialBody> celestialBodies = new ArrayList<>();

        for(int p = 0; p < celestialBodiesJSON.length(); p++) {
            JSONObject celestialBodyJSON = celestialBodiesJSON.getJSONObject(p);
            JSONObject orbitalCharacteristicJson = celestialBodyJSON.getJSONObject("orbitalCharacteristic");
            JSONObject physicalCharacteristicJson = celestialBodyJSON.getJSONObject("physicalCharacteristic");

            final String designation = celestialBodyJSON.getString("designation");
            final double perihelion = orbitalCharacteristicJson.getDouble("perihelion");
            final boolean retrograde = false; //physicalCharacteristicJson.getBoolean("retrograde");

            PhysicalCharacteristic physicalCharacteristic = new PhysicalCharacteristic.Builder(
                    physicalCharacteristicJson.getDouble("radius"), physicalCharacteristicJson.getDouble("mass")
            )
                    .setAxialTilt(physicalCharacteristicJson.getDouble("axialTilt"))
                    .setSiderealRotationPeriod(physicalCharacteristicJson.getDouble("siderealRotationPeriod"))
                    .build();

            OrbitalCharacteristic orbitalCharacteristic = new OrbitalCharacteristic.Builder(
                    orbitalCharacteristicJson.getDouble("aphelion"), perihelion
            )
                    .setInclination(orbitalCharacteristicJson.getDouble("inclination"))
                    .setOrbitalPeriod(retrograde, (designation.equalsIgnoreCase("earth")))
                    .build();

            CelestialBody.Builder planet = new CelestialBody.Builder(designation, orbitalCharacteristic, physicalCharacteristic)
                    .setCategory(celestialBodyJSON.getString("celestialBodyCategory"));

            planet.addCelestialBodies(loadCelestialBodies((JSONArray)celestialBodyJSON.getJSONArray("celestialBodies")));

            celestialBodies.add(planet.build());
        }

        return celestialBodies;
    }

    private JSONObject readJsonFile() {
        try(InputStream inputStream = getClass().getResourceAsStream(JSON_FILE)) {
            JSONTokener jsonTokener = new JSONTokener(inputStream);
            JSONObject jsonObject = new JSONObject(jsonTokener);

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
