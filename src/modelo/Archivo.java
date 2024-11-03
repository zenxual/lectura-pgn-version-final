package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Archivo {
    private List<String> listaMovimientos;
    String nombreArchivo;

    public Archivo() {
        this.listaMovimientos = new ArrayList<>();

    }

    // Método para leer el contenido del archivo PGN
    public String leerPgn(String nombreArchivo) {
        StringBuilder pgnContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                pgnContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pgnContent.toString();
    }

    // Método para extraer los movimientos del contenido PGN
    public String estraerMovimientos(String pgnContenido) {
        String[] lines = pgnContenido.split("\n");
        StringBuilder movimientos = new StringBuilder();

        for (String line : lines) {
            if (!line.startsWith("[") && !line.endsWith("]")) {
                movimientos.append(line).append(" ");
            }
        }
        System.out.println("Movimientos extraídos: " + movimientos.toString().trim()); // Imprimir movimientos
        return movimientos.toString().trim();
    }


    // Método para analizar y almacenar los movimientos en la lista
    public void analizarMovimientos(String movimientos) {
        movimientos = movimientos.replaceAll("\\b\\d+\\.", "");  // Eliminar numeraciones

        String[] fichas = movimientos.split("\\s+");
        this.listaMovimientos.clear();

        for (String ficha : fichas) {
            if (!ficha.matches("1-0|0-1|1/2-1/2")) {  // Filtrar resultados finales
                this.listaMovimientos.add(ficha);
            }
        }
    }

    // Método que procesa el archivo PGN y devuelve la lista de movimientos
    public List<String> obtenerMovimientosDesdeArchivo(String nombreArchivo) {
        String pgnContenido = leerPgn(nombreArchivo);             // Leer contenido del archivo
        String movimientos = estraerMovimientos(pgnContenido);    // Extraer movimientos
        analizarMovimientos(movimientos);                         // Analizar y almacenar movimientos
        return this.listaMovimientos;                             // Devolver la lista de movimientos
    }
}
