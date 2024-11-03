package modelo;

import java.util.ArrayList;
import java.util.List;

public class Dama extends Ficha {
    public Dama(String color) {
        super(color);
    }

    @Override
    public String gettoString() {
        return getColor();
    }

    public String getNombre() {
        return "Q";
    }

    public boolean validarMovimiento(Tablero tablero, String posicionActual, String movimientoPropuesto) {
        char columnaActual = posicionActual.charAt(0);
        int filaActual = Character.getNumericValue(posicionActual.charAt(1));
        char columnaDestino = movimientoPropuesto.charAt(0);
        int filaDestino = Character.getNumericValue(movimientoPropuesto.charAt(1));

        // Verifica que el movimiento propuesto está dentro de los límites del tablero
        if (filaDestino < 1 || filaDestino > 8 || columnaDestino < 'a' || columnaDestino > 'h') {
            return false; // Movimiento fuera del tablero
        }

        // Verifica que el movimiento es en línea recta o diagonal
        if (!(columnaActual == columnaDestino || filaActual == filaDestino ||
                Math.abs(columnaDestino - columnaActual) == Math.abs(filaDestino - filaActual))) {
            return false; // Movimiento no permitido para una dama
        }

        // Verifica que el camino esté despejado
        int incrementoFila = Integer.signum(filaDestino - filaActual);
        int incrementoColumna = Integer.signum(columnaDestino - columnaActual);
        int filaTemp = filaActual + incrementoFila;
        char columnaTemp = (char)(columnaActual + incrementoColumna);

        while (filaTemp != filaDestino || columnaTemp != columnaDestino) {
            String posicionIntermedia = "" + columnaTemp + filaTemp;
            if (tablero.getPiezaEn(posicionIntermedia) != null) {
                return false; // Hay una pieza en el camino
            }
            filaTemp += incrementoFila;
            columnaTemp += incrementoColumna;
        }

        // Verifica que la posición de destino está vacía o tiene una pieza enemiga
        Ficha piezaDestino = tablero.getPiezaEn(movimientoPropuesto);
        if (piezaDestino == null || !piezaDestino.getColor().equals(this.color)) {
            return true; // Movimiento válido
        }

        return false; // Hay una pieza aliada en la posición de destino
    }

}
