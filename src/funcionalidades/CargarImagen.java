package funcionalidades;

import javax.swing.*;
import java.awt.*;

public class CargarImagen {
    private String ruta;


    public Image cargarImagen(String ruta){
        Image imagen = null;
        try {
            imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen" + ruta);
            e.printStackTrace();
        }
        return imagen;
    }
}
