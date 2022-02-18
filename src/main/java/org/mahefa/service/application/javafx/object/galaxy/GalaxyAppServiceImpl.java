package org.mahefa.service.application.javafx.object.galaxy;

import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import org.fxyz3d.shapes.primitives.SegmentedSphereMesh;
import org.mahefa.common.constants.CelestialBodyCategory;
import org.mahefa.common.utils.NodeUtils;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.Galaxy;
import org.mahefa.data.meeus.jean.Coordinate;
import org.mahefa.data.meeus.jean.GeocentricCoordinate;
import org.mahefa.data.oracle.Xform;
import org.mahefa.data.view.DataView;
import org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position.PlanetaryPositionAppService;
import org.mahefa.service.application.astro.meeus.jean.algorithm.planetary_position.vsop87.Vsop87;
import org.mahefa.service.business.galaxy.DataBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class GalaxyAppServiceImpl implements GalaxyAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GalaxyAppServiceImpl.class);

    @Autowired Vsop87 vsop87;
    @Autowired PlanetaryPositionAppService planetaryPositionAppService;
    @Autowired DataBusinessService dataBusinessService;

    @Value("${scale.size.value}") double scaleSizeValue;
    @Value("${scale.distance.value}") double scaleDistanceValue;
    @Value("${background.location.file}") String backgroundLocationFile;

    Xform galaxyGroup = new Xform();

    public Map<String, Coordinate> coordinates;
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
    public Xform buildGalaxy(final double t) {
        Xform galaxyXform = new Xform();
        Xform starXform = new Xform();
        Xform celestialBodyXform = new Xform();
        Xform nameXform = new Xform();
        Xform ellipseXform = new Xform();
        Xform satelliteXform = new Xform();

        // Init hierarchy
        galaxyXform.getChildren().add(celestialBodyXform);
        galaxyXform.getChildren().addAll(starXform);
        galaxyXform.getChildren().add(satelliteXform);
        galaxyXform.getChildren().add(ellipseXform);
        galaxyXform.getChildren().add(nameXform);

        coordinates = new HashMap<>();

        galaxies.forEach(galaxy -> {
            SegmentedSphereMesh segmentedSphereMesh = new SegmentedSphereMesh(1e5);
            segmentedSphereMesh.setCullFace(CullFace.NONE);

            final PhongMaterial phongMaterial = new PhongMaterial();
            phongMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream(backgroundLocationFile)));

            segmentedSphereMesh.setMaterial(phongMaterial);
            segmentedSphereMesh.getTransforms().add(new Rotate(60.2));
            segmentedSphereMesh.setRotationAxis(Rotate.Y_AXIS);
            segmentedSphereMesh.setCache(true);
            segmentedSphereMesh.setCacheHint(CacheHint.QUALITY);

            galaxyXform.getChildren().add(segmentedSphereMesh);


            (galaxy.getStars()).forEach(star -> {
                final Node sphere = getSphere(star, t);

                if(sphere != null)
                    starXform.getChildren()
                            .addAll(sphere, NodeUtils.createLightSource(sphere.getTranslateX(), sphere.getTranslateY(), sphere.getTranslateZ()));
            });

            (galaxy.getPlanets()).stream()
                    .filter(celestialBody -> !celestialBody.getDesignation().equalsIgnoreCase("earth"))
                    .forEach(planet -> {
                final Node parent = getSphere(planet, t);

                if(parent != null)
                    celestialBodyXform.getChildren().add(parent);
            });
        });

        galaxyGroup.getChildren().add(galaxyXform);

        return galaxyGroup;
    }

    @Override
    public List<DataView> getInfo(final double JDE) {
        List<DataView> dataViews = new ArrayList<>();

        galaxies.forEach(galaxy -> {
            (galaxy.getPlanets()).stream()
                    .filter(celestialBody -> !celestialBody.getDesignation().equalsIgnoreCase("earth"))
                    .forEach(planet -> {
                        try {
                            GeocentricCoordinate geocentricCoordinate = vsop87.getMajorPlanetCoord(planet.getDesignation(), JDE);
                            dataViews.add(new DataView(planet.getDesignation(), geocentricCoordinate.getRightAscension(), geocentricCoordinate.getDeclination()));
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }

                    });
        });

        return dataViews;
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

    private Node getSphere(CelestialBody celestialBody, final double t) {
        Coordinate coordinate = new Coordinate();

        try {
            // TODO: Implement rising, transit and setting p.102
            // TODO: Implement solar coordinates p.156

            final String designation = celestialBody.getDesignation().toLowerCase();

            if(celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.PLANET)) {
                if(!designation.equalsIgnoreCase("earth")) {
                    coordinate = planetaryPositionAppService.getHeliocentricCoordinates(designation, t);
                    coordinates.put(designation, coordinate);
                }
            }

            return NodeUtils.buildSphere(celestialBody, coordinate, scaleDistanceValue);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }
}
