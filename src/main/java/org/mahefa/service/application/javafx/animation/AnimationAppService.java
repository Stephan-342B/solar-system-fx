package org.mahefa.service.application.javafx.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.fxyz3d.geometry.Vector3D;
import org.mahefa.data.oracle.Xform;

public interface AnimationAppService {

    void add(String key, AnimationTimer animationTimer);
    void remove(String... keys);

    void fadeIn(final Node node, final Pane container);
    void fadeOut(Node node, final Pane container);

    void move(Xform xform, Vector3D vector3dFrom, Vector3D vector3dTo);
    void lock(Xform xform, Node node);

}
