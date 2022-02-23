package org.mahefa.service.application.javafx.animation;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.data.oracle.Xform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnimationImpl implements Animation {

    @Value("${fade.timer}") int fadeTimer;
    @Value("${camera.motion.timer}") int cameraMotionTimer;
    @Value("${camera.close.range}") double closeRangeDistance;
    @Value("${camera.initial.distance}") double initialDistance;

    double α_old = 0d;

    ConcurrentHashMap<String, AnimationTimer> concurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void add(String key, AnimationTimer animationTimer) {
        concurrentHashMap.put(key, animationTimer);
    }

    @Override
    public void remove(String... keys) {
        if(keys == null || keys.length == 0 || concurrentHashMap.size() == 0) {
            return;
        }

        for(int k = 0; k < keys.length; k++) {
            AnimationTimer animationTimer = concurrentHashMap.get(keys[k]);

            if(animationTimer != null) {
                animationTimer.stop();
                concurrentHashMap.remove(keys[k], animationTimer);
            }
        }
    }

    @Override
    public void fadeIn(final Node node) {
        FadeTransition fadeTransition = fade(node, true);
        fadeTransition.setOnFinished(event -> node.setVisible(true));
        fadeTransition.play();
    }

    @Override
    public void fadeOut(final Node node) {
        FadeTransition fadeTransition = fade(node, false);
        fadeTransition.setOnFinished(event -> node.setVisible(false));
        fadeTransition.play();
    }

    @Override
    public void move(Xform xform, Vector3D vFrom, Vector3D vTo) {
        final double x = vTo.getX();
        final double y = vTo.getY();
        final double z = vTo.getZ();

        Xform cam2 = (Xform) xform.getChildren().get(0);
        Xform cam3 = (Xform) cam2.getChildren().get(0);
        PerspectiveCamera perspectiveCamera = (PerspectiveCamera) cam3.getChildren().get(0);

        Timeline timeline;

        if(vTo != Vector3D.ZERO) {
            vFrom.normalize();
            vTo.normalize();
            final double dot = vFrom.dotProduct(vTo);
            final double α = Math.toDegrees(Math.acos(dot));

            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0),
                            new KeyValue(xform.ry.angleProperty(), α_old),
                            new KeyValue(xform.t.xProperty(), xform.t.getX()),
                            new KeyValue(xform.t.yProperty(), xform.t.getY()),
                            new KeyValue(xform.t.zProperty(), xform.t.getZ()),
                            new KeyValue(perspectiveCamera.translateZProperty(), perspectiveCamera.getTranslateZ())
                    ),
                    new KeyFrame(
                            Duration.millis(cameraMotionTimer),
                            new KeyValue(xform.ry.angleProperty(), α),
                            new KeyValue(xform.t.xProperty(), x),
                            new KeyValue(xform.t.yProperty(), y),
                            new KeyValue(xform.t.zProperty(), z),
                            new KeyValue(perspectiveCamera.translateZProperty(), closeRangeDistance)
                    )
            );
            timeline.setOnFinished(event -> α_old = α);
        } else {
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0),
                            new KeyValue(perspectiveCamera.translateZProperty(), perspectiveCamera.getTranslateZ()),
                            new KeyValue(xform.ry.angleProperty(), α_old),
                            new KeyValue(xform.t.xProperty(), xform.t.getX()),
                            new KeyValue(xform.t.yProperty(), xform.t.getY()),
                            new KeyValue(xform.t.zProperty(), xform.t.getZ())
                    ),
                    new KeyFrame(
                            Duration.millis(cameraMotionTimer),
                            new KeyValue(perspectiveCamera.translateZProperty(), initialDistance),
                            new KeyValue(xform.ry.angleProperty(), 0),
                            new KeyValue(xform.t.xProperty(), x),
                            new KeyValue(xform.t.yProperty(), y),
                            new KeyValue(xform.t.zProperty(), z)
                    )
            );
            timeline.setOnFinished(event -> α_old = 0);
        }

        timeline.play();
    }

    @Override
    public AnimationTimer rotate(Node node, final double degreePerSecond) {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                node.rotateProperty().set(node.getRotate() + degreePerSecond);
            }
        };
        animationTimer.start();

        return animationTimer;
    }

    FadeTransition fade(final Node node, boolean in) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeTimer), node);
        fadeTransition.setFromValue((in) ? 0.0 : 1.0);
        fadeTransition.setToValue((in) ? 1.0 : 0.0);
        fadeTransition.setInterpolator((in) ? Interpolator.EASE_IN : Interpolator.EASE_BOTH);

        return fadeTransition;
    }
}
