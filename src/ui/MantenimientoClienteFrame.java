package ui;

import dao.ClienteDAO;
import dao.EstadoClienteDAO;
import dao.TipoClienteDAO;
import model.Cliente;
import model.EstadoCliente;
import model.TipoCliente;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana de mantenimiento para P1M_CLIENTE.
 *
 * Reglas implementadas:
 *  - CliTipCod y CliEstCli son FKs → se muestran con JComboBox alimentado dinámicamente.
 *  - CliCod es AUTO_INCREMENT → se muestra solo como lectura (generado por BD).
 *  - Gestión lógica de estados A / I / * (sin DELETE físico).
 *  - Botón Cancelar: aborta la operación sin tocar la BD, limpia campos, restaura botones.
 */
public class MantenimientoClienteFrame extends MantenimientoBaseFrame<Cliente, Integer> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Campos del formulario
    private JTextField txtCodigo;
    private JComboBox<TipoCliente> cmbTipoCliente;
    private JTextField txtNombre;
    private JTextField txtFechaIngreso;
    private JTextField txtFechaCese;
    private JTextField txtFechaUltProCer;
    private JComboBox<EstadoCliente> cmbEstadoCliente;
    private JTextField txtEstReg;  // Estado lógico (A/I/*), solo lectura

    @Override
    protected ClienteDAO getDao() {
        return new ClienteDAO();
    }

    @Override
    protected String getTituloVentana() {
        return "Mantenimiento - P1M_CLIENTE";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Cód.", "Tipo Cliente", "Nombre", "Fec. Ingreso", "Fec. Cese",
                "Fec. Últ. Pro. Cer.", "Est. Cliente", "Est. Reg."};
    }

    @Override
    protected Object[] modelToRow(Cliente c) {
        String estRegLabel = switch (c.getEstReg()) {
            case "A" -> "Activo";
            case "I" -> "Inactivo";
            case "*" -> "Eliminado";
            default -> c.getEstReg();
        };
        return new Object[]{
                c.getCodigo(),
                c.getTipClienteCod(),
                c.getNombre(),
                c.getFechaIngreso() != null ? c.getFechaIngreso().format(DATE_FMT) : "",
                c.getFechaCese() != null ? c.getFechaCese().format(DATE_FMT) : "",
                c.getFechaUltProCer() != null ? c.getFechaUltProCer().format(DATE_FMT) : "",
                c.getEstCli(),
                estRegLabel
        };
    }

    @Override
    protected void cargarModeloEnCampos(Cliente c) {
        txtCodigo.setText(String.valueOf(c.getCodigo()));
        // Seleccionar en ComboBox por código
        for (int i = 0; i < cmbTipoCliente.getItemCount(); i++) {
            if (cmbTipoCliente.getItemAt(i).getCodigo() == c.getTipClienteCod()) {
                cmbTipoCliente.setSelectedIndex(i);
                break;
            }
        }
        txtNombre.setText(c.getNombre());
        txtFechaIngreso.setText(c.getFechaIngreso() != null ? c.getFechaIngreso().format(DATE_FMT) : "");
        txtFechaCese.setText(c.getFechaCese() != null ? c.getFechaCese().format(DATE_FMT) : "");
        txtFechaUltProCer.setText(c.getFechaUltProCer() != null ? c.getFechaUltProCer().format(DATE_FMT) : "");
        // Seleccionar EstadoCliente en ComboBox
        for (int i = 0; i < cmbEstadoCliente.getItemCount(); i++) {
            if (cmbEstadoCliente.getItemAt(i).getCodigo().equals(c.getEstCli())) {
                cmbEstadoCliente.setSelectedIndex(i);
                break;
            }
        }
        txtEstReg.setText(c.getEstReg());
    }

    @Override
    protected Cliente obtenerModeloDeCampos() throws IllegalArgumentException {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");

        String fecIngStr = txtFechaIngreso.getText().trim();
        if (fecIngStr.isEmpty()) throw new IllegalArgumentException("La fecha de ingreso es obligatoria (yyyy-MM-dd).");
        LocalDate fecIng;
        try { fecIng = LocalDate.parse(fecIngStr, DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha de ingreso inválida. Use yyyy-MM-dd."); }

        LocalDate fecCes = null;
        String fecCesStr = txtFechaCese.getText().trim();
        if (!fecCesStr.isEmpty()) {
            try { fecCes = LocalDate.parse(fecCesStr, DATE_FMT); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha de cese inválida. Use yyyy-MM-dd."); }
        }

        LocalDate fecUlt = null;
        String fecUltStr = txtFechaUltProCer.getText().trim();
        if (!fecUltStr.isEmpty()) {
            try { fecUlt = LocalDate.parse(fecUltStr, DATE_FMT); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha últ. proyecto cerrado inválida. Use yyyy-MM-dd."); }
        }

        TipoCliente tipSel = (TipoCliente) cmbTipoCliente.getSelectedItem();
        if (tipSel == null) throw new IllegalArgumentException("Debe seleccionar un tipo de cliente.");

        EstadoCliente estCliSel = (EstadoCliente) cmbEstadoCliente.getSelectedItem();
        if (estCliSel == null) throw new IllegalArgumentException("Debe seleccionar un estado de cliente.");

        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";

        // CliCod = 0 para INSERT (AUTO_INCREMENT); se obtiene de txtCodigo si existe
        int cod = 0;
        String codStr = txtCodigo.getText().trim();
        if (!codStr.isEmpty()) {
            try { cod = Integer.parseInt(codStr); } catch (NumberFormatException ignored) {}
        }

        return new Cliente(cod, tipSel.getCodigo(), nombre, fecIng, fecCes, fecUlt,
                estCliSel.getCodigo(), estReg);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        if (cmbTipoCliente.getItemCount() > 0) cmbTipoCliente.setSelectedIndex(0);
        txtNombre.setText("");
        txtFechaIngreso.setText("");
        txtFechaCese.setText("");
        txtFechaUltProCer.setText("");
        if (cmbEstadoCliente.getItemCount() > 0) cmbEstadoCliente.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        switch (operacion) {
            case "ADD":
                // Código auto → siempre no editable
                txtCodigo.setEditable(false);
                cmbTipoCliente.setEnabled(true);
                txtNombre.setEditable(true);
                txtFechaIngreso.setEditable(true);
                txtFechaCese.setEditable(true);
                txtFechaUltProCer.setEditable(true);
                cmbEstadoCliente.setEnabled(true);
                break;
            case "MOD":
                txtCodigo.setEditable(false);
                cmbTipoCliente.setEnabled(true);
                txtNombre.setEditable(true);
                txtFechaIngreso.setEditable(true);
                txtFechaCese.setEditable(true);
                txtFechaUltProCer.setEditable(true);
                cmbEstadoCliente.setEnabled(true);
                break;
            case "DELETE":
            case "INACTIVATE":
            case "REACTIVATE":
                txtCodigo.setEditable(false);
                cmbTipoCliente.setEnabled(false);
                txtNombre.setEditable(false);
                txtFechaIngreso.setEditable(false);
                txtFechaCese.setEditable(false);
                txtFechaUltProCer.setEditable(false);
                cmbEstadoCliente.setEnabled(false);
                break;
            default: // Estado inicial: todo bloqueado
                txtCodigo.setEditable(false);
                cmbTipoCliente.setEnabled(false);
                txtNombre.setEditable(false);
                txtFechaIngreso.setEditable(false);
                txtFechaCese.setEditable(false);
                txtFechaUltProCer.setEditable(false);
                cmbEstadoCliente.setEnabled(false);
                break;
        }
        txtEstReg.setEditable(false); // Estado lógico siempre bloqueado al usuario
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        txtCodigo.setEditable(false); // AUTO_INCREMENT: nunca editable
    }

    @Override
    protected void setEditableNombre(boolean editable) {
        txtNombre.setEditable(editable);
    }

    @Override
    protected Integer getCodigoFromModel(Cliente model) {
        return model.getCodigo();
    }

    @Override
    protected String getNombreFromModel(Cliente model) {
        return model.getNombre();
    }

    @Override
    protected void setEstadoEnCampos(String estado) {
        txtEstReg.setText(estado);
    }

    @Override
    protected String getEstadoFromCampos() {
        return txtEstReg.getText();
    }

    // -------------------------------------------------------------------------
    // Sobrescribir ejecutarActualizar para manejar INSERT y UPDATE personalizados
    // -------------------------------------------------------------------------
    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado un comando para actualizar un registro de la BD",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ClienteDAO cliDAO = (ClienteDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    Cliente nuevo = obtenerModeloDeCampos();
                    cliDAO.insertar(nuevo);
                    JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
                    break;
                case "MOD":
                    Cliente mod = obtenerModeloDeCampos();
                    cliDAO.actualizar(mod);
                    JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");
                    break;
                case "DELETE":
                    cliDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Cliente eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    cliDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Cliente inactivado (I).");
                    break;
                case "REACTIVATE":
                    cliDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Cliente reactivado (A).");
                    break;
            }
            cargarTabla();
            resetearDespuesDeOperacion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    // -------------------------------------------------------------------------
    // Panel de campos con ComboBoxes dinámicos para FKs
    // -------------------------------------------------------------------------
    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        // --- Código (solo lectura, AUTO_INCREMENT) ---
        panel.add(new JLabel("Código (auto):"));
        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(Color.LIGHT_GRAY);
        panel.add(txtCodigo);

        // --- Tipo Cliente (FK → GZZ_TIPO_CLIENTE) ---
        panel.add(new JLabel("Tipo Cliente:"));
        cmbTipoCliente = new JComboBox<>();
        cargarComboTipoCliente();
        panel.add(cmbTipoCliente);

        // --- Nombre ---
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        // --- Fecha Ingreso ---
        panel.add(new JLabel("Fecha Ingreso (yyyy-MM-dd):"));
        txtFechaIngreso = new JTextField();
        panel.add(txtFechaIngreso);

        // --- Fecha Cese ---
        panel.add(new JLabel("Fecha Cese (yyyy-MM-dd, opcional):"));
        txtFechaCese = new JTextField();
        panel.add(txtFechaCese);

        // --- Fecha Último Proyecto Cerrado ---
        panel.add(new JLabel("Últ. Proy. Cerrado (yyyy-MM-dd, opc.):"));
        txtFechaUltProCer = new JTextField();
        panel.add(txtFechaUltProCer);

        // --- Estado Cliente (FK → GZZ_ESTADO_CLIENTE) ---
        panel.add(new JLabel("Estado Cliente:"));
        cmbEstadoCliente = new JComboBox<>();
        cargarComboEstadoCliente();
        panel.add(cmbEstadoCliente);

        // --- Estado Registro (A/I/*) — solo lectura ---
        panel.add(new JLabel("Estado Registro (A/I/*):"));
        txtEstReg = new JTextField();
        txtEstReg.setEditable(false);
        txtEstReg.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstReg);

        return panel;
    }

    /** Carga el JComboBox de Tipo Cliente desde GZZ_TIPO_CLIENTE (activos). */
    private void cargarComboTipoCliente() {
        cmbTipoCliente.removeAllItems();
        try {
            TipoClienteDAO tipDAO = new TipoClienteDAO();
            List<TipoCliente> lista = tipDAO.listarTodos();
            for (TipoCliente t : lista) {
                if ("A".equals(t.getEstado())) cmbTipoCliente.addItem(t);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Tipos de Cliente: " + ex.getMessage());
        }
    }

    /** Carga el JComboBox de Estado Cliente desde GZZ_ESTADO_CLIENTE (activos). */
    private void cargarComboEstadoCliente() {
        cmbEstadoCliente.removeAllItems();
        try {
            EstadoClienteDAO estDAO = new EstadoClienteDAO();
            List<EstadoCliente> lista = estDAO.listarTodos();
            for (EstadoCliente e : lista) {
                if ("A".equals(e.getEstado())) cmbEstadoCliente.addItem(e);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Estados de Cliente: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoClienteFrame().setVisible(true));
    }
}
