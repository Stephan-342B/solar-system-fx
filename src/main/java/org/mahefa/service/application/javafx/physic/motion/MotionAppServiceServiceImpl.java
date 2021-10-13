package org.mahefa.service.application.javafx.physic.motion;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MotionAppServiceServiceImpl implements MotionAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MotionAppServiceServiceImpl.class);

    private static final double ROTATION_SPEED = 0.01;
    private static final double ORBITAL_SPEED = 0.00003;

    @Value("${scale.size.value}")
    double scaleSizeValue;

    @Value("${scale.distance.value}")
    double scaleDistanceValue;

    @Override
    public void rotate(Node node, final double degreePerSecond) {
        AnimationTimer rotationAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                node.rotateProperty().set(node.getRotate() + degreePerSecond * ROTATION_SPEED * -1);
                node.setRotationAxis(new Point3D(0, 1, 0));
            }
        };

        rotationAnimationTimer.start();
    }

    /**
     * x(α) = Rx cos(α)
     * y(α) = Ry sin(α)
     *
     * @param node
     * @param radiusX
     * @param radiusY
     * @param rotationDegree
     * @param inclination
     */
    @Override
    public void orbit(Node node,  double radiusX, double radiusY, double rotationDegree, double inclination) {
        AnimationTimer revolutionTimer = new AnimationTimer() {
            double t = 0.0;
            double rd = rotationDegree * ORBITAL_SPEED;

            @Override
            public void handle(long now) {
                t += rd;

                node.translateXProperty().set((radiusY * Math.cos(t)) + (inclination * Math.sin(t)));
                node.translateYProperty().set(Math.cos(t) * inclination);
                node.translateZProperty().set((radiusX * Math.sin(t) + (inclination * Math.cos(t))));
            }
        };

        revolutionTimer.start();
    }

    /**
     * x(a) = h + Rx cos(a)
     * y(a) = k + Ry sin(a)
     *
     * where h and k are coordinates
     *
     * Rotate
     * x = a cos(t) + h sin(t)
     * y = b sin(t) + h cos(t)
     *
     * for 0 <= t <= 2PI
     * @param parentNode
     * @param node
     * @param radiusX
     * @param radiusY
     * @param inclination
     */
    @Override
    public void orbit(Node parentNode, Node node, double radiusX, double radiusY, double inclination) {
        AnimationTimer animationTimer = new AnimationTimer() {
            double originalX = 0.0 , originalZ = 0.0;

            @Override
            public void handle(long now) {
                double xPrime = parentNode.getTranslateX();
                double zPrime = parentNode.getTranslateZ();
                double tan = Math.atan2(originalX - xPrime, originalZ - zPrime);
                double t = 180 - Math.toDegrees(tan);

                node.translateXProperty().set((parentNode.getTranslateX() + (radiusY * Math.cos(t))) + (inclination * Math.sin(t)));
                node.translateYProperty().set(parentNode.getTranslateY() + (Math.cos(t) * inclination));
                node.translateZProperty().set((parentNode.getTranslateZ() + (radiusX * Math.sin(t)) + (inclination * Math.cos(t))));

                originalX = xPrime;
                originalZ = zPrime;
            }
        };

        animationTimer.start();
    }
}
