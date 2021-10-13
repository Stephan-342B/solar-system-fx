package org.mahefa.data.javafx;

import javafx.scene.PerspectiveCamera;
import org.mahefa.data.Xform;

public class Camera {

    final PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform(Xform.RotateOrder.ZYX);
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();

    private double nearClip;
    private double farClip;
    private double initialDistance;
    private double initialXAngle;
    private double initialYAngle;

    public Camera(double nearClip, double farClip, double initialDistance, double initialXAngle, double initialYAngle) {
        this.nearClip = nearClip;
        this.farClip = farClip;
        this.initialDistance = initialDistance;
        this.initialXAngle = initialXAngle;
        this.initialYAngle = initialYAngle;
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

    public void reset() {
        cameraXform.rx.setAngle(initialXAngle);
        cameraXform.ry.setAngle(initialYAngle);

        perspectiveCamera.setNearClip(nearClip);
        perspectiveCamera.setFarClip(farClip);
        perspectiveCamera.setTranslateZ(initialDistance);

        cameraXform.t.setX(0.0);
        cameraXform.t.setY(0.0);
//        cameraXform.t.setZ(0.0);
    }
}
