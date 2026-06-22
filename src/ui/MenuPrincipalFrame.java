package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Menú Principal — Sistema de Control de Proyectos de Software.
 * Organizado en 3 pestañas (JTabbedPane) con diseño corporativo moderno.
 */
public class MenuPrincipalFrame extends JFrame {

    // ── Paleta de colores ────────────────────────────────────────────────────
    private static final Color COLOR_FONDO = new Color(0xF0F2F5);
    private static final Color COLOR_HEADER_BG = new Color(0x1A2438);
    private static final Color COLOR_HEADER_FG = Color.WHITE;
    private static final Color COLOR_TAB_BG = new Color(0xF0F2F5);
    private static final Color COLOR_BTN_NORMAL = new Color(0x0D6EFD);
    private static final Color COLOR_BTN_HOVER = new Color(0x0A58CA);
    private static final Color COLOR_BTN_PRESSED = new Color(0x0747A6);
    private static final Color COLOR_BTN_RRHH = new Color(0x198754);
    private static final Color COLOR_BTN_RRHH_HOVER = new Color(0x146C43);
    private static final Color COLOR_BTN_OPS = new Color(0x6F42C1);
    private static final Color COLOR_BTN_OPS_HOVER = new Color(0x59359A);
    private static final Color COLOR_BTN_EXIT = new Color(0xDC3545);
    private static final Color COLOR_BTN_EXIT_HOVER = new Color(0xB02A37);
    private static final Color COLOR_BTN_FG = Color.WHITE;

    // ── Fuentes ──────────────────────────────────────────────────────────────
    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_SUBTIT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TAB = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_BTN_EXIT = new Font("Segoe UI", Font.BOLD, 14);

