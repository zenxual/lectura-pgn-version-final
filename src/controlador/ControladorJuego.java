package controlador;

import modelo.Ficha;
import modelo.Movimiento;
import modelo.Tablero;
import vista.PanelJuego;

import javax.swing.*;
import java.io.File;
import java.util.Map;
import java.util.Stack;

public class ControladorJuego {
    private Tablero tablero;
    private PanelJuego panelJuego;
    private Movimiento movimiento;
    private boolean esTurnoBlancas; // Controla si es el turno de las blancas
    private int numMovimiento;
    private Stack<MovimientoRealizado> historialMovimientos; // Pila para almacenar movimientos

    public ControladorJuego(Tablero tablero, PanelJuego panelJuego) {
        this.tablero = tablero;
        this.panelJuego = panelJuego;
        this.movimiento = null;
        this.esTurnoBlancas = true; // Comienza con el turno de las blancas
        this.numMovimiento = 0;
        this.historialMovimientos = new Stack<>(); // Inicializamos la pila de movimientos

        initListeners();
    }

    private void initListeners() {
        // Listener para avanzar al siguiente movimiento
        this.panelJuego.addBotonSiguienteListener(e -> avanzarMovimiento());

        // Listener para retroceder al movimiento anterior
        this.panelJuego.addBotonRetrocederListener(e -> retrocederMovimiento());

        // Listener para cargar el archivo PGN
        this.panelJuego.addBotonCargarPGNListener(e -> cargarArchivoPGN());
    }

    private void avanzarMovimiento() {
        if (movimiento == null) {
            System.out.println("No hay archivo PGN cargado.");
            return;
        }

        String movimientoP = movimiento.getMovimiento(numMovimiento);
        if (movimientoP != null) {
            String tipoFicha = movimiento.getTipoFicha();
            String destino = movimiento.getDestino();
            validarElMovimiento(tipoFicha, destino);
            panelJuego.reproducirSonido("resources/sonido/fichaAjedrez.wav");
            numMovimiento++;
        } else {
            System.out.println("No hay más movimientos.");
        }
    }

    private void retrocederMovimiento() {
        if (!historialMovimientos.isEmpty()) {
            MovimientoRealizado ultimoMovimiento = historialMovimientos.pop();
            deshacerMovimiento(ultimoMovimiento);
            panelJuego.reproducirSonido("resources/sonido/fichaAjedrez.wav");
        } else {
            System.out.println("No hay movimientos previos para deshacer.");
        }
    }

