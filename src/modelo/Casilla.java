package modelo;

public class Casilla {
    private Ficha ficha;
    private String id;

    public Casilla() {
        this.ficha = null; // Inicialmente, sin ficha
        this.id = null;
    }

    public boolean hasFicha() {
        return ficha != null;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
