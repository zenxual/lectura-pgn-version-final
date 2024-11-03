import javax.swing.*;

import controlador.ControladorPrincipal;

public class Main {
    public static void main(String[] args) {
        // Crear el marco
        JFrame marco = new JFrame();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(600, 800);
        marco.setLocationRelativeTo(null);
        // Esto hace que el panel sea el fondo del frame

        // Crear y configurar el controlador principal
        ControladorPrincipal controladorPrincipal = new ControladorPrincipal(marco);


        // Hacer visible el marco
        marco.setVisible(true);
    }
}