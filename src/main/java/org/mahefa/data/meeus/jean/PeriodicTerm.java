package org.mahefa.data.meeus.jean;

import java.util.List;

public class PeriodicTerm {

    private String designation;
    private List<Term> l;
    private List<Term> b;
    private List<Term> r;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
