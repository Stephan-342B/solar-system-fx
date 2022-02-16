package org.mahefa.service.application.javafx.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.mahefa.data.oracle.Xform;

import javax.vecmath.Vector3d;

public interface AnimationAppService {

    void add(String key, AnimationTimer animationTimer);
    void remove(String... keys);

    void fadeIn(final Node node, final Pane container);
    void fadeOut(Node node, final Pane container);

    void move(Xform xform, Vector3d vector3dFrom, Vector3d vector3dTo);
    void lock(Xform xform, Node node);

}
