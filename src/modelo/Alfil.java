package modelo;

import java.util.ArrayList;
import java.util.List;

public class Alfil extends Ficha {
    public Alfil(String color) {
        super(color );
    }

    @Override
    public String gettoString() {
        return getColor();
    }

    public String getNombre() {
        return "B";
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

        // Verifica que el movimiento es diagonal
        int deltaColumna = Math.abs(columnaDestino - columnaActual);
        int deltaFila = Math.abs(filaDestino - filaActual);

        if (deltaColumna != deltaFila) {
            return false; // Movimiento no permitido para un alfil
        }

        // Verifica que el camino esté despejado
        int incrementoFila = (filaDestino > filaActual) ? 1 : -1;
        int incrementoColumna = (columnaDestino > columnaActual) ? 1 : -1;

        int filaTemp = filaActual + incrementoFila;
        char columnaTemp = (char)(columnaActual + incrementoColumna);

        while (filaTemp != filaDestino && columnaTemp != columnaDestino) {
            String posicionIntermedia = "" + columnaTemp + filaTemp;
            if (tablero.getPiezaEn(posicionIntermedia) != null) {
                return false; // Hay una pieza en el camino
            }
            filaTemp += incrementoFila;
            columnaTemp += incrementoColumna;
        }

        // Verifica que la posición de destino está vacía o tiene una pieza enemiga
        Ficha piezaDestino = tablero.getPiezaEn(movimientoPropuesto);
        if (piezaDestino != null && piezaDestino.getColor().equals(this.color)) {
            return false; // Movimiento no válido si hay una pieza aliada en la posición de destino
        }

        return true; // Movimiento válido
    }
}
