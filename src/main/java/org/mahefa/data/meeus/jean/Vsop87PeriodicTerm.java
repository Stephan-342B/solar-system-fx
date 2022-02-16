package org.mahefa.data.meeus.jean;

import java.util.List;

public class Vsop87PeriodicTerm {

    private String label;
    private List<Term> l;
    private List<Term> b;
    private List<Term> r;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Term> getL() {
        return l;
    }

    public void setL(List<Term> l) {
        this.l = l;
    }

    public List<Term> getB() {
        return b;
    }

    public void setB(List<Term> b) {
        this.b = b;
    }

    public List<Term> getR() {
        return r;
    }

    public void setR(List<Term> r) {
        this.r = r;
    }
}
