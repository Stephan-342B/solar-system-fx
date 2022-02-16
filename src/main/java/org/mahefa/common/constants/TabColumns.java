package org.mahefa.common.constants;

public enum TabColumns {

    _BLANK(""),
    RIGHT_ASCENSION("R.A."),
    DECLINATION("Dec."),
    MAGNITUDE("Magnitude"),
    SIZE("Size"),
    PHASE("Phase"),
    RISE("Rise"),
    TRANSIT("Transit"),
    SET("Set");

    final String column;

    TabColumns(String column) {
        this.column = column;
    }

    public String toValue() {
        return column;
    }
}
