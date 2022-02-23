package org.mahefa.service.application.javafx.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.data.oracle.Xform;

public interface Animation {

    void add(String key, AnimationTimer animationTimer);
    void remove(String... keys);

    void fadeIn(Node node);
    void fadeOut(Node node);

    void move(Xform xform, Vector3D vFrom, Vector3D vTo);

    AnimationTimer rotate(Node node, final double degreePerSecond);

}
