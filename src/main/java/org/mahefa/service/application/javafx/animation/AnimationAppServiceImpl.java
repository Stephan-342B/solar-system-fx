package org.mahefa.service.application.javafx.animation;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.data.oracle.Xform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.vecmath.Vector3d;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnimationAppServiceImpl implements AnimationAppService {

    @Value("${fade.timer}") int fadeTimer;
    double old = 0d;

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
    public void fadeIn(final Node node, final Pane container) {
        FadeTransition fadeTransition = fade(node, true);

//        if(!container.getChildren().contains(node))
//            container.getChildren().add(node);

        fadeTransition.setOnFinished(event -> node.setVisible(true));
        fadeTransition.play();
    }

    @Override
    public void fadeOut(final Node node, final Pane container) {
        if(container.getChildren().contains(node)) {
            FadeTransition fadeTransition = fade(node, false);
            fadeTransition.setOnFinished(event -> {
                node.setVisible(false);
//                container.getChildren().remove(node);
            });
            fadeTransition.play();
        }
    }

    @Override
    public void move(Xform xform, Vector3d vector3dFrom, Vector3d vector3dTo) {
        final double x = vector3dFrom.getX();
        final double y = vector3dFrom.getY();
        final double z = vector3dFrom.getZ();

        final double angle = Angle.getAngleFromPoints(vector3dFrom, vector3dTo);
        final double radius = Math.sqrt((x * x) + (y * y) + (z * z));
        final double length = Angle.getArcLength(angle, radius, false);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(xform.ry.angleProperty(), old)
                ),
                new KeyFrame(
                        Duration.millis(5000),
                        new KeyValue(xform.ry.angleProperty(), angle+old)
                )
        );

        Vector3D vector3D = new Vector3D();
        SequentialTransition sequentialTransition = new SequentialTransition(timeline);
        sequentialTransition.setCycleCount(1);
//        sequentialTransition.setInterpolator(Interpolator.EASE_BOTH);
        sequentialTransition.setDelay(Duration.millis(5000));
        sequentialTransition.setOnFinished(event -> old += angle);
        sequentialTransition.play();
    }

    @Override
    public void lock(Xform xform, Node node) {
//        AnimationTimer animationTimer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if(xform.t.getX() != node.getTranslateX() || xform.t.getY() != node.getTranslateY() || xform.t.getZ() != node.getTranslateZ()) {
//                    xform.t.setX(node.getTranslateX());
//                    xform.t.setY(node.getTranslateY());
//                    xform.t.setZ(node.getTranslateZ());
//                }
//            }
//        };
//        animationTimer.start();
    }

    FadeTransition fade(final Node node, boolean in) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(fadeTimer), node);
        fadeTransition.setFromValue((in) ? 0.0 : 1.0);
        fadeTransition.setToValue((in) ? 1.0 : 0.0);
        fadeTransition.setInterpolator((in) ? Interpolator.EASE_IN : Interpolator.EASE_BOTH);

        return fadeTransition;
    }
}
