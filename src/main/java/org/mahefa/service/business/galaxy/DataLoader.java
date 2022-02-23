package org.mahefa.service.business.galaxy;

import org.mahefa.data.Galaxy;

import java.io.IOException;
import java.util.List;

public interface DataLoader {

    /**
     * @return
     */
    List<Galaxy> loadGalaxies() throws Exception;

}
