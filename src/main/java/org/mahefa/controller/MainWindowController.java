package org.mahefa.controller;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import org.mahefa.common.constants.SolarSystemTextures;
import org.mahefa.common.utils.TextureUtils;
import org.mahefa.data.Xform;
import org.mahefa.data.javafx.Camera;
import org.mahefa.service.application.javafx.object.galaxy.GalaxyAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MainWindowController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    HBox hBox;

    @FXML
    SubScene subScene;

    @FXML
    Button planetView, innerView, outerView;

    @FXML
    Label currentDate, currentTime;

    @Autowired
    GalaxyAppService galaxyAppService;
    @Autowired TextureUtils textureUtils;

    @Value("${mouse.speed}") double mouseSpeed;
    @Value("${rotation.speed}") double rotationSpeed;
    @Value("${track.speed}") double trackSpeed;
    @Value("${date.format}") String dateFormat;
    @Value("${time.format}") String timFormat;

    @Value("${camera.near.clip: 0.0}") double nearClip;
    @Value("${camera.far.clip}") double farClip;
    @Value("${camera.initial.distance: 0.0}") double initialDistance;
    @Value("${camera,initial.x.angle: 0.0}") double initialXAngle;
    @Value("${camera.initial.y.angle: 0.0}") double initialYAngle;

    final Group root = new Group();
    final Xform world = new Xform();

    private double mouseOldX, mouseOldY, mousePosY, mousePosX, deltaX, deltaY;
    private Node currentPivot;
    private double currentPivotRadius;
    private Camera camera;

    @FXML
    private void initialize() {
        // Hide planet view and inner view by default
        planetView.setVisible(false);
        planetView.setManaged(false);
        planetView.managedProperty().bind(planetView.visibleProperty());

        outerView.setVisible(false);
        outerView.setManaged(false);
        outerView.managedProperty().bind(outerView.visibleProperty());
        innerView.managedProperty().bind(innerView.visibleProperty());

        camera = new Camera(nearClip, farClip, initialDistance, initialXAngle, initialYAngle);
        root.getChildren().addAll(world, camera.build());
        root.setDepthTest(DepthTest.ENABLE);

        showCurrentDateTime();
        world.getChildren().add(galaxyAppService.buildGalaxy());

        subScene.setRoot(root);
        subScene.setCamera(camera.getPerspectiveCamera());

        subScene.widthProperty().bind(anchorPane.widthProperty());
        subScene.heightProperty().bind(anchorPane.heightProperty());

        // Init event control
        nodeEventControl(galaxyAppService.getNodes());
        initEventControls();
    }

    private void showCurrentDateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();

            currentDate.setText(localDate.format(DateTimeFormatter.ofPattern(dateFormat)));
            currentTime.setText(localTime.format(DateTimeFormatter.ofPattern(timFormat)));
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void enterInnerView(ActionEvent event) {
        innerView.setVisible(false);
        planetView.setVisible(false);
        outerView.setVisible(true);
    }

    @FXML
    private void enterOuterView(ActionEvent event) {
        outerView.setVisible(false);
        planetView.setVisible(false);
        innerView.setVisible(true);

        camera.reset();

        resetPivot();
    }

    private void nodeEventControl(List<Node> nodes) {
        if(nodes != null && !nodes.isEmpty()) {
            nodes.forEach(node -> {
                node.setOnMouseClicked(event -> {
                    innerView.setVisible(false);
                    outerView.setVisible(false);
                    planetView.setVisible(true);
                    planetView.setText(node.getId().toUpperCase());

                    // Reset previous pivot
                    resetPivot();

                    // Load texture
                    textureUtils.setMaps(SolarSystemTextures.getDiffuseMap(node.getId().toLowerCase()), null, null, null);
                    ((Sphere)node).setMaterial(textureUtils.getPhongMaterial());

                    // Lock on pivot
                    currentPivot = node;
                    currentPivotRadius = ((Sphere)node).getRadius();

                    ((Sphere)node).setRadius(50.0);

                    camera.getPerspectiveCamera().setTranslateZ(-200.0);

                    AnimationTimer animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            Xform cameraXform = camera.getCameraXform();

                            // Lock camera on pivot's position
                            cameraXform.t.setX(currentPivot.getTranslateX());
                            cameraXform.t.setY(currentPivot.getTranslateY());
                            cameraXform.t.setZ(currentPivot.getTranslateZ());
                        }
                    };

                    animationTimer.start();
                });

                node.setOnMouseEntered(event -> subScene.setCursor(Cursor.HAND));
                node.setOnMouseExited(event -> subScene.setCursor(Cursor.DEFAULT));
                node.setCache(true);
            });
        }
    }

    private void initEventControls() {
        subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            PerspectiveCamera perspectiveCamera = camera.getPerspectiveCamera();
            double delta = event.getDeltaY();
            double z = perspectiveCamera.getTranslateZ();

            perspectiveCamera.setTranslateZ(z + delta * mouseSpeed);

            // TODO: update scale
        });

        subScene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        subScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getX();
            mousePosY = event.getY();
            deltaX = mousePosX - mouseOldX;
            deltaY = mousePosY - mouseOldY;

            Xform cameraXform = camera.getCameraXform();
            Xform cameraXform2 = camera.getCameraXform2();

            if(event.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() + deltaX * mouseSpeed * rotationSpeed);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + deltaY * mouseSpeed * rotationSpeed);
            } else if(event.isSecondaryButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + deltaX * mouseSpeed * trackSpeed);
                cameraXform2.t.setY(cameraXform2.t.getY() + deltaY * mouseSpeed * trackSpeed);
            }
        });
    }

    /**
     * Reset pivot
     */
    void resetPivot() {
        if(currentPivot != null) {
            final String id = currentPivot.getId();

            if(id.equalsIgnoreCase("sun")) {
                final String diffuseMap = SolarSystemTextures.getDiffuseMap(id.toLowerCase());
                textureUtils.setMaps(diffuseMap, null, null, diffuseMap);
            } else {
                textureUtils.setColor(SolarSystemTextures.getColor(id.toLowerCase()));
            }

            ((Sphere) currentPivot).setMaterial(textureUtils.getPhongMaterial());
            ((Sphere) currentPivot).setRadius(currentPivotRadius);
            currentPivot = null;
        }
    }
}
