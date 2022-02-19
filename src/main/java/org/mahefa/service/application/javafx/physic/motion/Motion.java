package org.mahefa.service.application.javafx.physic.motion;

import javafx.scene.Node;

public interface Motion {
    void orbit(Node node,  double radiusX, double radiusY, double rotationDegree, double inclination);
    void orbit(Node parentNode, Node node, double radiusX, double radiusY, double inclination);


}
