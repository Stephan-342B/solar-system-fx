package org.mahefa.service.application.javafx.object.galaxy;

import org.mahefa.data.oracle.Xform;
import org.mahefa.data.view.DataView;

import java.util.List;

public interface Galaxy {

    Xform buildGalaxy(final double JDE);

    List<DataView> getInfo(final double JDE);

}
