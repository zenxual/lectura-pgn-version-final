package vista;

import funcionalidades.CargarImagen;
import modelo.Ficha;
import modelo.Tablero;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class PanelJuego extends JPanel {
    private Tablero tablero;
    private CargarImagen cargarImagen;
    private JButton botonSiguiente;
    private JButton botonRetroceder;
    // private JButton auto;
    private JButton botonCargarPGN; // Botón para cargar archivo PGN
    private JLabel resultadoLabel; // Muestra el resultado de la partida
    private Image fondo;

    public PanelJuego(Tablero tablero, CargarImagen cargarImagen) {
        this.tablero = tablero;
        this.cargarImagen = cargarImagen;
        FondoPanel("resources/imagenes/fondo1.png");

        // Inicializar botones
        botonSiguiente = new JButton("Siguiente");
        botonRetroceder = new JButton("Retroceder");
        // auto = new JButton("Auto");
        botonCargarPGN = new JButton("Cargar PGN");

        // Configuración de colores para botones de "Siguiente" y "Retroceder"
        Color colorBotonTablero = new Color(121, 197, 112);
        botonSiguiente.setBackground(colorBotonTablero);
        botonRetroceder.setBackground(colorBotonTablero);
        botonCargarPGN.setBackground(colorBotonTablero);
        botonCargarPGN.setForeground(Color.WHITE);
        botonSiguiente.setForeground(Color.WHITE);
        botonRetroceder.setForeground(Color.WHITE);

        // Configurar JLabel para resultado
        resultadoLabel = new JLabel(" ");
        resultadoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Añadir botones y etiquetas al panel
        setLayout(new BorderLayout());
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonRetroceder);
        // panelBotones.add(auto);
        panelBotones.add(botonSiguiente);
        panelBotones.add(botonCargarPGN);

        add(panelBotones, BorderLayout.SOUTH);
        add(resultadoLabel, BorderLayout.NORTH);

        // Funcionalidad para cargar archivo PGN
        botonCargarPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivoPGN();
            }
        });
    }

    public void FondoPanel(String ruta) {
        try {
            URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                fondo = ImageIO.read(url);
                repaint();
            } else {
                System.out.println("No se encontró la ruta: " + ruta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reproducirSonido(String rutaArchivo) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(rutaArchivo);
            if (url == null) {
                System.out.println("No se encontró el archivo " + rutaArchivo);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.start();

            audioStream.close();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }

        int tamañoTablero = 400;
        int tamañoCelda = tamañoTablero / 8;
        int inicioX = (getWidth() - tamañoTablero) / 2;
        int inicioY = (getHeight() - tamañoTablero) / 2;

        for (char col = 'a'; col <= 'h'; col++) {
            for (int fila = 1; fila <= 8; fila++) {
                String posicion = "" + col + fila;
                Ficha ficha = tablero.getPiezaEn(posicion);

                if ((fila + (col - 'a')) % 2 == 0) {
                    g.setColor(new Color(254, 254, 255));
                } else {
                    g.setColor(new Color(121, 197, 112));
                }
                g.fillRect(inicioX + (col - 'a') * tamañoCelda, inicioY + (fila - 1) * tamañoCelda, tamañoCelda, tamañoCelda);

                if (ficha != null) {
                    String tipoFicha = ficha.getNombre();
                    String colorFicha = ficha.getColor();

                    String rutaImagen = "/resources/imagenes/" + colorFicha + "/" + tipoFicha + ".png";
                    Image imagen = cargarImagen.cargarImagen(rutaImagen);
                    if (imagen != null) {
                        int fichaX = inicioX + (col - 'a') * tamañoCelda + tamañoCelda / 8;
                        int fichaY = inicioY + (fila - 1) * tamañoCelda + tamañoCelda / 8;
                        int fichaTamaño = tamañoCelda * 3 / 4;

                        g.drawImage(imagen, fichaX, fichaY, fichaTamaño, fichaTamaño, this);
                    }
                }
            }
        }
    }

    private void cargarArchivoPGN() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoPGN = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivoPGN))) {
                String linea;
                StringBuilder contenido = new StringBuilder();

                while ((linea = br.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }

                System.out.println("Contenido del archivo PGN:\n" + contenido.toString());
                // Actualizar el tablero si fuera necesario
                // tablero.actualizarDesdePGN(contenido.toString());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo PGN.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public void actualizarTablero(Tablero tablero) {
        this.tablero = tablero;
        repaint();
    }

    public void reiniciarTablero(Tablero tablero) {
        this.tablero = tablero;
        repaint();
    }

    public void mostrarResultado(String resultado) {
        resultadoLabel.setText("Resultado: " + resultado);
    }

    public void addBotonSiguienteListener(ActionListener listener) {
        botonSiguiente.addActionListener(listener);
    }

    public void addBotonRetrocederListener(ActionListener listener) {
        botonRetroceder.addActionListener(listener);
    }

    // public void addBotonAutoListener(ActionListener listener) {
    //     auto.addActionListener(listener);
    // }

    public void addBotonCargarPGNListener(ActionListener listener) {
        botonCargarPGN.addActionListener(listener);
    }
}
