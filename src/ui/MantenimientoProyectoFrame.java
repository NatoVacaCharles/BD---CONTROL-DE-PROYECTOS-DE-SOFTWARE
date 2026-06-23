package ui;

import dao.ClienteDAO;
import dao.EstadoProyectoDAO;
import dao.ProyectoDAO;
import dao.TipoProyectoDAO;
import model.Cliente;
import model.EstadoProyecto;
import model.Proyecto;
import model.TipoProyecto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana de mantenimiento para P2M_PROYECTO.
 *
 * PK compuesta: ProCliCod | ProTipProCod | ProSecPro
 * FKs gestionadas con JComboBox:
 *   - ProCliCod  → P1M_CLIENTE (JComboBox de clientes activos)
 *   - ProTipProCod → GZZ_TIPO_PROYECTO
 *   - ProEstPro  → GZZ_ESTADO_PROYECTO
 * ProSecPro se calcula automáticamente en ADD.
 * Estado lógico ProEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoProyectoFrame extends MantenimientoBaseFrame<Proyecto, String> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // PK (solo lectura en formulario)
    private JTextField txtCliCod;
    private JTextField txtSecPro;

    // FKs → JComboBox
    private JComboBox<Cliente>       cmbCliente;
    private JComboBox<TipoProyecto>  cmbTipoProyecto;
    private JComboBox<EstadoProyecto> cmbEstadoProyecto;

    // Fechas obligatorias
    private JTextField txtFecCon;
    private JTextField txtFecPac;
    // Fechas opcionales
    private JTextField txtFecIni;
    private JTextField txtFecEnt;
    private JTextField txtFecCie;

    // Montos estimados
    private JTextField txtMonPro;       // NOT NULL
    private JTextField txtMonProCos;
    private JTextField txtMonProGas;
    private JTextField txtMonProUti;

    // Montos reales
    private JTextField txtMonProRea;
    private JTextField txtMonProCosRea;
    private JTextField txtMonProGasRea;
    private JTextField txtMonProUtiRea;

    // Estado lógico (solo lectura)
    private JTextField txtEstReg;

    @Override
    protected ProyectoDAO getDao() { return new ProyectoDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P2M_PROYECTO"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod", "TipPro", "Sec", "Fec.Con", "Fec.Pac",
                "Fec.Ini", "Fec.Ent", "Fec.Cie", "Mon.Pro", "Est.Pro", "Est.Reg"};
    }

    @Override
    protected Object[] modelToRow(Proyecto p) {
        String estRegLabel = switch (p.getProEstReg()) {
            case "A" -> "Activo";
            case "I" -> "Inactivo";
            case "*" -> "Eliminado";
            default  -> p.getProEstReg();
        };
        return new Object[]{
                p.getProCliCod(), p.getProTipProCod(), p.getProSecPro(),
                p.getProFecCon() != null ? p.getProFecCon().format(DATE_FMT) : "",
                p.getProFecPac() != null ? p.getProFecPac().format(DATE_FMT) : "",
                p.getProFecIni() != null ? p.getProFecIni().format(DATE_FMT) : "",
                p.getProFecEnt() != null ? p.getProFecEnt().format(DATE_FMT) : "",
                p.getProFecCie() != null ? p.getProFecCie().format(DATE_FMT) : "",
                p.getProMonPro(),
                p.getProEstPro(),
                estRegLabel
        };
    }

    @Override
    protected void cargarModeloEnCampos(Proyecto p) {
        txtCliCod.setText(String.valueOf(p.getProCliCod()));
        txtSecPro.setText(String.valueOf(p.getProSecPro()));

        seleccionarEnCombo(cmbCliente, p.getProCliCod());
        seleccionarEnComboTipPro(p.getProTipProCod());
        seleccionarEnComboEstPro(p.getProEstPro());

        txtFecCon.setText(p.getProFecCon() != null ? p.getProFecCon().format(DATE_FMT) : "");
        txtFecPac.setText(p.getProFecPac() != null ? p.getProFecPac().format(DATE_FMT) : "");
        txtFecIni.setText(p.getProFecIni() != null ? p.getProFecIni().format(DATE_FMT) : "");
        txtFecEnt.setText(p.getProFecEnt() != null ? p.getProFecEnt().format(DATE_FMT) : "");
        txtFecCie.setText(p.getProFecCie() != null ? p.getProFecCie().format(DATE_FMT) : "");
        txtMonPro.setText(p.getProMonPro() != null ? p.getProMonPro().toString() : "");
        txtMonProCos.setText(p.getProMonProCos() != null ? p.getProMonProCos().toString() : "");
        txtMonProGas.setText(p.getProMonProGas() != null ? p.getProMonProGas().toString() : "");
        txtMonProUti.setText(p.getProMonProUti() != null ? p.getProMonProUti().toString() : "");
        txtMonProRea.setText(p.getProMonProRea() != null ? p.getProMonProRea().toString() : "");
        txtMonProCosRea.setText(p.getProMonProCosRea() != null ? p.getProMonProCosRea().toString() : "");
        txtMonProGasRea.setText(p.getProMonProGasRea() != null ? p.getProMonProGasRea().toString() : "");
        txtMonProUtiRea.setText(p.getProMonProUtiRea() != null ? p.getProMonProUtiRea().toString() : "");
        txtEstReg.setText(p.getProEstReg());
    }

    @Override
    protected Proyecto obtenerModeloDeCampos() throws IllegalArgumentException {
        Cliente cliSel = (Cliente) cmbCliente.getSelectedItem();
        if (cliSel == null) throw new IllegalArgumentException("Seleccione un cliente.");
        TipoProyecto tipSel = (TipoProyecto) cmbTipoProyecto.getSelectedItem();
        if (tipSel == null) throw new IllegalArgumentException("Seleccione un tipo de proyecto.");
        EstadoProyecto estSel = (EstadoProyecto) cmbEstadoProyecto.getSelectedItem();
        if (estSel == null) throw new IllegalArgumentException("Seleccione un estado de proyecto.");

        // Secuencia: la calculamos al insertar; en MOD la tomamos del campo
        int sec = 0;
        String secStr = txtSecPro.getText().trim();
        if (!secStr.isEmpty()) { try { sec = Integer.parseInt(secStr); } catch (NumberFormatException e) {} }

        LocalDate fecCon = parseFecha(txtFecCon.getText(), "Fecha de Contrato");
        LocalDate fecPac = parseFecha(txtFecPac.getText(), "Fecha Pactada");
        LocalDate fecIni = txtFecIni.getText().trim().isEmpty() ? null : parseFecha(txtFecIni.getText(), "Fecha Inicio");
        LocalDate fecEnt = txtFecEnt.getText().trim().isEmpty() ? null : parseFecha(txtFecEnt.getText(), "Fecha Entrega");
        LocalDate fecCie = txtFecCie.getText().trim().isEmpty() ? null : parseFecha(txtFecCie.getText(), "Fecha Cierre");

        BigDecimal monPro = parseMonto(txtMonPro.getText(), "Monto Proyecto (obligatorio)");
        if (monPro == null) throw new IllegalArgumentException("El monto del proyecto es obligatorio.");

        Proyecto p = new Proyecto();
        p.setProCliCod(cliSel.getCodigo());
        p.setProTipProCod(tipSel.getCodigo());
        p.setProSecPro(sec);
        p.setProFecCon(fecCon);
        p.setProFecPac(fecPac);
        p.setProFecIni(fecIni);
        p.setProFecEnt(fecEnt);
        p.setProFecCie(fecCie);
        p.setProMonPro(monPro);
        p.setProMonProCos(parseMonto(txtMonProCos.getText(), null));
        p.setProMonProGas(parseMonto(txtMonProGas.getText(), null));
        p.setProMonProUti(parseMonto(txtMonProUti.getText(), null));
        p.setProMonProRea(parseMonto(txtMonProRea.getText(), null));
        p.setProMonProCosRea(parseMonto(txtMonProCosRea.getText(), null));
        p.setProMonProGasRea(parseMonto(txtMonProGasRea.getText(), null));
        p.setProMonProUtiRea(parseMonto(txtMonProUtiRea.getText(), null));
        p.setProEstPro(estSel.getCodigo());
        String estReg = txtEstReg.getText().trim();
        p.setProEstReg(estReg.isEmpty() ? "A" : estReg);
        return p;
    }

    @Override
    protected void limpiarCampos() {
        txtCliCod.setText(""); txtSecPro.setText("");
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        if (cmbTipoProyecto.getItemCount() > 0) cmbTipoProyecto.setSelectedIndex(0);
        if (cmbEstadoProyecto.getItemCount() > 0) cmbEstadoProyecto.setSelectedIndex(0);
        txtFecCon.setText(""); txtFecPac.setText(""); txtFecIni.setText("");
        txtFecEnt.setText(""); txtFecCie.setText("");
        txtMonPro.setText(""); txtMonProCos.setText(""); txtMonProGas.setText("");
        txtMonProUti.setText(""); txtMonProRea.setText("");
        txtMonProCosRea.setText(""); txtMonProGasRea.setText(""); txtMonProUtiRea.setText("");
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean editable = "ADD".equals(operacion) || "MOD".equals(operacion);
        boolean comboEnabled = editable;
        cmbCliente.setEnabled(comboEnabled);
        cmbTipoProyecto.setEnabled(comboEnabled);
        cmbEstadoProyecto.setEnabled(comboEnabled);
        txtFecCon.setEditable(editable); txtFecPac.setEditable(editable);
        txtFecIni.setEditable(editable); txtFecEnt.setEditable(editable);
        txtFecCie.setEditable(editable);
        txtMonPro.setEditable(editable); txtMonProCos.setEditable(editable);
        txtMonProGas.setEditable(editable); txtMonProUti.setEditable(editable);
        txtMonProRea.setEditable(editable); txtMonProCosRea.setEditable(editable);
        txtMonProGasRea.setEditable(editable); txtMonProUtiRea.setEditable(editable);
        txtCliCod.setEditable(false);
        txtSecPro.setEditable(false);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        txtCliCod.setEditable(false); txtSecPro.setEditable(false); // PK nunca editable
    }

    @Override
    protected void setEditableNombre(boolean editable) {
        txtFecCon.setEditable(editable); // representativo
    }

    @Override
    protected String getCodigoFromModel(Proyecto model) {
        return model.getProCliCod() + "|" + model.getProTipProCod() + "|" + model.getProSecPro();
    }

    @Override
    protected String getNombreFromModel(Proyecto model) {
        return model.getProFecCon() != null ? model.getProFecCon().format(DATE_FMT) : "";
    }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    // -------------------------------------------------------------------------
    // ejecutarActualizar — lógica personalizada para PK compuesta
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
            ProyectoDAO proDAO = (ProyectoDAO) dao;
            switch (operacionPendiente) {
                case "ADD": {
                    Proyecto p = obtenerModeloDeCampos();
                    // Calcular secuencia automáticamente
                    int sec = proDAO.calcularSiguienteSecuencia(p.getProCliCod(), p.getProTipProCod());
                    p.setProSecPro(sec);
                    proDAO.insertar(p);
                    JOptionPane.showMessageDialog(this, "Proyecto registrado con SecPro=" + sec);
                    break;
                }
                case "MOD": {
                    Proyecto p = obtenerModeloDeCampos();
                    proDAO.actualizar(p, registroSeleccionadoId);
                    JOptionPane.showMessageDialog(this, "Proyecto modificado correctamente.");
                    break;
                }
                case "DELETE":
                    proDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Proyecto eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    proDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Proyecto inactivado (I).");
                    break;
                case "REACTIVATE":
                    proDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Proyecto reactivado (A).");
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
    // cargarTabla — obtiene el ID compuesto de la columna 0|1|2
    // -------------------------------------------------------------------------
    @Override
    protected void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            for (Proyecto p : dao.listarTodos()) {
                tableModel.addRow(modelToRow(p));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar grilla: " + e.getMessage());
        }
    }

    // El listener de selección de tabla debe construir la clave compuesta
    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        int cliCod = (int) tableModel.getValueAt(row, 0);
        int tipCod = (int) tableModel.getValueAt(row, 1);
        int sec    = (int) tableModel.getValueAt(row, 2);
        registroSeleccionadoId = cliCod + "|" + tipCod + "|" + sec;
        try {
            Proyecto p = dao.obtenerPorId(registroSeleccionadoId);
            if (p != null) { cargarModeloEnCampos(p); registroSeleccionado = p; }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar registro: " + e.getMessage());
        }
    }

    @Override
    protected void iniciarModificar() {
        if (CarFlaAct == 1) {
            JOptionPane.showMessageDialog(this, "Ya hay una operación pendiente. Use ACTUALIZAR o CANCELAR.");
            return;
        }
        if (tblRegistros.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un registro de la tabla.");
            return;
        }
        cargarRegistroSeleccionadoEnCampos();
        protegerCamposSegunOperacion("MOD");
        bloquearBotonesOperacion();
        CarFlaAct = 1;
        operacionPendiente = "MOD";
    }

    private void bloquearBotonesOperacion() {
        btnAdicionar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
    }

    // -------------------------------------------------------------------------
    // Panel de campos
    // -------------------------------------------------------------------------
    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 6, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Proyecto"));

        // PK (solo lectura)
        panel.add(new JLabel("Cód. Cliente (auto):")); txtCliCod = roField(); panel.add(txtCliCod);
        panel.add(new JLabel("Sec. Proyecto (auto):")); txtSecPro = roField(); panel.add(txtSecPro);

        // FKs → ComboBox
        panel.add(new JLabel("Cliente:")); cmbCliente = new JComboBox<>(); cargarComboCliente(); panel.add(cmbCliente);
        panel.add(new JLabel("Tipo Proyecto:")); cmbTipoProyecto = new JComboBox<>(); cargarComboTipoPro(); panel.add(cmbTipoProyecto);

        panel.add(new JLabel("Estado Proyecto:")); cmbEstadoProyecto = new JComboBox<>(); cargarComboEstPro(); panel.add(cmbEstadoProyecto);
        panel.add(new JLabel("Estado Reg. (A/I/*):")); txtEstReg = roField(); panel.add(txtEstReg);

        // Fechas obligatorias
        panel.add(new JLabel("Fec. Contrato (yyyy-MM-dd):")); txtFecCon = new JTextField(); panel.add(txtFecCon);
        panel.add(new JLabel("Fec. Pactada (yyyy-MM-dd):")); txtFecPac = new JTextField(); panel.add(txtFecPac);

        // Fechas opcionales
        panel.add(new JLabel("Fec. Inicio (opc.):")); txtFecIni = new JTextField(); panel.add(txtFecIni);
        panel.add(new JLabel("Fec. Entrega (opc.):")); txtFecEnt = new JTextField(); panel.add(txtFecEnt);
        panel.add(new JLabel("Fec. Cierre (opc.):")); txtFecCie = new JTextField(); panel.add(txtFecCie);
        panel.add(new JLabel("")); panel.add(new JLabel(""));  // spacer

        // Montos estimados
        panel.add(new JLabel("Monto Proyecto (*):")); txtMonPro = new JTextField(); panel.add(txtMonPro);
        panel.add(new JLabel("Mon. Costo Est.:")); txtMonProCos = new JTextField(); panel.add(txtMonProCos);
        panel.add(new JLabel("Mon. Gasto Est.:")); txtMonProGas = new JTextField(); panel.add(txtMonProGas);
        panel.add(new JLabel("Mon. Utilidad Est.:")); txtMonProUti = new JTextField(); panel.add(txtMonProUti);

        // Montos reales
        panel.add(new JLabel("Mon. Real:")); txtMonProRea = new JTextField(); panel.add(txtMonProRea);
        panel.add(new JLabel("Mon. Costo Real:")); txtMonProCosRea = new JTextField(); panel.add(txtMonProCosRea);
        panel.add(new JLabel("Mon. Gasto Real:")); txtMonProGasRea = new JTextField(); panel.add(txtMonProGasRea);
        panel.add(new JLabel("Mon. Utilidad Real:")); txtMonProUtiRea = new JTextField(); panel.add(txtMonProUtiRea);

        return panel;
    }

    private JTextField roField() {
        JTextField f = new JTextField(); f.setEditable(false); f.setBackground(Color.LIGHT_GRAY); return f;
    }

    // --- Loaders de ComboBox ---
    private void cargarComboCliente() {
        cmbCliente.removeAllItems();
        try { new ClienteDAO().listarActivos().forEach(cmbCliente::addItem); }
        catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Clientes: " + ex.getMessage()); }
    }

    private void cargarComboTipoPro() {
        cmbTipoProyecto.removeAllItems();
        try {
            new TipoProyectoDAO().listarTodos().stream()
                .filter(t -> "A".equals(t.getEstado())).forEach(cmbTipoProyecto::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Tipos de Proyecto: " + ex.getMessage()); }
    }

    private void cargarComboEstPro() {
        cmbEstadoProyecto.removeAllItems();
        try {
            new EstadoProyectoDAO().listarTodos().stream()
                .filter(e -> "A".equals(e.getEstado())).forEach(cmbEstadoProyecto::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Estados Proyecto: " + ex.getMessage()); }
    }

    // --- Helpers de selección en ComboBox ---
    private void seleccionarEnCombo(JComboBox<Cliente> cmb, int cod) {
        for (int i = 0; i < cmb.getItemCount(); i++)
            if (cmb.getItemAt(i).getCodigo() == cod) { cmb.setSelectedIndex(i); return; }
    }
    private void seleccionarEnComboTipPro(int cod) {
        for (int i = 0; i < cmbTipoProyecto.getItemCount(); i++)
            if (cmbTipoProyecto.getItemAt(i).getCodigo() == cod) { cmbTipoProyecto.setSelectedIndex(i); return; }
    }
    private void seleccionarEnComboEstPro(int cod) {
        for (int i = 0; i < cmbEstadoProyecto.getItemCount(); i++)
            if (cmbEstadoProyecto.getItemAt(i).getCodigo() == cod) { cmbEstadoProyecto.setSelectedIndex(i); return; }
    }

    // --- Parsing helpers ---
    private LocalDate parseFecha(String s, String campo) throws IllegalArgumentException {
        if (s == null || s.trim().isEmpty()) {
            if (campo != null) throw new IllegalArgumentException(campo + " es obligatorio (yyyy-MM-dd).");
            return null;
        }
        try { return LocalDate.parse(s.trim(), DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException((campo != null ? campo : "Fecha") + " inválida. Use yyyy-MM-dd."); }
    }

    private BigDecimal parseMonto(String s, String campo) throws IllegalArgumentException {
        if (s == null || s.trim().isEmpty()) return null;
        try { return new BigDecimal(s.trim()); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException((campo != null ? campo : "Monto") + " debe ser numérico.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoProyectoFrame().setVisible(true));
    }
}
