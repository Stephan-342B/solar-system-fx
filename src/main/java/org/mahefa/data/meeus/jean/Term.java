package org.mahefa.data.meeus.jean;

import java.util.List;

public class Term {

    private int length;
    private List<Double> a;
    private List<Double> b;
    private List<Double> c;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Double> getA() {
        return a;
    }

    public void setA(List<Double> a) {
        this.a = a;
    }

    public List<Double> getB() {
        return b;
    }

    public void setB(List<Double> b) {
        this.b = b;
    }

    public List<Double> getC() {
        return c;
    }

    public void setC(List<Double> c) {
        this.c = c;
    }
}
