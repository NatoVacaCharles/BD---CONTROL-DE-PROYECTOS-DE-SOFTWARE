package ui;

import dao.CargoProyectoDAO;
import dao.PersonalCargoPryDAO;
import dao.PersonalDAO;
import model.CargoProyecto;
import model.Personal;
import model.PersonalCargoPry;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Mantenimiento para P3T_PERSONAL_CARGO_PRY.
 * Registra qué cargos de proyecto puede desempeñar cada personal.
 *
 * PK compuesta: PerCarPerCod | PerCarCodCar
 * FKs via JComboBox:
 *   - PerCarPerCod → P3M_PERSONAL
 *   - PerCarCodCar → GZZ_CARGO_PROYECTO
 * Estado lógico PerCarEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoPersonalCargoPryFrame extends MantenimientoBaseFrame<PersonalCargoPry, String> {

    private JComboBox<Personal>       cmbPersonal;    // FK PerCarPerCod → P3M_PERSONAL
    private JComboBox<CargoProyecto>  cmbCargoPry;    // FK PerCarCodCar → GZZ_CARGO_PROYECTO
    private JTextField                txtEstReg;      // solo lectura

    @Override
    protected PersonalCargoPryDAO getDao() { return new PersonalCargoPryDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3T_PERSONAL_CARGO_PRY"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Cód. Personal", "Cód. Cargo Pry.", "Est. Reg."};
    }

    @Override
    protected Object[] modelToRow(PersonalCargoPry m) {
        String est = switch (m.getPerCarEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> m.getPerCarEstReg();
        };
        return new Object[]{m.getPerCarPerCod(), m.getPerCarCodCar(), est};
    }

    @Override
    protected void cargarModeloEnCampos(PersonalCargoPry m) {
        seleccionarPersonal(m.getPerCarPerCod());
        seleccionarCargoPry(m.getPerCarCodCar());
        txtEstReg.setText(m.getPerCarEstReg());
    }

    @Override
    protected PersonalCargoPry obtenerModeloDeCampos() throws IllegalArgumentException {
        Personal perSel = (Personal) cmbPersonal.getSelectedItem();
        if (perSel == null) throw new IllegalArgumentException("Seleccione un personal.");
        CargoProyecto carSel = (CargoProyecto) cmbCargoPry.getSelectedItem();
        if (carSel == null) throw new IllegalArgumentException("Seleccione un cargo de proyecto.");
        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";
        return new PersonalCargoPry(perSel.getCodigo(), carSel.getCodigo(), estReg);
    }

    @Override
    protected void limpiarCampos() {
        if (cmbPersonal.getItemCount() > 0) cmbPersonal.setSelectedIndex(0);
        if (cmbCargoPry.getItemCount() > 0) cmbCargoPry.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd = "ADD".equals(operacion);
        cmbPersonal.setEnabled(esAdd);
        cmbCargoPry.setEnabled(esAdd);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        cmbPersonal.setEnabled(editable); cmbCargoPry.setEnabled(editable);
    }

    @Override
    protected void setEditableNombre(boolean editable) { /* no aplica */ }

    @Override
    protected String getCodigoFromModel(PersonalCargoPry model) {
        return model.getPerCarPerCod() + "|" + model.getPerCarCodCar();
    }

    @Override
    protected String getNombreFromModel(PersonalCargoPry model) { return String.valueOf(model.getPerCarCodCar()); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        int perCod = (int) tableModel.getValueAt(row, 0);
        int carCod = (int) tableModel.getValueAt(row, 1);
        registroSeleccionadoId = perCod + "|" + carCod;
        try {
            PersonalCargoPry m = dao.obtenerPorId(registroSeleccionadoId);
            if (m != null) { cargarModeloEnCampos(m); registroSeleccionado = m; }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            PersonalCargoPryDAO pDao = (PersonalCargoPryDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    pDao.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Cargo de proyecto asignado correctamente.");
                    break;
                case "DELETE":
                    pDao.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    pDao.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    pDao.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Registro reactivado (A).");
                    break;
                case "MOD":
                    JOptionPane.showMessageDialog(this, "Esta tabla no permite modificación (solo insertar/cambiar estado).");
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
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Asignación Personal ↔ Cargo de Proyecto"));

        panel.add(new JLabel("Personal:"));
        cmbPersonal = new JComboBox<>(); cargarComboPersonal(); panel.add(cmbPersonal);

        panel.add(new JLabel("Cargo de Proyecto:"));
        cmbCargoPry = new JComboBox<>(); cargarComboCargoPry(); panel.add(cmbCargoPry);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField(); txtEstReg.setEditable(false);
        txtEstReg.setBackground(Color.LIGHT_GRAY); panel.add(txtEstReg);

        return panel;
    }

    private void cargarComboPersonal() {
        cmbPersonal.removeAllItems();
        try {
            new PersonalDAO().listarActivos().forEach(cmbPersonal::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Personal: " + ex.getMessage()); }
    }

    private void cargarComboCargoPry() {
        cmbCargoPry.removeAllItems();
        try {
            List<CargoProyecto> lista = new CargoProyectoDAO().listarTodos();
            lista.stream().filter(c -> "A".equals(c.getEstado())).forEach(cmbCargoPry::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Cargos Proyecto: " + ex.getMessage()); }
    }

    private void seleccionarPersonal(int cod) {
        for (int i = 0; i < cmbPersonal.getItemCount(); i++)
            if (cmbPersonal.getItemAt(i).getCodigo() == cod) { cmbPersonal.setSelectedIndex(i); return; }
    }

    private void seleccionarCargoPry(int cod) {
        for (int i = 0; i < cmbCargoPry.getItemCount(); i++)
            if (cmbCargoPry.getItemAt(i).getCodigo() == cod) { cmbCargoPry.setSelectedIndex(i); return; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoPersonalCargoPryFrame().setVisible(true));
    }
}
