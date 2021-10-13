package org.mahefa.service.application.javafx.object.galaxy;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.shapes.primitives.SegmentedSphereMesh;
import org.mahefa.common.constants.CelestialBodyCategory;
import org.mahefa.common.constants.SolarSystemTextures;
import org.mahefa.common.utils.TextureUtils;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.Galaxy;
import org.mahefa.data.PhysicalCharacteristic;
import org.mahefa.data.Xform;
import org.mahefa.data.meeus.jean.Coordinates;
import org.mahefa.data.meeus.jean.PeriodicTerm;
import org.mahefa.service.application.meeus.jean.algorithm.planetary_position.PlanetaryPositionAppService;
import org.mahefa.service.business.galaxy.DataBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GalaxyAppServiceImpl implements GalaxyAppService {

    @Autowired
    PlanetaryPositionAppService planetaryPositionAppService;

    @Autowired
    DataBusinessService dataBusinessService;

    @Autowired
    TextureUtils textureUtils;

    @Value("${scale.size.value}")
    double scaleSizeValue;

    @Value("${scale.distance.value}")
    double scaleDistanceValue;

    @Value("${background.location.file}")
    String backgroundLocationFile;

    Node sun;
    List<Node> nodes;
    Xform galaxyGroup = new Xform();
    List<PeriodicTerm> periodicTermList;

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public Xform buildGalaxy() {
        Xform galaxyXform = new Xform();
        Xform starXform = new Xform();
        Xform lightXform = new Xform();
        Xform celestialBodyXform = new Xform();
        Xform nameXform = new Xform();
        Xform ellipseXform = new Xform();
        Xform satelliteXform = new Xform();

        // Init hierarchy
        galaxyXform.getChildren().add(celestialBodyXform);
        galaxyXform.getChildren().addAll(starXform);
        galaxyXform.getChildren().add(satelliteXform);
        galaxyXform.getChildren().add(ellipseXform);
        starXform.getChildren().add(lightXform);
        celestialBodyXform.getChildren().add(nameXform);

        // Load galaxies
        nodes = new ArrayList<>();
        List<Galaxy> galaxies = dataBusinessService.loadGalaxies();

        galaxies.forEach(galaxy -> {
            SegmentedSphereMesh segmentedSphereMesh = new SegmentedSphereMesh(2048.0);
            segmentedSphereMesh.setCullFace(CullFace.NONE);
            //segmentedSphereMesh.setTextureModeImage(getClass().getResource(BACKGROUND_IMAGE).toExternalForm());

            final PhongMaterial phongMaterial = new PhongMaterial();
            Image image = new Image(getClass().getResourceAsStream(backgroundLocationFile));

            phongMaterial.setSelfIlluminationMap(image);

            segmentedSphereMesh.setMaterial(phongMaterial);
            segmentedSphereMesh.getTransforms().add(new Rotate(60.2));
            segmentedSphereMesh.setRotationAxis(Rotate.Y_AXIS);

            galaxyXform.getChildren().add(segmentedSphereMesh);

            galaxy.getStars().forEach(star -> {
                final Node sphere = create(star);

                starXform.getChildren().add(sphere);
                lightXform.getChildren().add(createLightSource(sphere.getTranslateX(), sphere.getTranslateY(), sphere.getTranslateZ()));
//                nameXform.getChildren().add(createLabel(sphere));

                nodes.add(sphere);

                if(star.getDesignation().equalsIgnoreCase("sun")) {
                    sun = sphere;
                }
            });

            galaxy.getPlanets().forEach(planet -> {
                final Node parent = create(planet);

//                celestialBodyXform.getChildren().add(parent);
//                nameXform.getChildren().add(createLabel(parent));

                nodes.add(parent);

                List<Node> moons = new ArrayList<>();

//                    planet.getCelestialBodies().forEach(satellite -> {
//                        final Node child = createAndAnimateSphere(satellite, parent);
//
//                        nodes.add(child);
//                        moons.add(child);
//
//                        satelliteXform.getChildren().add(child);
//                    });
            });
        });

        galaxyGroup.getChildren().add(galaxyXform);

        return galaxyGroup;
    }

    private PointLight createLightSource(double x, double y, double z) {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE);

        pointLight.getTransforms().add(new Translate(x, y, z));

        return pointLight;
    }

    private Label attachLabel(Node node) {
        final Bounds bounds = node.localToScene(node.getBoundsInLocal());

        Label label = new Label(node.getId());
        label.setLayoutX(node.getTranslateX() + (bounds.getWidth() / 2));
        label.setLayoutY(node.getTranslateY() - (bounds.getHeight() / 3));
        label.setRotationAxis(Rotate.X_AXIS);
        label.setLabelFor(node);

        return label;
    }

    private Node create(CelestialBody celestialBody) {
        final PhysicalCharacteristic physicalCharacteristic = celestialBody.getPhysicalCharacteristic();
        final String designation = celestialBody.getDesignation().toLowerCase();
        final double axialTilt = physicalCharacteristic.getAxialTilt();
        final LocalDateTime localDateTime = LocalDateTime.of(1992, 10, 13, 0, 0, 0);
        final double JDE = JulianDay.getJulianDay(
                localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        final double t = JulianDay.inJulianCenturies(JDE);
        Coordinates coordinates = new Coordinates();
        double radius = 50;

        //TODO: Implement solar coordinates p.151

        if((celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.PLANET))) {
            radius = 8.5;

            if(designation.equalsIgnoreCase("earth")) {
                coordinates = planetaryPositionAppService.majorPlanet(celestialBody, t);
            }
        }

        return buildSphere(
                radius,
                coordinates.getX(),
                coordinates.getY(),
                coordinates.getZ(),
                axialTilt,
                designation
        );
    }



    public Node buildSphere(double radius, double x, double y, double z, double axialTilt, String id) {
        if(id.equalsIgnoreCase("sun")) {
            final String diffuseMap = SolarSystemTextures.getDiffuseMap(id.toLowerCase());
            textureUtils.setMaps(diffuseMap, null, null, diffuseMap);
        } else {
            textureUtils.setColor(SolarSystemTextures.getColor(id.toLowerCase()));
        }

        Sphere sphere = new Sphere(radius);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
        sphere.setMaterial(textureUtils.getPhongMaterial());
        sphere.setDrawMode(DrawMode.FILL);
        sphere.setCullFace(CullFace.BACK);
        sphere.setId(id);

        Rotate rotate = new Rotate(axialTilt);

        sphere.getTransforms().add(rotate);
        sphere.setRotationAxis(Rotate.Y_AXIS);

        return sphere;
    }
}
