package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalFrame extends JFrame {

    public MenuPrincipalFrame() {
        setTitle("Mantenimiento - Tablas Referenciales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Sistema de Mantenimiento - Proyecto Control de Proyectos SW", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel de botones (GridLayout 3 columnas)
        JPanel panelBotones = new JPanel(new GridLayout(0, 3, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Lista de tablas (nombre visible y clase correspondiente)
        String[][] tablas = {
                { "GZZ_TIPO_CLIENTE", "MantenimientoTipoClienteFrame" },
                { "GZZ_ESTADO_REGISTRO", "MantenimientoEstadoRegistroFrame" },
                { "GZZ_ESTADO_CLIENTE", "MantenimientoEstadoClienteFrame" },
                { "GZZ_TIPO_PROYECTO", "MantenimientoTipoProyectoFrame" },
                { "GZZ_ESTADO_PROYECTO", "MantenimientoEstadoProyectoFrame" },
                { "GZZ_CARGO_PERSONAL", "MantenimientoCargoPersonalFrame" },
                { "GZZ_CARGO_PROYECTO", "MantenimientoCargoProyectoFrame" },
                { "GZZ_FACTOR", "MantenimientoFactorFrame" },
                { "GZZ_ESTADO_DISPONIBILIDAD", "MantenimientoEstadoDisponibilidadFrame" },
                { "GZZ_TIPO_ESTANDAR", "MantenimientoTipoEstandarFrame" }
        };

        for (String[] tabla : tablas) {
            String nombre = tabla[0];
            String clase = tabla[1];
            JButton btn = new JButton(nombre);
            btn.addActionListener(e -> abrirMantenimiento(clase));
            panelBotones.add(btn);
        }

        add(panelBotones, BorderLayout.CENTER);

        // Botón Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(Color.RED);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> System.exit(0));
        JPanel panelSalir = new JPanel();
        panelSalir.add(btnSalir);
        add(panelSalir, BorderLayout.SOUTH);
    }

    private void abrirMantenimiento(String nombreClase) {
        try {
            // Usamos reflexión para instanciar la clase dinámicamente
            Class<?> clase = Class.forName("ui." + nombreClase);
            JFrame frame = (JFrame) clase.getDeclaredConstructor().newInstance();
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el mantenimiento: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public static void main(String[] args) {
        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 16));
        SwingUtilities.invokeLater(() -> new MenuPrincipalFrame().setVisible(true));
    }
}