package ui;

import dao.ActividadDAO;
import dao.EstandarDAO;
import dao.TipoEstandarDAO;
import model.Actividad;
import model.Estandar;
import model.TipoEstandar;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Ventana de mantenimiento para P3M_ESTANDAR.
 *
 * PK compuesta: EstActEtaCod | EstActActCod (via JComboBox de Actividad) + EstCod (manual).
 * FKs gestionadas con JComboBox:
 *   - (EstActEtaCod, EstActActCod) → P3M_ACTIVIDAD  (ComboBox muestra etaCod-actCod nombre)
 *   - EstTipCod → GZZ_TIPO_ESTANDAR
 *
 * Lógica especial CHECK:
 *   - EstValNum y EstValTxt son mutuamente excluyentes (radio buttons para elegir el tipo).
 *   - Radio "Numérico" habilita txtValNum y deshabilita txtValTxt y vice-versa.
 *
 * Estado lógico EstEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoEstandarFrame extends MantenimientoBaseFrame<Estandar, String> {

    // PK compuesta
    private JComboBox<Actividad>  cmbActividad;  // FK (EtaCod, ActCod) → P3M_ACTIVIDAD
    private JTextField            txtEstCod;      // TINYINT UNSIGNED (manual)

    // FK simple
    private JComboBox<TipoEstandar> cmbTipoEstandar;

    // Campos de datos
    private JTextField txtNombre;
    private JTextField txtUnidad;

    // Valor: exclusivo entre numérico y texto
    private JRadioButton rdoNumerico;
    private JRadioButton rdoTexto;
    private JTextField txtValNum;
    private JTextField txtValTxt;

    // Estado lógico (solo lectura)
    private JTextField txtEstReg;

    @Override
    protected EstandarDAO getDao() { return new EstandarDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3M_ESTANDAR"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Eta.", "Act.", "EstCod", "TipCod", "Nombre", "Val.Num", "Val.Txt", "Unidad", "Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(Estandar e) {
        String estLabel = switch (e.getEstEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> e.getEstEstReg();
        };
        return new Object[]{
                e.getEstActEtaCod(), e.getEstActActCod(), e.getEstCod(), e.getEstTipCod(),
                e.getEstNom(), e.getEstValNum(), e.getEstValTxt(), e.getEstUni(), estLabel};
    }

    @Override
    protected void cargarModeloEnCampos(Estandar e) {
        // Seleccionar actividad en combobox
        for (int i = 0; i < cmbActividad.getItemCount(); i++) {
            Actividad a = cmbActividad.getItemAt(i);
            if (a.getActEtaCod() == e.getEstActEtaCod() && a.getActCod() == e.getEstActActCod()) {
                cmbActividad.setSelectedIndex(i); break;
            }
        }
        txtEstCod.setText(String.valueOf(e.getEstCod()));
        // Tipo Estandar
        for (int i = 0; i < cmbTipoEstandar.getItemCount(); i++) {
            if (cmbTipoEstandar.getItemAt(i).getCodigo() == e.getEstTipCod()) {
                cmbTipoEstandar.setSelectedIndex(i); break;
            }
        }
        txtNombre.setText(e.getEstNom());
        txtUnidad.setText(e.getEstUni());
        // Valor exclusivo
        if (e.getEstValNum() != null) {
            rdoNumerico.setSelected(true);
            txtValNum.setText(e.getEstValNum().toPlainString());
            txtValTxt.setText("");
        } else {
            rdoTexto.setSelected(true);
            txtValTxt.setText(e.getEstValTxt() != null ? e.getEstValTxt() : "");
            txtValNum.setText("");
        }
        actualizarVisibilidadValores();
        txtEstReg.setText(e.getEstEstReg());
    }

    @Override
    protected Estandar obtenerModeloDeCampos() throws IllegalArgumentException {
        Actividad actSel = (Actividad) cmbActividad.getSelectedItem();
        if (actSel == null) throw new IllegalArgumentException("Seleccione una actividad.");
        int estCod;
        try { estCod = Integer.parseInt(txtEstCod.getText().trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Cód. Estándar debe ser entero."); }
        TipoEstandar tipSel = (TipoEstandar) cmbTipoEstandar.getSelectedItem();
        if (tipSel == null) throw new IllegalArgumentException("Seleccione un tipo de estándar.");
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        String unidad = txtUnidad.getText().trim();
        if (unidad.isEmpty()) throw new IllegalArgumentException("La unidad no puede estar vacía.");

        BigDecimal valNum = null;
        String valTxt = null;
        if (rdoNumerico.isSelected()) {
            String vs = txtValNum.getText().trim();
            if (vs.isEmpty()) throw new IllegalArgumentException("Ingrese un valor numérico.");
            try { valNum = new BigDecimal(vs); }
            catch (NumberFormatException ex) { throw new IllegalArgumentException("Valor numérico inválido."); }
            if (valNum.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor numérico debe ser > 0.");
        } else {
            valTxt = txtValTxt.getText().trim();
            if (valTxt.isEmpty()) throw new IllegalArgumentException("Ingrese un valor de texto.");
        }

        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";

        Estandar e = new Estandar();
        e.setEstActEtaCod(actSel.getActEtaCod());
        e.setEstActActCod(actSel.getActCod());
        e.setEstCod(estCod);
        e.setEstTipCod(tipSel.getCodigo());
        e.setEstNom(nombre);
        e.setEstValNum(valNum);
        e.setEstValTxt(valTxt);
        e.setEstUni(unidad);
        e.setEstEstReg(estReg);
        return e;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbActividad.getItemCount() > 0) cmbActividad.setSelectedIndex(0);
        txtEstCod.setText("");
        if (cmbTipoEstandar.getItemCount() > 0) cmbTipoEstandar.setSelectedIndex(0);
        txtNombre.setText(""); txtUnidad.setText("");
        rdoNumerico.setSelected(true);
        txtValNum.setText(""); txtValTxt.setText("");
        actualizarVisibilidadValores();
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd  = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbActividad.setEnabled(esAdd);  // PK parte 1-2: solo en ADD
        txtEstCod.setEditable(esAdd);    // PK parte 3: solo en ADD
        cmbTipoEstandar.setEnabled(esEdit);
        txtNombre.setEditable(esEdit);
        txtUnidad.setEditable(esEdit);
        rdoNumerico.setEnabled(esEdit);
        rdoTexto.setEnabled(esEdit);
        txtValNum.setEditable(esEdit);
        txtValTxt.setEditable(esEdit);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        cmbActividad.setEnabled(editable); txtEstCod.setEditable(editable);
    }

    @Override
    protected void setEditableNombre(boolean editable) { txtNombre.setEditable(editable); }

    @Override
    protected String getCodigoFromModel(Estandar model) {
        return model.getEstActEtaCod() + "|" + model.getEstActActCod() + "|" + model.getEstCod();
    }

    @Override
    protected String getNombreFromModel(Estandar model) { return model.getEstNom(); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    // -------------------------------------------------------------------------
    // Selección en grilla → construir clave compuesta de 3 partes
    // -------------------------------------------------------------------------
    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        int etaCod = (int) tableModel.getValueAt(row, 0);
        int actCod = (int) tableModel.getValueAt(row, 1);
        int estCod = (int) tableModel.getValueAt(row, 2);
        registroSeleccionadoId = etaCod + "|" + actCod + "|" + estCod;
        try {
            Estandar e = dao.obtenerPorId(registroSeleccionadoId);
            if (e != null) { cargarModeloEnCampos(e); registroSeleccionado = e; }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
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
            EstandarDAO estDAO = (EstandarDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    estDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Estándar registrado correctamente.");
                    break;
                case "MOD":
                    estDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Estándar modificado correctamente.");
                    break;
                case "DELETE":
                    estDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Estándar eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    estDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Estándar inactivado (I).");
                    break;
                case "REACTIVATE":
                    estDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Estándar reactivado (A).");
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Estándar"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // --- Actividad (FK compuesta) ---
        addRow(panel, gbc, row++, "Actividad (Eta-Act):",
                cmbActividad = new JComboBox<>());
        cargarComboActividad();

        // --- EstCod (manual) ---
        addRow(panel, gbc, row++, "Cód. Estándar (TINYINT):",
                txtEstCod = new JTextField(5));

        // --- Tipo Estándar (FK) ---
        addRow(panel, gbc, row++, "Tipo Estándar:",
                cmbTipoEstandar = new JComboBox<>());
        cargarComboTipoEstandar();

        // --- Nombre ---
        addRow(panel, gbc, row++, "Nombre:", txtNombre = new JTextField());

        // --- Unidad ---
        addRow(panel, gbc, row++, "Unidad:", txtUnidad = new JTextField());

        // --- Radio buttons para tipo de valor ---
        rdoNumerico = new JRadioButton("Valor Numérico", true);
        rdoTexto    = new JRadioButton("Valor Texto");
        ButtonGroup bgVal = new ButtonGroup();
        bgVal.add(rdoNumerico); bgVal.add(rdoTexto);
        JPanel rdoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rdoPanel.add(rdoNumerico); rdoPanel.add(rdoTexto);
        rdoNumerico.addActionListener(e -> actualizarVisibilidadValores());
        rdoTexto.addActionListener(e -> actualizarVisibilidadValores());
        addRow(panel, gbc, row++, "Tipo de Valor:", rdoPanel);

        // --- Valor Numérico ---
        addRow(panel, gbc, row++, "Valor Numérico (>0):", txtValNum = new JTextField());

        // --- Valor Texto ---
        addRow(panel, gbc, row++, "Valor Texto:", txtValTxt = new JTextField());
        txtValTxt.setEnabled(false);

        // --- Estado lógico ---
        txtEstReg = new JTextField();
        txtEstReg.setEditable(false); txtEstReg.setBackground(Color.LIGHT_GRAY);
        addRow(panel, gbc, row, "Estado Reg. (A/I/*):", txtEstReg);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void actualizarVisibilidadValores() {
        boolean esNum = rdoNumerico.isSelected();
        txtValNum.setEnabled(esNum);
        txtValTxt.setEnabled(!esNum);
        if (esNum) txtValTxt.setText("");
        else       txtValNum.setText("");
    }

    private void cargarComboActividad() {
        cmbActividad.removeAllItems();
        try {
            new ActividadDAO().listarActivas().forEach(cmbActividad::addItem);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Actividades: " + ex.getMessage());
        }
    }

    private void cargarComboTipoEstandar() {
        cmbTipoEstandar.removeAllItems();
        try {
            new TipoEstandarDAO().listarTodos().stream()
                .filter(t -> "A".equals(t.getEstado())).forEach(cmbTipoEstandar::addItem);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Tipos de Estándar: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoEstandarFrame().setVisible(true));
    }
}
