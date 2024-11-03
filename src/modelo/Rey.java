package modelo;

import java.util.ArrayList;
import java.util.List;

public class Rey extends Ficha {
    public Rey(String color) {
        super(color);
    }

    @Override
    public String gettoString(){
        return getColor();
    }

    public String getNombre() {
        return "K";
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

        // Verifica que el movimiento es de una casilla en cualquier dirección
        if (Math.abs(columnaDestino - columnaActual) > 1 || Math.abs(filaDestino - filaActual) > 1) {
            return false; // Movimiento no permitido para un rey
        }

        // Verifica que la posición de destino está vacía o tiene una pieza enemiga
        Ficha piezaDestino = tablero.getPiezaEn(movimientoPropuesto);
        if (piezaDestino != null && piezaDestino.getColor().equals(this.color)) {
            return false; // Movimiento no válido si hay una pieza aliada en la posición de destino
        }

        // Aquí podrías agregar lógica adicional para verificar si el rey se movería a una casilla amenazada
        if (isCasillaAmenazada(tablero, movimientoPropuesto)) {
            return false; // Movimiento no válido si la casilla está amenazada
        }

        return true; // Movimiento válido
    }

    private boolean isCasillaAmenazada(Tablero tablero, String posicion) {
        // Implementa la lógica para verificar si la casilla está amenazada por cualquier pieza enemiga
        // Esto implicaría revisar todas las piezas enemigas y sus posibles movimientos

//        // Este es un método de ejemplo. Deberías implementar la lógica según tu estructura de datos
//        for (Ficha piezaEnemiga : tablero.getPiezasEnemigas(this.color)) {
//            // Supongamos que tienes un método para obtener los movimientos válidos de cada pieza
//            List<String> movimientosPosibles = piezaEnemiga.calcularMovimientos(tablero);
//            if (movimientosPosibles.contains(posicion)) {
//                return true; // La casilla está amenazada
//            }
//        }
        return false; // La casilla no está amenazada
    }

}
