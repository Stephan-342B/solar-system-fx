package org.mahefa.service.application.javafx.object.galaxy;

import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
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
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.Galaxy;
import org.mahefa.data.PhysicalCharacteristic;
import org.mahefa.data.oracle.Xform;
import org.mahefa.data.meeus.jean.Coordinates;
import org.mahefa.data.meeus.jean.PeriodicTerm;
import org.mahefa.service.application.meeus.jean.algorithm.planetary_position.PlanetaryPositionAppService;
import org.mahefa.service.business.galaxy.DataBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GalaxyAppServiceImpl implements GalaxyAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GalaxyAppServiceImpl.class);

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
    Xform galaxyGroup = new Xform();
    List<PeriodicTerm> periodicTermList;
    List<Coordinates> coordinatesList = new ArrayList<>();

    public static List<Galaxy> galaxies;

    @PostConstruct
    private void init() {
        // Load galaxies
        galaxies = dataBusinessService.loadGalaxies();
    }

    @Override
    public CelestialBody getCelestialBody(String id) {
        Optional<CelestialBody> optionalCelestialBody = galaxies.get(0).getPlanets()
                .stream().filter(celestialBody -> celestialBody.getDesignation().equalsIgnoreCase(id)).findFirst();

        if(!optionalCelestialBody.isPresent()) {
            LOGGER.error("No celestial body found with this id: " + id);
            return null;
        }

        return optionalCelestialBody.get();
    }

    @Override
    public Xform buildGalaxy() {
        Xform galaxyXform = new Xform();
        Xform starXform = new Xform();
        Xform lightXform = new Xform();
        Xform celestialBodyXform = new Xform();
        Xform nameXform = new Xform(Xform.RotateOrder.Y);
        Xform ellipseXform = new Xform();
        Xform satelliteXform = new Xform();

        // Init hierarchy
        galaxyXform.getChildren().add(celestialBodyXform);
        galaxyXform.getChildren().addAll(starXform);
        galaxyXform.getChildren().add(satelliteXform);
        galaxyXform.getChildren().add(ellipseXform);
        starXform.getChildren().add(lightXform);
        galaxyXform.getChildren().add(nameXform);

        galaxies.forEach(galaxy -> {
            SegmentedSphereMesh segmentedSphereMesh = new SegmentedSphereMesh(1e5);
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
                final Node label = attachLabel(sphere);

                starXform.getChildren().add(sphere);
                lightXform.getChildren().add(createLightSource(sphere.getTranslateX(), sphere.getTranslateY(), sphere.getTranslateZ()));
                nameXform.getChildren().add(label);

                if(star.getDesignation().equalsIgnoreCase("sun")) {
                    sun = sphere;
                }
            });

            galaxy.getPlanets().forEach(planet -> {
                if(!planet.getDesignation().equalsIgnoreCase("earth")) {
                    final Node parent = create(planet);
                    final Node label = attachLabel(parent);

                    celestialBodyXform.getChildren().add(parent);
                    nameXform.getChildren().add(label);
                }

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

    @Override
    public List<Coordinates> getCurrentCoordinates() {
        return coordinatesList;
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
        label.setTranslateX(node.getTranslateX() + (bounds.getWidth() / 2));
        label.setTranslateY(node.getTranslateY() - (bounds.getHeight() / 2));
        label.setTranslateZ(-1);
//        label.setLayoutX(label.getTranslateX());
//        label.setLayoutY(label.getTranslateY());
        label.setRotationAxis(Rotate.Y_AXIS);
        label.setLabelFor(node);

        return label;
    }

    private Node create(CelestialBody celestialBody) {
        final PhysicalCharacteristic physicalCharacteristic = celestialBody.getPhysicalCharacteristic();
        final String designation = celestialBody.getDesignation().toLowerCase();
        final double axialTilt = physicalCharacteristic.getAxialTilt();
        final LocalDateTime localDateTime = LocalDateTime.now(); //of(1992, 10, 13, 0, 0, 0);
        final double JDE = JulianDay.getJulianDay(
                localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
        );
        final double t = JulianDay.inJulianCenturies(JDE);
        Coordinates coordinates = new Coordinates();
        double radius = 8.0;

        //TODO: Implement solar coordinates p.151

        if((celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.PLANET))) {
            if(!designation.equalsIgnoreCase("earth")) {
                coordinates = planetaryPositionAppService.majorPlanet(celestialBody, t);
            }
        }


        coordinatesList.add(coordinates);

        return buildSphere(
                radius,
                (coordinates.getX() * AstroMath.AU * 1e-3) * scaleDistanceValue,
                (coordinates.getZ() * AstroMath.AU * 1e-3) * scaleDistanceValue,
                (coordinates.getY() * AstroMath.AU * 1e-3) * scaleDistanceValue,
                axialTilt,
                designation,
                physicalCharacteristic.isRingSystem()
        );
    }



    public Node buildSphere(double radius, double x, double y, double z, double axialTilt, String id, boolean isRingSystem) {
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
        sphere.setCache(true);
        sphere.setCacheHint(CacheHint.SPEED);

        if(isRingSystem) {
            sphere.getStyleClass().add("isRingSystem");
        }

        return sphere;
    }
}
