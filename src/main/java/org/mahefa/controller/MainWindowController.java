package org.mahefa.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.mahefa.data.*;
import org.mahefa.service.application.GalaxyApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainWindowController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private SubScene subScene;

    @Autowired
    private GalaxyApplication galaxyApplication;

    final Group root = new Group();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform(Xform.RotateOrder.ZYX);
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

    private double mouseOldX, mouseOldY, mousePosY, mousePosX, deltaX, deltaY;
    private Node currentPivotNode;

    private static final double CAMERA_NEAR_CLIP = 0.01;
    private static final double CAMERA_FAR_CLIP = 10000000.0;
    private static final double CAMERA_INITIAL_DISTANCE = -1800;
    private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;

    private static final double MOUSE_SPEED = 0.6;
    private static final double ROTATION_SPEED = 3.0;
    private static final double TRACK_SPEED = 0.3;
    private static final double SHIFT_MULTIPLIER = 10.0;

    @FXML
    private void initialize() {
        root.getChildren().addAll(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        galaxyApplication.buildGalaxy(world);

        subScene.setRoot(root);
        subScene.setCamera(camera);

        subScene.widthProperty().bind(anchorPane.widthProperty());
        subScene.heightProperty().bind(anchorPane.heightProperty());

        nodeEventControl(galaxyApplication.getNodes());
        initEventControls();
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        //camera.setFieldOfView(180);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void nodeEventControl(List<Node> nodes) {
        if(nodes != null && !nodes.isEmpty()) {
            nodes.forEach(node -> {
                node.setOnMouseClicked(event -> {
                    currentPivotNode = node;

                    camera.setTranslateZ(-50);

                    AnimationTimer animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            // Lock camera on pivot's position
                            cameraXform.t.setX(currentPivotNode.getTranslateX());
                            cameraXform.t.setY(currentPivotNode.getTranslateY());
                            cameraXform.t.setZ(currentPivotNode.getTranslateZ());
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
            double delta = event.getDeltaY();
            double z = camera.getTranslateZ();
            double newZ = z + delta * MOUSE_SPEED;

            camera.setTranslateZ(newZ);

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

            if(event.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() + deltaX * MOUSE_SPEED * ROTATION_SPEED);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + deltaY * MOUSE_SPEED * ROTATION_SPEED);
            } else if(event.isSecondaryButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + deltaX * MOUSE_SPEED * TRACK_SPEED);
                cameraXform2.t.setY(cameraXform2.t.getY() + deltaY * MOUSE_SPEED * TRACK_SPEED);
            }
        });
    }
}
