package org.mahefa.service.application.javafx.object.galaxy;

import javafx.scene.Node;
import org.mahefa.data.Xform;

import java.util.List;

public interface GalaxyAppService {

    Xform buildGalaxy();
    List<Node> getNodes();

}
