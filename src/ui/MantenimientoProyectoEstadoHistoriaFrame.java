package ui;

import dao.EstadoProyectoDAO;
import dao.PersonalDAO;
import dao.ProyectoDAO;
import dao.ProyectoEstadoHistoriaDAO;
import model.EstadoProyecto;
import model.Personal;
import model.Proyecto;
import model.ProyectoEstadoHistoria;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Mantenimiento para P2H_PROYECTO_ESTADO.
 * Registra el historial de cambios de estado de un proyecto.
 *
 * PK 4 partes: HisProCliCod | HisProTipCod | HisProSec | HisProSecCam
 * HisProSecCam se calcula automáticamente en ADD.
 *
 * FKs via JComboBox:
 *   - Proyecto (3 partes) → P2M_PROYECTO
 *   - HisProEstAnt → GZZ_ESTADO_PROYECTO (estado anterior)
 *   - HisProEstNue → GZZ_ESTADO_PROYECTO (estado nuevo)
 *   - HisProPerCod → P3M_PERSONAL (quién realizó el cambio)
 *
 * Tabla histórica: NO existe MOD de datos (solo ADD y cambio de estado lógico).
 * Estado lógico HisProEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoProyectoEstadoHistoriaFrame
        extends MantenimientoBaseFrame<ProyectoEstadoHistoria, String> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // PK
    private JComboBox<Proyecto>      cmbProyecto;
    private JTextField               txtSecCam;    // auto-calculado, solo lectura

    // FKs
    private JComboBox<EstadoProyecto> cmbEstAnt;   // estado anterior
    private JComboBox<EstadoProyecto> cmbEstNue;   // estado nuevo
    private JComboBox<Personal>       cmbPersonal; // quién realizó el cambio
    private JTextField               txtFecCam;    // DATE NOT NULL

    // Estado lógico
    private JTextField txtEstReg;

    @Override
    protected ProyectoEstadoHistoriaDAO getDao() { return new ProyectoEstadoHistoriaDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P2H_PROYECTO_ESTADO"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod","TipPro","SecPro","SecCam","EstAnt","EstNue","Fec.Cam","PerCod","Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(ProyectoEstadoHistoria h) {
        String est = switch (h.getHisProEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> h.getHisProEstReg();
        };
        return new Object[]{
            h.getHisProCliCod(), h.getHisProTipCod(), h.getHisProSec(), h.getHisProSecCam(),
            h.getHisProEstAnt(), h.getHisProEstNue(),
            h.getHisProFecCam() != null ? h.getHisProFecCam().format(DATE_FMT) : "",
            h.getHisProPerCod(), est
        };
    }

    @Override
    protected void cargarModeloEnCampos(ProyectoEstadoHistoria h) {
        // Proyecto
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            Proyecto p = cmbProyecto.getItemAt(i);
            if (p.getProCliCod() == h.getHisProCliCod() && p.getProTipProCod() == h.getHisProTipCod()
                    && p.getProSecPro() == h.getHisProSec()) {
                cmbProyecto.setSelectedIndex(i); break;
            }
        }
        txtSecCam.setText(String.valueOf(h.getHisProSecCam()));
        // Estado anterior
        for (int i = 0; i < cmbEstAnt.getItemCount(); i++) {
            if (cmbEstAnt.getItemAt(i).getCodigo() == h.getHisProEstAnt()) { cmbEstAnt.setSelectedIndex(i); break; }
        }
        // Estado nuevo
        for (int i = 0; i < cmbEstNue.getItemCount(); i++) {
            if (cmbEstNue.getItemAt(i).getCodigo() == h.getHisProEstNue()) { cmbEstNue.setSelectedIndex(i); break; }
        }
        txtFecCam.setText(h.getHisProFecCam() != null ? h.getHisProFecCam().format(DATE_FMT) : "");
        // Personal
        for (int i = 0; i < cmbPersonal.getItemCount(); i++) {
            if (cmbPersonal.getItemAt(i).getCodigo() == h.getHisProPerCod()) { cmbPersonal.setSelectedIndex(i); break; }
        }
        txtEstReg.setText(h.getHisProEstReg());
    }

    @Override
    protected ProyectoEstadoHistoria obtenerModeloDeCampos() throws IllegalArgumentException {
        Proyecto proSel = (Proyecto) cmbProyecto.getSelectedItem();
        if (proSel == null) throw new IllegalArgumentException("Seleccione un proyecto.");

        EstadoProyecto estAntSel = (EstadoProyecto) cmbEstAnt.getSelectedItem();
        if (estAntSel == null) throw new IllegalArgumentException("Seleccione el estado anterior.");

        EstadoProyecto estNueSel = (EstadoProyecto) cmbEstNue.getSelectedItem();
        if (estNueSel == null) throw new IllegalArgumentException("Seleccione el estado nuevo.");

        if (estAntSel.getCodigo() == estNueSel.getCodigo())
            throw new IllegalArgumentException("El estado anterior y nuevo no pueden ser iguales.");

        String fecStr = txtFecCam.getText().trim();
        if (fecStr.isEmpty()) throw new IllegalArgumentException("La Fecha de Cambio es obligatoria.");
        LocalDate fecCam;
        try { fecCam = LocalDate.parse(fecStr, DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha inválida. Use yyyy-MM-dd."); }

        Personal perSel = (Personal) cmbPersonal.getSelectedItem();
        if (perSel == null) throw new IllegalArgumentException("Seleccione el personal responsable.");

        String estReg = txtEstReg.getText().trim(); if (estReg.isEmpty()) estReg = "A";

        ProyectoEstadoHistoria h = new ProyectoEstadoHistoria();
        h.setHisProCliCod(proSel.getProCliCod()); h.setHisProTipCod(proSel.getProTipProCod());
        h.setHisProSec(proSel.getProSecPro());
        h.setHisProSecCam(0); // auto-calculado en insertar()
        h.setHisProEstAnt(estAntSel.getCodigo()); h.setHisProEstNue(estNueSel.getCodigo());
        h.setHisProFecCam(fecCam); h.setHisProPerCod(perSel.getCodigo());
        h.setHisProEstReg(estReg);
        return h;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        txtSecCam.setText("");
        if (cmbEstAnt.getItemCount() > 0) cmbEstAnt.setSelectedIndex(0);
        if (cmbEstNue.getItemCount() > 0) cmbEstNue.setSelectedIndex(0);
        txtFecCam.setText("");
        if (cmbPersonal.getItemCount() > 0) cmbPersonal.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        // Histórica: MOD no aplica (solo ADD y cambio de estado lógico)
        boolean esAdd = "ADD".equals(operacion);
        cmbProyecto.setEnabled(esAdd); cmbEstAnt.setEnabled(esAdd);
        cmbEstNue.setEnabled(esAdd); cmbPersonal.setEnabled(esAdd);
        txtFecCam.setEditable(esAdd);
        txtSecCam.setEditable(false); txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean e) { cmbProyecto.setEnabled(e); }
    @Override protected void setEditableNombre(boolean e) { txtFecCam.setEditable(e); }

    @Override
    protected String getCodigoFromModel(ProyectoEstadoHistoria model) {
        return model.getHisProCliCod() + "|" + model.getHisProTipCod() + "|"
             + model.getHisProSec() + "|" + model.getHisProSecCam();
    }

    @Override protected String getNombreFromModel(ProyectoEstadoHistoria m) { return String.valueOf(m.getHisProSecCam()); }
    @Override protected void setEstadoEnCampos(String e) { txtEstReg.setText(e); }
    @Override protected String getEstadoFromCampos()     { return txtEstReg.getText(); }

    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        registroSeleccionadoId = tableModel.getValueAt(row, 0) + "|" + tableModel.getValueAt(row, 1)
                + "|" + tableModel.getValueAt(row, 2) + "|" + tableModel.getValueAt(row, 3);
        try {
            ProyectoEstadoHistoria h = dao.obtenerPorId(registroSeleccionadoId);
            if (h != null) { cargarModeloEnCampos(h); registroSeleccionado = h; }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            ProyectoEstadoHistoriaDAO hDAO = (ProyectoEstadoHistoriaDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    ProyectoEstadoHistoria nuevo = obtenerModeloDeCampos();
                    hDAO.insertar(nuevo);
                    JOptionPane.showMessageDialog(this, "Cambio de estado registrado (SecCam=" + nuevo.getHisProSecCam() + ").");
                    break;
                case "MOD":
                    // Tabla histórica: no se modifica el contenido, solo se informa
                    JOptionPane.showMessageDialog(this,
                            "Los registros históricos no son modificables.\nSolo puede cambiar su estado lógico (Inactivar/Reactivar).",
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "DELETE":
                    hDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    hDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    hDAO.cambiarEstado(registroSeleccionadoId, "A");
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
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Historial de Cambios de Estado del Proyecto"));

        panel.add(new JLabel("Proyecto (Cli|Tip|Sec):"));
        cmbProyecto = new JComboBox<>(); cargarComboProyecto(); panel.add(cmbProyecto);

        panel.add(new JLabel("Sec. Cambio (auto):"));
        txtSecCam = new JTextField(); txtSecCam.setEditable(false);
        txtSecCam.setBackground(Color.LIGHT_GRAY); panel.add(txtSecCam);

        panel.add(new JLabel("Estado Anterior:"));
        cmbEstAnt = new JComboBox<>(); cargarComboEstados(cmbEstAnt); panel.add(cmbEstAnt);

        panel.add(new JLabel("Estado Nuevo:"));
        cmbEstNue = new JComboBox<>(); cargarComboEstados(cmbEstNue); panel.add(cmbEstNue);

        panel.add(new JLabel("Fecha Cambio (yyyy-MM-dd):"));
        txtFecCam = new JTextField(); panel.add(txtFecCam);

        panel.add(new JLabel("Personal Responsable:"));
        cmbPersonal = new JComboBox<>(); cargarComboPersonal(); panel.add(cmbPersonal);

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

    private void cargarComboEstados(JComboBox<EstadoProyecto> cmb) {
        cmb.removeAllItems();
        try {
            new EstadoProyectoDAO().listarTodos().stream()
                .filter(e -> "A".equals(e.getEstado())).forEach(cmb::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Estados: " + ex.getMessage()); }
    }

    private void cargarComboPersonal() {
        cmbPersonal.removeAllItems();
        try { new PersonalDAO().listarActivos().forEach(cmbPersonal::addItem); }
        catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Personal: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoProyectoEstadoHistoriaFrame().setVisible(true));
    }
}
