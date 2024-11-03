package controlador;

import funcionalidades.CargarImagen;
import modelo.Tablero;
import vista.PanelJuego;

import javax.swing.*;

public class ControladorPrincipal {
    private JFrame marco;

    public ControladorPrincipal(JFrame marco) {
        this.marco = marco;
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        Tablero tablero = new Tablero();
        CargarImagen cargarImagen = new CargarImagen();
        PanelJuego panelJuego = new PanelJuego(tablero, cargarImagen);
//        String nombreArchivo = "C:\\Users\\cerpa\\Documents\\P.pgn";
//        Movimiento movimiento = new Movimiento(nombreArchivo);
        ControladorJuego controladorJuego = new ControladorJuego(tablero, panelJuego);
        marco.setContentPane(panelJuego);
    }
}