    private void cargarArchivoPGN() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(panelJuego);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoPGN = fileChooser.getSelectedFile();
            String ruta = archivoPGN.getAbsolutePath();
            movimiento = new Movimiento(ruta);
            JOptionPane.showMessageDialog(panelJuego, "Archivo PGN cargado exitosamente.");
            reiniciarTablero();
        }
    }

    private void reiniciarTablero() {
        tablero = new Tablero();
        panelJuego.reiniciarTablero(tablero);
        numMovimiento = 0;
    }

    private void deshacerMovimiento(MovimientoRealizado movimientoRealizado) {
        Ficha fichaMovida = movimientoRealizado.getFichaMovida();
        String posicionInicial = movimientoRealizado.getPosicionInicial();
        String posicionFinal = movimientoRealizado.getPosicionFinal();

        tablero.eliminarFicha(posicionFinal); // Eliminamos la ficha de la posición final
        tablero.agregarFicha(posicionInicial, fichaMovida); // La regresamos a su posición inicial

        if (movimientoRealizado.getFichaCapturada() != null) {
            tablero.agregarFicha(posicionFinal, movimientoRealizado.getFichaCapturada());
        }

        panelJuego.actualizarTablero(tablero);
        cambiarTurno();
        numMovimiento = Math.max(0, numMovimiento - 1);
    }

    private void validarElMovimiento(String fichaAMover, String movimientoPropuesto) {
        if (movimientoPropuesto == null || movimientoPropuesto.isEmpty()) {
            System.out.println("Movimiento propuesto es nulo o vacío.");
            return;
        }

        String tipoFicha = movimiento.getTipoFicha();
        System.out.println("Validando movimiento para la ficha: " + tipoFicha + " hacia " + movimientoPropuesto);

        if (movimientoPropuesto.equals("O-O")) {
            realizarEnroque(true);
            return;
        } else if (movimientoPropuesto.equals("O-O-O")) {
            realizarEnroque(false);
            return;
        }

        // Validación y ejecución del movimiento estándar
        boolean movimientoValido = false;
        Ficha fichaSeleccionada = null;
        String posicionActual = null;

        for (Map.Entry<String, Ficha> entry : tablero.getTablero().entrySet()) {
            String posicion = entry.getKey();
            Ficha ficha = entry.getValue();

            if (ficha != null && ficha.getNombre().equals(fichaAMover) &&
                    ((esTurnoBlancas && ficha.getColor().equals("W")) || (!esTurnoBlancas && ficha.getColor().equals("B")))) {

                if (ficha.validarMovimiento(tablero, posicion, movimientoPropuesto)) {
                    movimientoValido = true;
                    fichaSeleccionada = ficha;
                    posicionActual = posicion;
                    break;
                }
            }
        }

        if (movimientoValido && fichaSeleccionada != null) {
            realizarMovimiento(fichaSeleccionada, posicionActual, movimientoPropuesto, tablero.getPiezaEn(movimientoPropuesto));
            panelJuego.actualizarTablero(tablero);
            cambiarTurno();
        } else {
            System.out.println("Movimiento no válido para la ficha: " + fichaAMover + " hacia " + movimientoPropuesto);
        }
    }

    private void realizarMovimiento(Ficha ficha, String posicionActual, String movimientoPropuesto, Ficha fichaCapturada) {
        historialMovimientos.push(new MovimientoRealizado(ficha, posicionActual, movimientoPropuesto, fichaCapturada));
        tablero.eliminarFicha(posicionActual);
        tablero.agregarFicha(movimientoPropuesto, ficha);
    }

    private void realizarEnroque(boolean esCorto) {
        if (validarEnroque(esCorto)) {
            String posicionRey = esTurnoBlancas ? (esCorto ? "e1" : "e8") : (esCorto ? "e8" : "a8");
            String posicionTorre = esTurnoBlancas ? (esCorto ? "h1" : "a1") : (esCorto ? "h8" : "a8");
            String nuevaPosicionRey = esTurnoBlancas ? (esCorto ? "g1" : "c1") : (esCorto ? "g8" : "c8");
            String nuevaPosicionTorre = esTurnoBlancas ? (esCorto ? "f1" : "d1") : (esCorto ? "f8" : "d8");

            Ficha rey = tablero.getPiezaEn(posicionRey);
            Ficha torre = tablero.getPiezaEn(posicionTorre);

            tablero.eliminarFicha(posicionRey);
            tablero.eliminarFicha(posicionTorre);
            tablero.agregarFicha(nuevaPosicionRey, rey);
            tablero.agregarFicha(nuevaPosicionTorre, torre);

            System.out.println((esCorto ? "Enroque corto" : "Enroque largo") + " realizado.");
            panelJuego.actualizarTablero(tablero);
            cambiarTurno();
        } else {
            System.out.println((esCorto ? "Enroque corto" : "Enroque largo") + " no permitido.");
        }
    }

    private boolean validarEnroque(boolean esCorto) {
        String[] posicionesIntermedias = esTurnoBlancas ? (esCorto ? new String[]{"f1", "g1"} : new String[]{"d1", "c1"}) : (esCorto ? new String[]{"f8", "g8"} : new String[]{"d8", "c8"});

        for (String pos : posicionesIntermedias) {
            if (tablero.casillaEnJaque(pos, esTurnoBlancas)) {
                return false;
            }
        }
        return true;
    }

    private void cambiarTurno() {
        esTurnoBlancas = !esTurnoBlancas;
    }

    // Clase para almacenar la información de cada movimiento realizado
    private static class MovimientoRealizado {
        private final Ficha fichaMovida;
        private final String posicionInicial;
        private final String posicionFinal;
        private final Ficha fichaCapturada;

        public MovimientoRealizado(Ficha fichaMovida, String posicionInicial, String posicionFinal, Ficha fichaCapturada) {
            this.fichaMovida = fichaMovida;
            this.posicionInicial = posicionInicial;
            this.posicionFinal = posicionFinal;
            this.fichaCapturada = fichaCapturada;
        }

        public Ficha getFichaMovida() { return fichaMovida; }
        public String getPosicionInicial() { return posicionInicial; }
        public String getPosicionFinal() { return posicionFinal; }
        public Ficha getFichaCapturada() { return fichaCapturada; }
    }
}
