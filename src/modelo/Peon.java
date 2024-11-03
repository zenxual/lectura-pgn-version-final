package modelo;

import java.util.ArrayList;
import java.util.List;

public class Peon extends Ficha {
    public Peon(String color) {
        super(color);
    }

    @Override
    public String gettoString(){
        return getColor();
    }

    @Override
    public String getNombre() {
        return "P";
    }

    public boolean validarMovimiento(Tablero tablero, String posicionActual, String movimientoPropuesto) {
        // Verifica que los parámetros no sean nulos o vacíos
        if (posicionActual == null || posicionActual.isEmpty() || movimientoPropuesto == null || movimientoPropuesto.isEmpty()) {
            System.out.println("Posición actual o movimiento propuesto son nulos o vacíos.");
            return false; // Movimiento no válido
        }

        // Verifica que las longitudes sean correctas
        if (posicionActual.length() != 2 || movimientoPropuesto.length() != 2) {
            System.out.println("Posiciones deben tener longitud 2.");
            return false; // Movimiento no válido
        }

        char columnaActual = posicionActual.charAt(0);
        int filaActual = Character.getNumericValue(posicionActual.charAt(1));
        char columnaDestino = movimientoPropuesto.charAt(0);
        int filaDestino = Character.getNumericValue(movimientoPropuesto.charAt(1));

        // Verifica que el movimiento propuesto está dentro de los límites del tablero
        if (filaDestino < 1 || filaDestino > 8 || columnaDestino < 'a' || columnaDestino > 'h') {
            System.out.println("Movimiento fuera del tablero: " + movimientoPropuesto);
            return false; // Movimiento fuera del tablero
        }

        // Verifica que el movimiento es hacia adelante
        int direccion = (this.color.equals("W")) ? 1 : -1; // Dirección del movimiento según el color
        if (columnaActual == columnaDestino) {
            // Movimiento hacia adelante
            if (filaDestino == filaActual + direccion) {
                // Verifica que la casilla está vacía
                if (tablero.getPiezaEn(movimientoPropuesto) == null) {
                    return true; // Movimiento válido
                }
            } else if (filaDestino == filaActual + 2 * direccion && filaActual == (this.color.equals("W") ? 2 : 7)) {
                // Movimiento inicial de dos casillas hacia adelante
                String intermedia = "" + columnaActual + (filaActual + direccion);
                if (tablero.getPiezaEn(intermedia) == null && tablero.getPiezaEn(movimientoPropuesto) == null) {
                    return true; // Movimiento válido
                }
            }
        } else if (Math.abs(columnaDestino - columnaActual) == 1 && filaDestino == filaActual + direccion) {
            // Captura diagonal
            Ficha piezaDestino = tablero.getPiezaEn(movimientoPropuesto);
            if (piezaDestino != null && !piezaDestino.getColor().equals(this.color)) {
                return true; // Movimiento válido para captura
            }
        }

        return false; // Movimiento no válido
    }


}