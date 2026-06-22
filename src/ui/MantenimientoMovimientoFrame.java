package ui;

import dao.EquipoProyectoDAO;
import dao.MovimientoDAO;
import dao.PersonalDAO;
import dao.ProyectoDAO;
import dao.ProyectoEtapaDAO;
import model.EquipoProyecto;
import model.Movimiento;
import model.Personal;
import model.Proyecto;
import model.ProyectoEtapa;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Mantenimiento para P4T_MOVIMIENTO.
 * Registra horas trabajadas por un miembro del equipo en una etapa del proyecto.
 *
 * PK 7 partes: MovCliCod|MovTipProCod|MovSecPro|MovPerCod|MovCarCod|MovEtaCod|MovEtaSec
 * MovEtaSec se calcula automáticamente en ADD.
 *
 * FKs via JComboBox:
 *   - Proyecto (3 partes) → P2M_PROYECTO           → cmbProyecto
 *   - Equipo (perCod+carCod) → P3T_EQUIPO_PROYECTO → cmbEquipo  (filtrado por proyecto)
 *   - Etapa (etaCod) → P3T_PROYECTO_ETAPA           → cmbEtapa   (filtrada por proyecto)
 *
 * Campos adicionales: MovFecRegEta (DATE), MovEtaHrsTra (0-12), MovEtaMinTra (0|15|30|45)
 * Estado lógico MovEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoMovimientoFrame extends MantenimientoBaseFrame<Movimiento, String> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ComboBoxes encadenados
    private JComboBox<Proyecto>      cmbProyecto;  // filtra los dos siguientes
    private JComboBox<EquipoProyecto> cmbEquipo;   // FK perCod+carCod
    private JComboBox<ProyectoEtapa>  cmbEtapa;    // FK etaCod

    // PK auto-calculada (solo lectura)
    private JTextField txtEtaSec;

    // Campos de datos
    private JTextField txtFecRegEta;
    private JSpinner   spnHoras;      // 0-12
    private JComboBox<Integer> cmbMinutos; // 0, 15, 30, 45

    // Estado lógico
    private JTextField txtEstReg;

    // Cache de personal para renderer del combo equipo
    private List<Personal> listaPersonal;

    @Override
    protected MovimientoDAO getDao() { return new MovimientoDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P4T_MOVIMIENTO"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod","TipPro","SecPro","PerCod","CarCod","EtaCod","EtaSec",
                "Fec.Reg","Horas","Minutos","Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(Movimiento m) {
        String est = switch (m.getMovEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> m.getMovEstReg();
        };
        return new Object[]{
            m.getMovCliCod(), m.getMovTipProCod(), m.getMovSecPro(),
            m.getMovPerCod(), m.getMovCarCod(), m.getMovEtaCod(), m.getMovEtaSec(),
            m.getMovFecRegEta() != null ? m.getMovFecRegEta().format(DATE_FMT) : "",
            m.getMovEtaHrsTra(), m.getMovEtaMinTra(), est
        };
    }

    @Override
    protected void cargarModeloEnCampos(Movimiento m) {
        // Seleccionar proyecto (recarga equipo y etapas)
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            Proyecto p = cmbProyecto.getItemAt(i);
            if (p.getProCliCod() == m.getMovCliCod() && p.getProTipProCod() == m.getMovTipProCod()
                    && p.getProSecPro() == m.getMovSecPro()) {
                cmbProyecto.setSelectedIndex(i); break;
            }
        }
        actualizarCombosEncadenados(m.getMovCliCod(), m.getMovTipProCod(), m.getMovSecPro());
        // Seleccionar equipo
        for (int i = 0; i < cmbEquipo.getItemCount(); i++) {
            EquipoProyecto eq = cmbEquipo.getItemAt(i);
            if (eq.getEqpPerCod() == m.getMovPerCod() && eq.getEqpCarCod() == m.getMovCarCod()) {
                cmbEquipo.setSelectedIndex(i); break;
            }
        }
        // Seleccionar etapa
        for (int i = 0; i < cmbEtapa.getItemCount(); i++) {
            if (cmbEtapa.getItemAt(i).getProEtaCod() == m.getMovEtaCod()) {
                cmbEtapa.setSelectedIndex(i); break;
            }
        }
        txtEtaSec.setText(String.valueOf(m.getMovEtaSec()));
        txtFecRegEta.setText(m.getMovFecRegEta() != null ? m.getMovFecRegEta().format(DATE_FMT) : "");
        spnHoras.setValue(m.getMovEtaHrsTra());
        cmbMinutos.setSelectedItem(m.getMovEtaMinTra());
        txtEstReg.setText(m.getMovEstReg());
    }

    @Override
    protected Movimiento obtenerModeloDeCampos() throws IllegalArgumentException {
        Proyecto proSel = (Proyecto) cmbProyecto.getSelectedItem();
        if (proSel == null) throw new IllegalArgumentException("Seleccione un proyecto.");
        EquipoProyecto eqSel = (EquipoProyecto) cmbEquipo.getSelectedItem();
        if (eqSel == null) throw new IllegalArgumentException("Seleccione un miembro del equipo.");
        ProyectoEtapa etaSel = (ProyectoEtapa) cmbEtapa.getSelectedItem();
        if (etaSel == null) throw new IllegalArgumentException("Seleccione una etapa del proyecto.");

        String fecStr = txtFecRegEta.getText().trim();
        if (fecStr.isEmpty()) throw new IllegalArgumentException("La Fecha de Registro es obligatoria.");
        LocalDate fecReg;
        try { fecReg = LocalDate.parse(fecStr, DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha inválida. Use yyyy-MM-dd."); }

        int horas = (Integer) spnHoras.getValue();
        Integer minutos = (Integer) cmbMinutos.getSelectedItem();
        if (minutos == null) throw new IllegalArgumentException("Seleccione los minutos.");
        if (horas == 0 && minutos == 0) throw new IllegalArgumentException("Las horas y minutos no pueden ser ambos 0.");

        String etaSec = txtEtaSec.getText().trim();
        int sec = 0;
        if (!etaSec.isEmpty()) { try { sec = Integer.parseInt(etaSec); } catch (NumberFormatException ignored) {} }

        String estReg = txtEstReg.getText().trim(); if (estReg.isEmpty()) estReg = "A";

        Movimiento m = new Movimiento();
        m.setMovCliCod(proSel.getProCliCod());    m.setMovTipProCod(proSel.getProTipProCod());
        m.setMovSecPro(proSel.getProSecPro());     m.setMovPerCod(eqSel.getEqpPerCod());
        m.setMovCarCod(eqSel.getEqpCarCod());      m.setMovEtaCod(etaSel.getProEtaCod());
        m.setMovEtaSec(sec);
        m.setMovFecRegEta(fecReg);
        m.setMovEtaHrsTra(horas); m.setMovEtaMinTra(minutos);
        m.setMovEstReg(estReg);
        return m;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        cmbEquipo.removeAllItems(); cmbEtapa.removeAllItems();
        txtEtaSec.setText(""); txtFecRegEta.setText("");
        spnHoras.setValue(0); cmbMinutos.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd  = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbProyecto.setEnabled(esAdd); cmbEquipo.setEnabled(esAdd); cmbEtapa.setEnabled(esAdd);
        txtFecRegEta.setEditable(esEdit);
        spnHoras.setEnabled(esEdit); cmbMinutos.setEnabled(esEdit);
        txtEtaSec.setEditable(false); txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean e) {
        cmbProyecto.setEnabled(e); cmbEquipo.setEnabled(e); cmbEtapa.setEnabled(e);
    }

    @Override protected void setEditableNombre(boolean e) { txtFecRegEta.setEditable(e); }

    @Override
    protected String getCodigoFromModel(Movimiento model) {
        return model.getMovCliCod() + "|" + model.getMovTipProCod() + "|" + model.getMovSecPro()
             + "|" + model.getMovPerCod() + "|" + model.getMovCarCod()
             + "|" + model.getMovEtaCod() + "|" + model.getMovEtaSec();
    }

    @Override protected String getNombreFromModel(Movimiento m) { return String.valueOf(m.getMovEtaSec()); }
    @Override protected void setEstadoEnCampos(String e) { txtEstReg.setText(e); }
    @Override protected String getEstadoFromCampos()     { return txtEstReg.getText(); }

    // -------------------------------------------------------------------------
    // Selección en grilla — clave de 7 partes
    // -------------------------------------------------------------------------
    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        registroSeleccionadoId = tableModel.getValueAt(row, 0) + "|"
                + tableModel.getValueAt(row, 1) + "|" + tableModel.getValueAt(row, 2) + "|"
                + tableModel.getValueAt(row, 3) + "|" + tableModel.getValueAt(row, 4) + "|"
                + tableModel.getValueAt(row, 5) + "|" + tableModel.getValueAt(row, 6);
        try {
            Movimiento m = dao.obtenerPorId(registroSeleccionadoId);
            if (m != null) { cargarModeloEnCampos(m); registroSeleccionado = m; }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    // -------------------------------------------------------------------------
    // ejecutarActualizar
    // -------------------------------------------------------------------------
    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            MovimientoDAO mDAO = (MovimientoDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    Movimiento nuevo = obtenerModeloDeCampos();
                    mDAO.insertar(nuevo); // calcula MovEtaSec internamente
                    JOptionPane.showMessageDialog(this, "Movimiento registrado (EtaSec=" + nuevo.getMovEtaSec() + ").");
                    break;
                case "MOD":
                    mDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Movimiento modificado.");
                    break;
                case "DELETE":
                    mDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Movimiento eliminado (*).");
                    break;
                case "INACTIVATE":
                    mDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Movimiento inactivado (I).");
                    break;
                case "REACTIVATE":
                    mDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Movimiento reactivado (A).");
                    break;
            }
            cargarTabla(); resetearDespuesDeOperacion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    // -------------------------------------------------------------------------
    // Panel de campos
    // -------------------------------------------------------------------------
    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registro de Movimiento (Horas Trabajadas)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Proyecto (filtra equipo y etapas)
        cmbProyecto = new JComboBox<>();
        cargarComboProyecto();
        cmbProyecto.addActionListener(e -> {
            Proyecto p = (Proyecto) cmbProyecto.getSelectedItem();
            if (p != null) actualizarCombosEncadenados(p.getProCliCod(), p.getProTipProCod(), p.getProSecPro());
        });
        addRow(panel, gbc, row++, "Proyecto (Cli|Tip|Sec):", cmbProyecto);

        // Equipo (miembro del proyecto)
        cmbEquipo = new JComboBox<>();
        addRow(panel, gbc, row++, "Miembro Equipo (Per|Car):", cmbEquipo);

        // Etapa del proyecto
        cmbEtapa = new JComboBox<>();
        addRow(panel, gbc, row++, "Etapa del Proyecto:", cmbEtapa);

        // EtaSec (auto, solo lectura)
        txtEtaSec = new JTextField(); txtEtaSec.setEditable(false); txtEtaSec.setBackground(Color.LIGHT_GRAY);
        addRow(panel, gbc, row++, "Sec. Movimiento (auto):", txtEtaSec);

        // Fecha de registro
        txtFecRegEta = new JTextField();
        addRow(panel, gbc, row++, "Fecha Registro (yyyy-MM-dd):", txtFecRegEta);

        // Horas (0-12) con JSpinner
        spnHoras = new JSpinner(new SpinnerNumberModel(0, 0, 12, 1));
        addRow(panel, gbc, row++, "Horas trabajadas (0-12):", spnHoras);

        // Minutos (0, 15, 30, 45)
        cmbMinutos = new JComboBox<>(new Integer[]{0, 15, 30, 45});
        addRow(panel, gbc, row++, "Minutos (0/15/30/45):", cmbMinutos);

        // Estado lógico
        txtEstReg = new JTextField(); txtEstReg.setEditable(false); txtEstReg.setBackground(Color.LIGHT_GRAY);
        addRow(panel, gbc, row, "Estado Reg. (A/I/*):", txtEstReg);

        // Inicializar combos encadenados con el primer proyecto cargado
        Proyecto pIni = (Proyecto) cmbProyecto.getSelectedItem();
        if (pIni != null) actualizarCombosEncadenados(pIni.getProCliCod(), pIni.getProTipProCod(), pIni.getProSecPro());

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void cargarComboProyecto() {
        cmbProyecto.removeAllItems();
        try {
            new ProyectoDAO().listarTodos().stream()
                .filter(p -> "A".equals(p.getProEstReg())).forEach(cmbProyecto::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Proyectos: " + ex.getMessage()); }
    }

    /**
     * Recarga cmbEquipo y cmbEtapa en función del proyecto seleccionado.
     * Este es el patrón de JComboBox encadenados.
     */
    private void actualizarCombosEncadenados(int cliCod, int tipCod, int sec) {
        // --- Equipo ---
        cmbEquipo.removeAllItems();
        try {
            listaPersonal = new PersonalDAO().listarActivos();
            List<EquipoProyecto> equipo = new EquipoProyectoDAO().listarActivosPorProyecto(cliCod, tipCod, sec);
            for (EquipoProyecto eq : equipo) cmbEquipo.addItem(eq);
            // Renderer: muestra "perCod|carCod – NombrePersonal"
            cmbEquipo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof EquipoProyecto eq && listaPersonal != null) {
                        String nom = listaPersonal.stream()
                                .filter(p -> p.getCodigo() == eq.getEqpPerCod())
                                .map(Personal::getNombre).findFirst().orElse("?");
                        setText(eq.getEqpPerCod() + "|" + eq.getEqpCarCod() + " – " + nom);
                    }
                    return this;
                }
            });
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Equipo: " + ex.getMessage()); }

        // --- Etapas del proyecto ---
        cmbEtapa.removeAllItems();
        try {
            List<ProyectoEtapa> etapas = new ProyectoEtapaDAO().listarActivasPorProyecto(cliCod, tipCod, sec);
            for (ProyectoEtapa pe : etapas) cmbEtapa.addItem(pe);
            cmbEtapa.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof ProyectoEtapa pe) {
                        setText("Etapa " + pe.getProEtaCod());
                    }
                    return this;
                }
            });
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Etapas: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoMovimientoFrame().setVisible(true));
    }
}
