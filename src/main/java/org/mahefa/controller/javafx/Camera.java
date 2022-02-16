package org.mahefa.controller.javafx;

import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import org.mahefa.data.oracle.Xform;
import org.mahefa.service.application.javafx.animation.AnimationAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.vecmath.Vector3d;

@Component
public class Camera {

    final PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform(Xform.RotateOrder.YXZ);
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

    @Value("${camera.near.clip: 0.0}") double nearClip;
    @Value("${camera.far.clip}") double farClip;
    @Value("${camera.initial.distance: 0.0}") double initialDistance;
    @Value("${camera,initial.x.angle: 0.0}") double initialXAngle;
    @Value("${camera.initial.y.angle: 0.0}") double initialYAngle;
    @Value("${camera.close.range}") double closeRange;

    @Autowired
    AnimationAppService animationAppService;

    public Camera() {}

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

    public void lock(Node pivotA, Node pivotB) {
        Vector3d coordinateFrom = new Vector3d(perspectiveCamera.getTranslateX(), perspectiveCamera.getTranslateY(), perspectiveCamera.getTranslateZ());
                //new Coordinate(cameraXform.t.getX(), cameraXform.t.getY(), cameraXform.t.getZ());

        if(pivotB != null) {
            if(pivotA != null) {
                coordinateFrom.setX(pivotA.getTranslateX());
                coordinateFrom.setY(pivotA.getTranslateY());
                coordinateFrom.setZ(pivotA.getTranslateZ());
            }

            animationAppService.move(
                    cameraXform,
                    coordinateFrom,
                    new Vector3d(pivotB.getTranslateX(), pivotB.getTranslateY(), pivotB.getTranslateZ())
            );
        }
    }

    public void resetPositionFrom(Node pivot) {
        if(pivot != null) {
//            animationAppService.move(
//                    perspectiveCamera,
//                    new Coordinate(pivot.getTranslateX(), pivot.getTranslateY(), pivot.getTranslateZ()),
//                    new Coordinate(0d, 0d, initialDistance)
//            );
        }
    }

    public void reset() {
        cameraXform.rx.setAngle(initialXAngle);
        cameraXform.ry.setAngle(initialYAngle);

        perspectiveCamera.setNearClip(nearClip);
        perspectiveCamera.setFarClip(farClip);
        perspectiveCamera.setTranslateZ(initialDistance);
    }
}
