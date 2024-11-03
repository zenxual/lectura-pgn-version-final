package modelo;

public class Caballo extends Ficha {
    public Caballo(String color) {
        super(color);
    }

    @Override
    public String gettoString() {
        return getColor();
    }

    public String getNombre() {
        return "N";
    }

    public boolean validarMovimiento(Tablero tablero, String posicionActual, String movimientoPropuesto) {
        char columnaActual = posicionActual.charAt(0);
        int filaActual = Character.getNumericValue(posicionActual.charAt(1)); // 1-8
        char columnaDestino = movimientoPropuesto.charAt(0);
        int filaDestino = Character.getNumericValue(movimientoPropuesto.charAt(1)); // 1-8

        // Verifica que el movimiento propuesto está dentro de los límites del tablero
        if (filaDestino < 1 || filaDestino > 8 || columnaDestino < 'a' || columnaDestino > 'h') {
            return false; // Movimiento fuera del tablero
        }

        // Verifica que el movimiento es en forma de "L"
        int deltaColumna = Math.abs(columnaDestino - columnaActual);
        int deltaFila = Math.abs(filaDestino - filaActual);

        if (!((deltaColumna == 2 && deltaFila == 1) || (deltaColumna == 1 && deltaFila == 2))) {
            return false; // Movimiento no permitido para un caballo
        }

        // Verifica que la posición de destino está vacía o tiene una pieza enemiga
        Ficha piezaDestino = tablero.getPiezaEn(movimientoPropuesto);
        if (piezaDestino != null && piezaDestino.getColor().equals(this.getColor())) {
            return false; // Movimiento no válido si hay una pieza aliada en la posición de destino
        }

        return true; // Movimiento válido
    }

}
