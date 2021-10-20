package org.mahefa.data.view;

public class DataView {

    private Data sun;
    private Data mercury;
    private Data venus;
    private Data mars;
    private Data jupiter;
    private Data saturn;
    private Data uranus;
    private Data neptune;

    public DataView() {
    }

    public Data getSun() {
        return sun;
    }

    public void setSun(Data sun) {
        this.sun = sun;
    }

    public Data getMercury() {
        return mercury;
    }

    public void setMercury(Data mercury) {
        this.mercury = mercury;
    }

    public Data getVenus() {
        return venus;
    }

    public void setVenus(Data venus) {
        this.venus = venus;
    }

    public Data getMars() {
        return mars;
    }

    public void setMars(Data mars) {
        this.mars = mars;
    }

    public Data getJupiter() {
        return jupiter;
    }

    public void setJupiter(Data jupiter) {
        this.jupiter = jupiter;
    }

    public Data getSaturn() {
        return saturn;
    }

    public void setSaturn(Data saturn) {
        this.saturn = saturn;
    }

    public Data getUranus() {
        return uranus;
    }

    public void setUranus(Data uranus) {
        this.uranus = uranus;
    }

    public Data getNeptune() {
        return neptune;
    }

    public void setNeptune(Data neptune) {
        this.neptune = neptune;
    }

    public class Data {
        private String ra;
        private String decl;

        public Data(String ra, String decl) {
            this.ra = ra;
            this.decl = decl;
        }

        public String getRa() {
            return ra;
        }

        public void setRa(String ra) {
            this.ra = ra;
        }

        public String getDecl() {
            return decl;
        }

        public void setDecl(String decl) {
            this.decl = decl;
        }
    }
}