    // ── Constructor ──────────────────────────────────────────────────────────
    public MenuPrincipalFrame() {
        setTitle("Control de Proyectos de Software — Sistema de Mantenimiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));

        // ── Header ──────────────────────────────────────────────────────────
        add(crearHeader(), BorderLayout.NORTH);

        // ── JTabbedPane central ─────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(FONT_TAB);
        tabs.setBackground(COLOR_TAB_BG);
        tabs.setBorder(new EmptyBorder(8, 16, 8, 16));

        // Pestaña 1 – Configuración y Catálogos
        tabs.addTab("Configuración y Catálogos",
                crearPanelPestana(tablasCatalogos(), COLOR_BTN_NORMAL, COLOR_BTN_HOVER));

        // Pestaña 2 – RRHH y Clientes
        tabs.addTab("Gestión de RRHH y Clientes",
                crearPanelPestana(tablasRRHH(), COLOR_BTN_RRHH, COLOR_BTN_RRHH_HOVER));

        // Pestaña 3 – Proyectos y Operaciones
        tabs.addTab("Proyectos y Operaciones",
                crearPanelPestana(tablasProyectos(), COLOR_BTN_OPS, COLOR_BTN_OPS_HOVER));

        add(tabs, BorderLayout.CENTER);

        // ── Footer con botón Salir ──────────────────────────────────────────
        add(crearFooter(), BorderLayout.SOUTH);
    }

    // ── Definición de grupos de tablas ───────────────────────────────────────

    /** Pestaña 1 — Tablas referenciales GZZ + P3M_ESTANDAR */
    private String[][] tablasCatalogos() {
        return new String[][] {
                { "GZZ_TIPO_CLIENTE", "MantenimientoTipoClienteFrame" },
                { "GZZ_ESTADO_CLIENTE", "MantenimientoEstadoClienteFrame" },
                { "GZZ_TIPO_PROYECTO", "MantenimientoTipoProyectoFrame" },
                { "GZZ_ESTADO_PROYECTO", "MantenimientoEstadoProyectoFrame" },
                { "GZZ_CARGO_PERSONAL", "MantenimientoCargoPersonalFrame" },
                { "GZZ_CARGO_PROYECTO", "MantenimientoCargoProyectoFrame" },
                { "GZZ_FACTOR", "MantenimientoFactorFrame" },
                { "GZZ_ESTADO_DISPONIBILIDAD", "MantenimientoEstadoDisponibilidadFrame" },
                { "GZZ_TIPO_ESTANDAR", "MantenimientoTipoEstandarFrame" },
                { "GZZ_ESTADO_REGISTRO", "MantenimientoEstadoRegistroFrame" },
                { "P3M_ESTANDAR", "MantenimientoEstandarFrame" },
        };
    }

    /** Pestaña 2 — Personal y Clientes */
    private String[][] tablasRRHH() {
        return new String[][] {
                { "P1M_CLIENTE", "MantenimientoClienteFrame" },
                { "P3M_PERSONAL", "MantenimientoPersonalFrame" },
                { "P3T_PERSONAL_CARGO_PRY", "MantenimientoPersonalCargoPryFrame" },
                { "P3T_PERSONAL_DISPONIB.", "MantenimientoPersonalDisponibilidadFrame" },
        };
    }

    /** Pestaña 3 — Proyectos, Etapas, Actividades y Transaccionales */
    private String[][] tablasProyectos() {
        return new String[][] {
                { "P2M_PROYECTO", "MantenimientoProyectoFrame" },
                { "P3M_ETAPA", "MantenimientoEtapaFrame" },
                { "P3M_ACTIVIDAD", "MantenimientoActividadFrame" },
                { "P3T_EQUIPO_PROYECTO", "MantenimientoEquipoProyectoFrame" },
                { "P3T_PROYECTO_ETAPA", "MantenimientoProyectoEtapaFrame" },
                { "P2T_PROYECTO_FACTOR", "MantenimientoProyectoFactorFrame" },
                { "P4T_MOVIMIENTO", "MantenimientoMovimientoFrame" },
                { "P2H_PROYECTO_ESTADO", "MantenimientoProyectoEstadoHistoriaFrame" },
        };
    }

    // ── Construcción de componentes ──────────────────────────────────────────

    /** Panel de cabecera con gradiente y datos del sistema. */
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x1A2438),
                        getWidth(), 0, new Color(0x0D6EFD));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(18, 30, 18, 30));

        JLabel lblTitulo = new JLabel("Control de Proyectos de Software");
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setForeground(COLOR_HEADER_FG);

        JLabel lblSub = new JLabel("Sistema de Mantenimiento — Seleccione una categoría para comenzar");
        lblSub.setFont(FONT_SUBTIT);
        lblSub.setForeground(new Color(0xADB5BD));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblSub);

        header.add(textos, BorderLayout.WEST);

        // Badge de versión
        JLabel lblVer = new JLabel("v1.0.0", SwingConstants.RIGHT);
        lblVer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblVer.setForeground(new Color(0xADB5BD));
        header.add(lblVer, BorderLayout.EAST);

        return header;
    }

    /**
     * Crea el panel de botones de una pestaña.
     * 
     * @param tablas pares {nombre visible, nombre de clase}
     * @param base   color base de los botones
     * @param hover  color hover de los botones
     */
    private JPanel crearPanelPestana(String[][] tablas, Color base, Color hover) {
        JPanel panel = new JPanel(new GridLayout(0, 3, 14, 14));
        panel.setBackground(COLOR_TAB_BG);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        for (String[] tabla : tablas) {
            String nombre = tabla[0];
            String clase = tabla[1];
            JButton btn = crearBoton(nombre, base, hover);
            btn.addActionListener(e -> abrirMantenimiento(clase));
            panel.add(btn);
        }

        // Envuelve en un ScrollPane por si crecen las tablas en el futuro
        JScrollPane scroll = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COLOR_TAB_BG);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_TAB_BG);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    /** Crea un botón corporativo con efectos hover/press y cursor de mano. */
    private JButton crearBoton(String texto, Color colorBase, Color colorHover) {
        JButton btn = new JButton("<html><center>" + texto + "</center></html>");
        btn.setFont(FONT_BTN);
        btn.setForeground(COLOR_BTN_FG);
        btn.setBackground(colorBase);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 54));

        // Efecto hover / press
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(colorBase);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(COLOR_BTN_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(colorHover);
            }
        });

        return btn;
    }

    /** Footer con botón Salir estilizado. */
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(new Color(0xE9ECEF));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xCED4DA)));

        JLabel lblInfo = new JLabel(
                "© 2025 Control de Proyectos SW  |  Todos los derechos reservados | Cristhian Bravo-Jose Morocco-Renato Ponce");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(0x6C757D));
        footer.add(lblInfo, 0);
        footer.setLayout(new BorderLayout());
        footer.add(lblInfo, BorderLayout.WEST);

        JButton btnSalir = new JButton("Salir del Sistema");
        btnSalir.setFont(FONT_BTN_EXIT);
        btnSalir.setForeground(COLOR_BTN_FG);
        btnSalir.setBackground(COLOR_BTN_EXIT);
        btnSalir.setOpaque(true);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(200, 38));
        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSalir.setBackground(COLOR_BTN_EXIT_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(COLOR_BTN_EXIT);
            }
        });
        btnSalir.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea salir del sistema?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION)
                System.exit(0);
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 6));
        rightPanel.setOpaque(false);
        rightPanel.add(btnSalir);
        footer.add(rightPanel, BorderLayout.EAST);

        return footer;
    }

    // ── Lógica de apertura de ventanas (reflexión) ───────────────────────────
    private void abrirMantenimiento(String nombreClase) {
        try {
            Class<?> clase = Class.forName("ui." + nombreClase);
            JFrame frame = (JFrame) clase.getDeclaredConstructor().newInstance();
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el módulo: " + nombreClase + "\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ── Utilidad: fuente global ──────────────────────────────────────────────
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    // ── Main ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Look & Feel nativo del SO
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 13));

            // Colores de pestaña
            UIManager.put("TabbedPane.selected", new Color(0x0D6EFD));
            UIManager.put("TabbedPane.background", COLOR_TAB_BG);
            UIManager.put("TabbedPane.foreground", new Color(0x1A2438));
            UIManager.put("TabbedPane.selectedForeground", Color.WHITE);

            new MenuPrincipalFrame().setVisible(true);
        });
    }
}