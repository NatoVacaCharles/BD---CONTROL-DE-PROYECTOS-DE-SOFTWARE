package ui;

import dao.EtapaDAO;
import dao.ProyectoDAO;
import dao.ProyectoEtapaDAO;
import model.Etapa;
import model.Proyecto;
import model.ProyectoEtapa;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Mantenimiento para P3T_PROYECTO_ETAPA.
 * Asigna etapas a proyectos con tiempos estimados ajustados y fechas de inicio/fin.
 *
 * PK 4 partes: ProEtaCliCod | ProEtaTipCod | ProEtaSec | ProEtaCod
 * FKs via JComboBox:
 *   - Proyecto (3 partes) → P2M_PROYECTO
 *   - ProEtaCod → P3M_ETAPA
 * Estado lógico ProEtaEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoProyectoEtapaFrame extends MantenimientoBaseFrame<ProyectoEtapa, String> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JComboBox<Proyecto> cmbProyecto;
    private JComboBox<Etapa>    cmbEtapa;
    private JTextField txtTpoEstAju;
    private JTextField txtFecIni;
    private JTextField txtFecFin;
    private JTextField txtEstReg;

    @Override
    protected ProyectoEtapaDAO getDao() { return new ProyectoEtapaDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3T_PROYECTO_ETAPA"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod", "TipPro", "SecPro", "EtaCod", "Tpo.AjuH", "Fec.Ini", "Fec.Fin", "Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(ProyectoEtapa pe) {
        String est = switch (pe.getProEtaEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> pe.getProEtaEstReg();
        };
        return new Object[]{pe.getProEtaCliCod(), pe.getProEtaTipCod(), pe.getProEtaSec(), pe.getProEtaCod(),
                pe.getProEtaTpoEstAju(),
                pe.getProEtaFecIni() != null ? pe.getProEtaFecIni().format(DATE_FMT) : "",
                pe.getProEtaFecFin() != null ? pe.getProEtaFecFin().format(DATE_FMT) : "",
                est};
    }

    @Override
    protected void cargarModeloEnCampos(ProyectoEtapa pe) {
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            Proyecto p = cmbProyecto.getItemAt(i);
            if (p.getProCliCod() == pe.getProEtaCliCod() && p.getProTipProCod() == pe.getProEtaTipCod()
                    && p.getProSecPro() == pe.getProEtaSec()) {
                cmbProyecto.setSelectedIndex(i); break;
            }
        }
        for (int i = 0; i < cmbEtapa.getItemCount(); i++) {
            if (cmbEtapa.getItemAt(i).getCodigo() == pe.getProEtaCod()) {
                cmbEtapa.setSelectedIndex(i); break;
            }
        }
        txtTpoEstAju.setText(pe.getProEtaTpoEstAju() != null ? pe.getProEtaTpoEstAju().toPlainString() : "");
        txtFecIni.setText(pe.getProEtaFecIni() != null ? pe.getProEtaFecIni().format(DATE_FMT) : "");
        txtFecFin.setText(pe.getProEtaFecFin() != null ? pe.getProEtaFecFin().format(DATE_FMT) : "");
        txtEstReg.setText(pe.getProEtaEstReg());
    }

    @Override
    protected ProyectoEtapa obtenerModeloDeCampos() throws IllegalArgumentException {
        Proyecto proSel = (Proyecto) cmbProyecto.getSelectedItem();
        if (proSel == null) throw new IllegalArgumentException("Seleccione un proyecto.");
        Etapa etaSel = (Etapa) cmbEtapa.getSelectedItem();
        if (etaSel == null) throw new IllegalArgumentException("Seleccione una etapa.");
        BigDecimal tpo;
        try { tpo = new BigDecimal(txtTpoEstAju.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Tiempo ajustado debe ser numérico > 0."); }
        if (tpo.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Tiempo ajustado debe ser > 0.");
        LocalDate fecIni = parseOpcional(txtFecIni.getText(), "Fecha Inicio");
        LocalDate fecFin = parseOpcional(txtFecFin.getText(), "Fecha Fin");
        String estReg = txtEstReg.getText().trim(); if (estReg.isEmpty()) estReg = "A";
        ProyectoEtapa pe = new ProyectoEtapa();
        pe.setProEtaCliCod(proSel.getProCliCod()); pe.setProEtaTipCod(proSel.getProTipProCod());
        pe.setProEtaSec(proSel.getProSecPro());     pe.setProEtaCod(etaSel.getCodigo());
        pe.setProEtaTpoEstAju(tpo); pe.setProEtaFecIni(fecIni); pe.setProEtaFecFin(fecFin);
        pe.setProEtaEstReg(estReg);
        return pe;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        if (cmbEtapa.getItemCount() > 0) cmbEtapa.setSelectedIndex(0);
        txtTpoEstAju.setText(""); txtFecIni.setText(""); txtFecFin.setText(""); txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd  = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbProyecto.setEnabled(esAdd); cmbEtapa.setEnabled(esAdd);
        txtTpoEstAju.setEditable(esEdit); txtFecIni.setEditable(esEdit); txtFecFin.setEditable(esEdit);
        txtEstReg.setEditable(false);
    }

    @Override protected void setEditableCodigo(boolean e) { cmbProyecto.setEnabled(e); cmbEtapa.setEnabled(e); }
    @Override protected void setEditableNombre(boolean e) { txtTpoEstAju.setEditable(e); }

    @Override
    protected String getCodigoFromModel(ProyectoEtapa model) {
        return model.getProEtaCliCod() + "|" + model.getProEtaTipCod() + "|"
             + model.getProEtaSec() + "|" + model.getProEtaCod();
    }

    @Override protected String getNombreFromModel(ProyectoEtapa m) { return String.valueOf(m.getProEtaCod()); }
    @Override protected void setEstadoEnCampos(String e) { txtEstReg.setText(e); }
    @Override protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        registroSeleccionadoId = tableModel.getValueAt(row, 0) + "|" + tableModel.getValueAt(row, 1)
                + "|" + tableModel.getValueAt(row, 2) + "|" + tableModel.getValueAt(row, 3);
        try {
            ProyectoEtapa pe = dao.obtenerPorId(registroSeleccionadoId);
            if (pe != null) { cargarModeloEnCampos(pe); registroSeleccionado = pe; }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            ProyectoEtapaDAO peDAO = (ProyectoEtapaDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    peDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Etapa asignada al proyecto.");
                    break;
                case "MOD":
                    peDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Etapa de proyecto modificada.");
                    break;
                case "DELETE":
                    peDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado (*).");
                    break;
                case "INACTIVATE":
                    peDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    peDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Registro reactivado (A).");
                    break;
            }
            cargarTabla(); resetearDespuesDeOperacion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Etapas del Proyecto"));

        panel.add(new JLabel("Proyecto (Cli|Tip|Sec):"));
        cmbProyecto = new JComboBox<>(); cargarComboProyecto(); panel.add(cmbProyecto);

        panel.add(new JLabel("Etapa:"));
        cmbEtapa = new JComboBox<>(); cargarComboEtapa(); panel.add(cmbEtapa);

        panel.add(new JLabel("Tpo. Est. Ajustado h (>0):")); txtTpoEstAju = new JTextField(); panel.add(txtTpoEstAju);
        panel.add(new JLabel("Fec. Inicio (yyyy-MM-dd, opc.):")); txtFecIni = new JTextField(); panel.add(txtFecIni);
        panel.add(new JLabel("Fec. Fin (yyyy-MM-dd, opc.):")); txtFecFin = new JTextField(); panel.add(txtFecFin);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField(); txtEstReg.setEditable(false);
        txtEstReg.setBackground(Color.LIGHT_GRAY); panel.add(txtEstReg);
        return panel;
    }

    private void cargarComboProyecto() {
        cmbProyecto.removeAllItems();
        try {
            new ProyectoDAO().listarTodos().stream()
                .filter(p -> "A".equals(p.getProEstReg())).forEach(cmbProyecto::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Proyectos: " + ex.getMessage()); }
    }

    private void cargarComboEtapa() {
        cmbEtapa.removeAllItems();
        try { new EtapaDAO().listarActivas().forEach(cmbEtapa::addItem); }
        catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Etapas: " + ex.getMessage()); }
    }

    private LocalDate parseOpcional(String s, String campo) {
        if (s == null || s.trim().isEmpty()) return null;
        try { return LocalDate.parse(s.trim(), DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException(campo + " inválida. Use yyyy-MM-dd."); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoProyectoEtapaFrame().setVisible(true));
    }
}
