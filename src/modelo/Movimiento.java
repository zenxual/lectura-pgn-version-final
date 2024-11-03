package modelo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movimiento {
    private String tipoFicha;
    private String destino;
    private boolean captura;
    private boolean enroqueCorto;
    private boolean enroqueLargo;
    private boolean jaque;
    private boolean jaqueMate;
    private Character promocion;
    private List<String> movimientos;
    private Archivo archivo;
    private String resultado; // Nuevo campo para el resultado del juego

    // Constructor que inicializa los movimientos a partir de un archivo PGN
    public Movimiento(String nombreArchivo) {
        this.archivo = new Archivo();
        this.movimientos = archivo.obtenerMovimientosDesdeArchivo(nombreArchivo);
    }

    // Método que procesa un solo movimiento en notación PGN
    private void procesarMovimiento(String mov) {
        // Reiniciar valores predeterminados
        tipoFicha = "P";
        destino = "";
        captura = false;
        enroqueCorto = false;
        enroqueLargo = false;
        jaque = false;
        jaqueMate = false;
        promocion = null;

        // Verificar si el movimiento es un resultado
        if (mov.equals("1-0") || mov.equals("0-1") || mov.equals("1/2-1/2")) {
            resultado = mov; // Asignamos el resultado si es un movimiento de final de partida
            return;
        }

        // Verificar si es enroque y actualizar los campos
        if (mov.equals("O-O")) {
            enroqueCorto = true;
            tipoFicha = "K"; // Rey realiza el enroque
            destino = "O-O";
            return;
        } else if (mov.equals("O-O-O")) {
            enroqueLargo = true;
            tipoFicha = "K";
            destino = "O-O-O";
            return;
        }

        // Expresión regular para analizar otros movimientos
        Pattern patron = Pattern.compile("([KQRBN])?([a-h1-8]?)x?([a-h][1-8])(?:=([QRBN]))?([+#]?)");
        Matcher matcher = patron.matcher(mov);

        if (matcher.find()) {
            tipoFicha = matcher.group(1) != null ? matcher.group(1) : "P";
            captura = mov.contains("x");
            destino = matcher.group(3);

            if (matcher.group(4) != null) {
                promocion = matcher.group(4).charAt(0);
            }

            if (matcher.group(5) != null) {
                jaque = matcher.group(5).equals("+");
                jaqueMate = matcher.group(5).equals("#");
            }
        } else {
            destino = mov;
        }
    }

    // Metodo que devuelve el movimiento o resultado
    public String getMovimiento(int numeroMovimiento) {
        if (numeroMovimiento >= 0 && numeroMovimiento < movimientos.size()) {
            String movimientoActual = movimientos.get(numeroMovimiento);
            procesarMovimiento(movimientoActual);

            // Si hay un resultado (1-0, 0-1, o 1/2-1/2), lo devolvemos directamente
            if (resultado != null) {
                return resultado; // Devuelve directamente el resultado
            }

            return formatoMovimiento(); // Devuelve el formato del movimiento
        }
        return null; // Movimiento no válido
    }

    // Formateo para mostrar el movimiento en texto
    private String formatoMovimiento() {
        if (enroqueCorto) {
            return "O-O";
        }
        if (enroqueLargo) {
            return "O-O-O";
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append(tipoFicha);

        if (captura) {
            resultado.append("x");
        }

        resultado.append(destino);

        if (promocion != null) {
            resultado.append("=").append(promocion);
        }

        if (jaque) {
            resultado.append("+");
        } else if (jaqueMate) {
            resultado.append("#");
        }

        return resultado.toString();
    }

    public String getTipoFicha() {
        return tipoFicha;
    }

    public String getDestino() {
        return destino;
    }
}
