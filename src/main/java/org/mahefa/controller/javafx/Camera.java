package org.mahefa.controller.javafx;

import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.data.oracle.Xform;
import org.mahefa.service.application.javafx.animation.Animation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    Animation animation;

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

    public void lockOn(Node pivot) {
        if(pivot == null)
            System.out.println("Coordinates not specified");

        Vector3D vFrom = new Vector3D(perspectiveCamera.getTranslateX(), perspectiveCamera.getTranslateY(), perspectiveCamera.getTranslateZ());

        if(pivot != null) {
            Vector3D vTo = new Vector3D(pivot.getTranslateX(), pivot.getTranslateY(), pivot.getTranslateZ());

            // Already at the specified coordinate
            if(vFrom == vTo)
                return;

            animation.move(cameraXform, vFrom, vTo);
        }
    }

    public void release() {
        Vector3D vFrom = new Vector3D(perspectiveCamera.getTranslateX(), perspectiveCamera.getTranslateY(), perspectiveCamera.getTranslateZ());
        Vector3D vTo = Vector3D.ZERO;

        // Already at the specified coordinate
        if(vFrom == vTo)
            return;

        animation.move(cameraXform, vFrom, vTo);
    }
}
