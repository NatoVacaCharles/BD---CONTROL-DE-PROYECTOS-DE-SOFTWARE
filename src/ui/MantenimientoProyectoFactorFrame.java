package ui;

import dao.FactorDAO;
import dao.ProyectoDAO;
import dao.ProyectoFactorDAO;
import model.Factor;
import model.Proyecto;
import model.ProyectoFactor;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Mantenimiento para P2T_PROYECTO_FACTOR.
 * Asigna factores de costo a proyectos con su porcentaje de aplicación.
 *
 * PK 4 partes: ProFacCliCod | ProFacTipCod | ProFacSec | ProFacCod
 * FKs via JComboBox:
 *   - Proyecto (3 partes) → P2M_PROYECTO
 *   - ProFacCod → GZZ_FACTOR
 * Estado lógico ProFacEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoProyectoFactorFrame extends MantenimientoBaseFrame<ProyectoFactor, String> {

    private JComboBox<Proyecto> cmbProyecto;
    private JComboBox<Factor>   cmbFactor;
    private JTextField txtPorApl;
    private JTextField txtEstReg;

    @Override
    protected ProyectoFactorDAO getDao() { return new ProyectoFactorDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P2T_PROYECTO_FACTOR"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod", "TipPro", "SecPro", "FacCod", "Por.Apl.(%)", "Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(ProyectoFactor pf) {
        String est = switch (pf.getProFacEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> pf.getProFacEstReg();
        };
        return new Object[]{pf.getProFacCliCod(), pf.getProFacTipCod(), pf.getProFacSec(),
                pf.getProFacCod(), pf.getProFacPorApl(), est};
    }

    @Override
    protected void cargarModeloEnCampos(ProyectoFactor pf) {
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            Proyecto p = cmbProyecto.getItemAt(i);
            if (p.getProCliCod() == pf.getProFacCliCod() && p.getProTipProCod() == pf.getProFacTipCod()
                    && p.getProSecPro() == pf.getProFacSec()) {
                cmbProyecto.setSelectedIndex(i); break;
            }
        }
        for (int i = 0; i < cmbFactor.getItemCount(); i++) {
            if (cmbFactor.getItemAt(i).getCodigo() == pf.getProFacCod()) {
                cmbFactor.setSelectedIndex(i); break;
            }
        }
        txtPorApl.setText(pf.getProFacPorApl() != null ? pf.getProFacPorApl().toPlainString() : "");
        txtEstReg.setText(pf.getProFacEstReg());
    }

    @Override
    protected ProyectoFactor obtenerModeloDeCampos() throws IllegalArgumentException {
        Proyecto proSel = (Proyecto) cmbProyecto.getSelectedItem();
        if (proSel == null) throw new IllegalArgumentException("Seleccione un proyecto.");
        Factor facSel = (Factor) cmbFactor.getSelectedItem();
        if (facSel == null) throw new IllegalArgumentException("Seleccione un factor.");
        BigDecimal por;
        try { por = new BigDecimal(txtPorApl.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Porcentaje debe ser numérico (0-100)."); }
        if (por.compareTo(BigDecimal.ZERO) < 0 || por.compareTo(new BigDecimal("100")) > 0)
            throw new IllegalArgumentException("Porcentaje debe estar entre 0 y 100.");
        String estReg = txtEstReg.getText().trim(); if (estReg.isEmpty()) estReg = "A";
        ProyectoFactor pf = new ProyectoFactor();
        pf.setProFacCliCod(proSel.getProCliCod()); pf.setProFacTipCod(proSel.getProTipProCod());
        pf.setProFacSec(proSel.getProSecPro());     pf.setProFacCod(facSel.getCodigo());
        pf.setProFacPorApl(por);                    pf.setProFacEstReg(estReg);
        return pf;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        if (cmbFactor.getItemCount() > 0) cmbFactor.setSelectedIndex(0);
        txtPorApl.setText(""); txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd  = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbProyecto.setEnabled(esAdd); cmbFactor.setEnabled(esAdd);
        txtPorApl.setEditable(esEdit); txtEstReg.setEditable(false);
    }

    @Override protected void setEditableCodigo(boolean e) { cmbProyecto.setEnabled(e); cmbFactor.setEnabled(e); }
    @Override protected void setEditableNombre(boolean e) { txtPorApl.setEditable(e); }

    @Override
    protected String getCodigoFromModel(ProyectoFactor model) {
        return model.getProFacCliCod() + "|" + model.getProFacTipCod() + "|"
             + model.getProFacSec() + "|" + model.getProFacCod();
    }

    @Override protected String getNombreFromModel(ProyectoFactor m) { return String.valueOf(m.getProFacCod()); }
    @Override protected void setEstadoEnCampos(String e) { txtEstReg.setText(e); }
    @Override protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        registroSeleccionadoId = tableModel.getValueAt(row, 0) + "|" + tableModel.getValueAt(row, 1)
                + "|" + tableModel.getValueAt(row, 2) + "|" + tableModel.getValueAt(row, 3);
        try {
            ProyectoFactor pf = dao.obtenerPorId(registroSeleccionadoId);
            if (pf != null) { cargarModeloEnCampos(pf); registroSeleccionado = pf; }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            ProyectoFactorDAO pfDAO = (ProyectoFactorDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    pfDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Factor asignado al proyecto.");
                    break;
                case "MOD":
                    pfDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Porcentaje actualizado.");
                    break;
                case "DELETE":
                    pfDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado (*).");
                    break;
                case "INACTIVATE":
                    pfDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    pfDAO.cambiarEstado(registroSeleccionadoId, "A");
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
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Factores del Proyecto"));

        panel.add(new JLabel("Proyecto (Cli|Tip|Sec):"));
        cmbProyecto = new JComboBox<>(); cargarComboProyecto(); panel.add(cmbProyecto);

        panel.add(new JLabel("Factor:"));
        cmbFactor = new JComboBox<>(); cargarComboFactor(); panel.add(cmbFactor);

        panel.add(new JLabel("Porcentaje Aplicado (0-100):")); txtPorApl = new JTextField(); panel.add(txtPorApl);

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

    private void cargarComboFactor() {
        cmbFactor.removeAllItems();
        try {
            new FactorDAO().listarTodos().stream()
                .filter(f -> "A".equals(f.getEstado())).forEach(cmbFactor::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Factores: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoProyectoFactorFrame().setVisible(true));
    }
}
