package ui;

import dao.BaseDAO;
import dao.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public abstract class MantenimientoBaseFrame<T, ID> extends JFrame {

    // Componentes comunes
    protected JTable tblRegistros;
    protected DefaultTableModel tableModel;
    protected JButton btnAdicionar, btnModificar, btnEliminar, btnInactivar, btnReactivar;
    protected JButton btnActualizar, btnCancelar, btnSalir;

    // Flag y control de operación
    protected int CarFlaAct = 0;
    protected String operacionPendiente = "";
    protected ID registroSeleccionadoId = null;
    protected T registroSeleccionado = null;

    protected BaseDAO<T, ID> dao;

    // Métodos abstractos
    protected abstract BaseDAO<T, ID> getDao();

    protected abstract String getTituloVentana();

    protected abstract String[] getNombresColumnas();

    protected abstract Object[] modelToRow(T model);

    protected abstract void cargarModeloEnCampos(T model);

    protected abstract T obtenerModeloDeCampos() throws IllegalArgumentException;

    protected abstract void limpiarCampos();

    protected abstract void protegerCamposSegunOperacion(String operacion);

    protected abstract void setEditableCodigo(boolean editable);

    protected abstract void setEditableNombre(boolean editable);

    protected abstract ID getCodigoFromModel(T model);

    protected abstract String getNombreFromModel(T model);

    protected abstract void setEstadoEnCampos(String estado);

    protected abstract String getEstadoFromCampos();

    protected abstract JPanel crearPanelCampos();

    public MantenimientoBaseFrame() {
        this.dao = getDao();
        initComponentsBase();
        verificarConexionInicial();
        cargarTabla();
        aplicarEstadoInicial();
        configurarEventos();
    }

    private void initComponentsBase() {
        setTitle(getTituloVentana());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelCampos = crearPanelCampos();
        add(panelCampos, BorderLayout.NORTH);

        // Grilla no editable
        tableModel = new DefaultTableModel(getNombresColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición directa
            }
        };
        tblRegistros = new JTable(tableModel);
        tblRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblRegistros);
        add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnInactivar = new JButton("Inactivar");
        btnReactivar = new JButton("Reactivar");
        btnActualizar = new JButton("ACTUALIZAR");
        btnCancelar = new JButton("Cancelar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void verificarConexionInicial() {
        try (java.sql.Connection conn = DatabaseConnection.getConnection()) {
            JOptionPane.showMessageDialog(this,
                    "Conexión a la base de datos establecida exitosamente.",
                    "Conexión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión: " + e.getMessage(),
                    "Conexión Fallida",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    protected void aplicarEstadoInicial() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        limpiarCampos();
        protegerCamposSegunOperacion(""); // Deshabilitar todos los campos
        CarFlaAct = 0;
        operacionPendiente = "";
        registroSeleccionadoId = null;
        registroSeleccionado = null;
    }

    private void configurarEventos() {
        btnAdicionar.addActionListener(e -> iniciarAdicionar());
        btnModificar.addActionListener(e -> iniciarModificar());
        btnEliminar.addActionListener(e -> iniciarEliminar());
        btnInactivar.addActionListener(e -> iniciarInactivar());
        btnReactivar.addActionListener(e -> iniciarReactivar());
        btnActualizar.addActionListener(e -> ejecutarActualizar());
        btnCancelar.addActionListener(e -> cancelarOperacion());
        btnSalir.addActionListener(e -> dispose());

        tblRegistros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && CarFlaAct == 0) {
                int row = tblRegistros.getSelectedRow();
                if (row != -1) {
                    registroSeleccionadoId = (ID) tableModel.getValueAt(row, 0);
                    // No cargar automáticamente los campos, solo guardar la selección
                }
            }
        });
    }

    // --------------------------------------------------------------
    // Inicio de operaciones (todas dejan CarFlaAct = 1)
    // --------------------------------------------------------------
    protected void iniciarAdicionar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        limpiarCampos();
        setEditableCodigo(true);
        setEditableNombre(true);
        setEstadoEnCampos("A");
        protegerCamposSegunOperacion("ADD");
        bloquearOtrosBotones();
        CarFlaAct = 1;
        operacionPendiente = "ADD";
    }

    protected void iniciarModificar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        if (registroSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un registro de la tabla.");
            return;
        }
        cargarRegistroSeleccionadoEnCampos();
        setEditableCodigo(false);
        setEditableNombre(true);
        protegerCamposSegunOperacion("MOD");
        bloquearOtrosBotones();
        CarFlaAct = 1;
        operacionPendiente = "MOD";
    }

    protected void iniciarEliminar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        if (registroSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un registro de la tabla.");
            return;
        }
        cargarRegistroSeleccionadoEnCampos();
        setEditableCodigo(false);
        setEditableNombre(false);
        setEstadoEnCampos("*");
        protegerCamposSegunOperacion("DELETE");
        bloquearOtrosBotones();
        CarFlaAct = 1;
        operacionPendiente = "DELETE";
    }

    protected void iniciarInactivar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        if (registroSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un registro de la tabla.");
            return;
        }
        cargarRegistroSeleccionadoEnCampos();
        setEditableCodigo(false);
        setEditableNombre(false);
        setEstadoEnCampos("I");
        protegerCamposSegunOperacion("INACTIVATE");
        bloquearOtrosBotones();
        CarFlaAct = 1;
        operacionPendiente = "INACTIVATE";
    }

    protected void iniciarReactivar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        if (registroSeleccionadoId == null) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un registro de la tabla.");
            return;
        }
        cargarRegistroSeleccionadoEnCampos();
        setEditableCodigo(false);
        setEditableNombre(false);
        setEstadoEnCampos("A");
        protegerCamposSegunOperacion("REACTIVATE");
        bloquearOtrosBotones();
        CarFlaAct = 1;
        operacionPendiente = "REACTIVATE";
    }

    protected void cargarRegistroSeleccionadoEnCampos() {
        try {
            T model = dao.obtenerPorId(registroSeleccionadoId);
            if (model != null) {
                cargarModeloEnCampos(model);
                registroSeleccionado = model;
            } else {
                JOptionPane.showMessageDialog(this, "Registro no encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar registro: " + e.getMessage());
        }
    }

    private void bloquearOtrosBotones() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
    }

    protected void habilitarTodosBotones() {
        btnAdicionar.setEnabled(true);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnInactivar.setEnabled(true);
        btnReactivar.setEnabled(true);
    }

    // --------------------------------------------------------------
    // ACTUALIZAR (único que escribe en BD)
    // --------------------------------------------------------------
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado un comando para actualizar un registro de la BD",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            switch (operacionPendiente) {
                case "ADD":
                    T nuevoModelo = obtenerModeloDeCampos();
                    dao.insertar(nuevoModelo);
                    JOptionPane.showMessageDialog(this, "Registro añadido correctamente.");
                    break;

                case "MOD":
                    T modeloMod = obtenerModeloDeCampos();
                    dao.actualizarNombre(getCodigoFromModel(modeloMod), getNombreFromModel(modeloMod));
                    JOptionPane.showMessageDialog(this, "Registro modificado correctamente.");
                    break;

                case "DELETE":
                    dao.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado lógicamente (*).");
                    break;

                case "INACTIVATE":
                    dao.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;

                case "REACTIVATE":
                    dao.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Registro reactivado (A).");
                    break;
            }
            cargarTabla();
            resetearDespuesDeOperacion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void cancelarOperacion() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay ninguna operación pendiente para cancelar.");
            return;
        }
        resetearDespuesDeOperacion();
        JOptionPane.showMessageDialog(this, "Operación cancelada. Se borraron los datos del área de registro.");
    }

    protected void resetearDespuesDeOperacion() {
        limpiarCampos();
        protegerCamposSegunOperacion(""); // Deshabilitar todos los campos
        habilitarTodosBotones();
        CarFlaAct = 0;
        operacionPendiente = "";
        registroSeleccionadoId = null;
        registroSeleccionado = null;
        tblRegistros.clearSelection();
    }

    protected void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            List<T> lista = dao.listarTodos();
            for (T model : lista) {
                tableModel.addRow(modelToRow(model));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la grilla: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}