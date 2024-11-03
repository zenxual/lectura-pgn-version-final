package modelo;

import java.util.HashMap;
import java.util.Map;

public class Tablero {
    private Map<String, Ficha> tablero;

    public Tablero() {
        tablero = new HashMap<>();
        inicializarTablero();
    }

    // Inicializa automáticamente las piezas en sus posiciones iniciales
    private void inicializarTablero() {
        Map<String, String> posicionesIniciales = Map.ofEntries(
                Map.entry("a1", "T_W"), Map.entry("h1", "T_W"),
                Map.entry("a8", "T_B"), Map.entry("h8", "T_B"),
                Map.entry("b1", "C_W"), Map.entry("g1", "C_W"),
                Map.entry("b8", "C_B"), Map.entry("g8", "C_B"),
                Map.entry("c1", "A_W"), Map.entry("f1", "A_W"),
                Map.entry("c8", "A_B"), Map.entry("f8", "A_B"),
                Map.entry("d1", "D_W"), Map.entry("d8", "D_B"),
                Map.entry("e1", "R_W"), Map.entry("e8", "R_B")
        );

        // Inicialización de las piezas según el mapa
        for (Map.Entry<String, String> entry : posicionesIniciales.entrySet()) {
            String posicion = entry.getKey();
            String ficha = entry.getValue();
            tablero.put(posicion, crearPieza(ficha));
        }

        // Inicializar los peones por separado
        for (char col = 'a'; col <= 'h'; col++) {
            tablero.put(col + "2", new Peon("W"));
            tablero.put(col + "7", new Peon("B"));
        }
    }

    // Método auxiliar para crear una pieza según el tipo especificado
    private Ficha crearPieza(String tipo) {
        switch (tipo) {
            case "T_W": return new Torre("W");
            case "T_B": return new Torre("B");
            case "C_W": return new Caballo("W");
            case "C_B": return new Caballo("B");
            case "A_W": return new Alfil("W");
            case "A_B": return new Alfil("B");
            case "D_W": return new Dama("W");
            case "D_B": return new Dama("B");
            case "R_W": return new Rey("W");
            case "R_B": return new Rey("B");
            default: return null;
        }
    }

    // Obtener ficha en una posición
    public Ficha getPiezaEn(String posicion) {
        return tablero.get(posicion);
    }

    // Agregar ficha a una posición específica
    public void agregarFicha(String posicion, Ficha ficha) {
        tablero.put(posicion, ficha);
    }

    // Eliminar ficha de una posición específica
    public void eliminarFicha(String posicion) {
        tablero.remove(posicion);
    }

    // Método para verificar si una posición está vacía
    public boolean posicionVacia(String posicion) {
        return !tablero.containsKey(posicion);
    }

    // Obtener el mapa completo del tablero
    public Map<String, Ficha> getTablero() {
        return tablero;
    }

    // Método para mover ficha entre posiciones en el tablero
    public void moverFicha(String posicionActual, String nuevaPosicion) {
        Ficha ficha = tablero.get(posicionActual);
        if (ficha != null) {
            eliminarFicha(posicionActual);  // Elimina la ficha de la posición actual
            agregarFicha(nuevaPosicion, ficha);  // Agrega la ficha en la nueva posición
        }
    }

    public boolean casillaEnJaque(String posicion, boolean esBlanco) {
        // Obtener todas las piezas del oponente
        for (Map.Entry<String, Ficha> entry : tablero.entrySet()) {
            String posicionPieza = entry.getKey();
            Ficha ficha = entry.getValue();

            // Si la ficha no es nula y pertenece al color contrario
            if (ficha != null && ((esBlanco && ficha.getColor().equals("B")) || (!esBlanco && ficha.getColor().equals("W")))) {
                // Si esta ficha puede moverse a la casilla en cuestión, significa que está en jaque
                if (ficha.validarMovimiento(this, posicionPieza, posicion)) {
                    return true; // Casilla en jaque
                }
            }
        }
        return false; // Ninguna ficha enemiga puede atacar esta casilla
    }

}
