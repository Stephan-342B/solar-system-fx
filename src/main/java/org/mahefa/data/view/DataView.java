package org.mahefa.data.view;

import javafx.beans.property.SimpleStringProperty;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;

public class DataView {

    private final SimpleStringProperty designation;
    private final SimpleStringProperty ra;
    private final SimpleStringProperty decl;
    private final SimpleStringProperty magnitude;
    private final SimpleStringProperty size;
    private final SimpleStringProperty phase;
    private final SimpleStringProperty rise;
    private final SimpleStringProperty transit;
    private final SimpleStringProperty set;

    public DataView(String designation, Double ra, Double decl) {
        this.designation = new SimpleStringProperty(designation);
        this.ra = new SimpleStringProperty(Angle.convertDecimalDegreeToDMS(ra));
        this.decl = new SimpleStringProperty(String.format("%s°", AstroMath.round(decl, 1e2)));
        this.magnitude = new SimpleStringProperty("0.0");
        this.size = new SimpleStringProperty("0.0\'");
        this.phase = new SimpleStringProperty("0%");
        this.rise = new SimpleStringProperty("00:00 am/pm");
        this.transit = new SimpleStringProperty("00:00 am/pm");
        this.set = new SimpleStringProperty("00:00 am/pm");
    }

    public DataView(String designation, Double ra, Double decl, String magnitude, String size, String phase, String rise, String transit, String set) {
        this.designation = new SimpleStringProperty(designation);
        this.ra = new SimpleStringProperty(Angle.convertDecimalDegreeToDMS(ra));
        this.decl = new SimpleStringProperty(String.format("%s°", AstroMath.round(decl, 1e2)));
        this.magnitude = new SimpleStringProperty(magnitude);
        this.size = new SimpleStringProperty(null);
        this.phase = new SimpleStringProperty(phase);
        this.rise = new SimpleStringProperty(rise);
        this.transit = new SimpleStringProperty(transit);
        this.set = new SimpleStringProperty(set);
    }

    public String getDesignation() {
        return designation.get().toUpperCase();
    }

    public SimpleStringProperty designationProperty() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public String getRa() {
        return ra.get();
    }

    public SimpleStringProperty raProperty() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra.set(ra);
    }

    public String getDecl() {
        return decl.get();
    }

    public SimpleStringProperty declProperty() {
        return decl;
    }

    public void setDecl(String decl) {
        this.decl.set(decl);
    }

    public String getMagnitude() {
        return magnitude.get();
    }

    public SimpleStringProperty magnitudeProperty() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude.set(magnitude);
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public String getPhase() {
        return phase.get();
    }

    public SimpleStringProperty phaseProperty() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase.set(phase);
    }

    public String getRise() {
        return rise.get();
    }

    public SimpleStringProperty riseProperty() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise.set(rise);
    }

    public String getTransit() {
        return transit.get();
    }

    public SimpleStringProperty transitProperty() {
        return transit;
    }

    public void setTransit(String transit) {
        this.transit.set(transit);
    }

    public String getSet() {
        return set.get();
    }

    public SimpleStringProperty setProperty() {
        return set;
    }

    public void setSet(String set) {
        this.set.set(set);
    }
}
