package org.mahefa.controller.javafx;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import org.mahefa.data.oracle.Xform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Camera {

    final PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform(Xform.RotateOrder.YZX);
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

    @Value("${camera.near.clip: 0.0}") double nearClip;
    @Value("${camera.far.clip}") double farClip;
    @Value("${camera.initial.distance: 0.0}") double initialDistance;
    @Value("${camera,initial.x.angle: 0.0}") double initialXAngle;
    @Value("${camera.initial.y.angle: 0.0}") double initialYAngle;
    @Value("${camera.close.range}") double closeRange;

    public Camera() {
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return perspectiveCamera;
    }

    public Xform getCameraXform() {
        return cameraXform;
    }

    public Xform getCameraXform2() {
        return cameraXform2;
    }

    public Xform getCameraXform3() {
        return cameraXform3;
    }

    public Xform build() {
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(perspectiveCamera);

        perspectiveCamera.setNearClip(nearClip);
        perspectiveCamera.setFarClip(farClip);
        perspectiveCamera.setTranslateZ(initialDistance);
        cameraXform.rx.setAngle(initialXAngle);
        cameraXform.ry.setAngle(initialYAngle);

        return cameraXform;
    }

    public void gettingCloser() {
        executeTimeLine(initialDistance, closeRange);
    }

    public void movingFarAway() {
        executeTimeLine(closeRange, initialDistance);
    }

    public AnimationTimer lock(Node currentPivot) {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cameraXform.t.setX(currentPivot.getTranslateX());
                cameraXform.t.setY(currentPivot.getTranslateY());
                cameraXform.t.setZ(currentPivot.getTranslateZ());
            }
        };

        animationTimer.start();

        return animationTimer;
    }

    public void reset() {
        cameraXform.rx.setAngle(initialXAngle);
        cameraXform.ry.setAngle(initialYAngle);

        perspectiveCamera.setNearClip(nearClip);
        perspectiveCamera.setFarClip(farClip);
        perspectiveCamera.setTranslateZ(initialDistance);

        cameraXform.t.setX(0.0);
        cameraXform.t.setY(0.0);
        cameraXform.t.setZ(0.0);
    }

    void executeTimeLine(double initialValue, double finalValue) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(perspectiveCamera.translateZProperty(), initialValue)
                ),
                new KeyFrame(
                        Duration.seconds(1.5),
                        new KeyValue(perspectiveCamera.translateZProperty(), finalValue)
                )
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
