package modelo;

public abstract class Ficha {

    protected String color; // blanco o negro

    public Ficha(String color) {
        this.color = color; // N: negras, B: blancas
    }

    public String getColor() {
        return color;
    }

    // Metodo que determina si puede capturar otra ficha
    public boolean puedeCapturar(Ficha fichaDestino) {
        // Comprueba que la ficha de destino no es nula y que es de un color diferente
        return fichaDestino != null && !this.color.equals(fichaDestino.getColor());
    }

    public abstract String gettoString();
    public abstract String getNombre();
    public abstract boolean validarMovimiento(Tablero tablero, String posicionActual, String movimientoPropuesto);
}
