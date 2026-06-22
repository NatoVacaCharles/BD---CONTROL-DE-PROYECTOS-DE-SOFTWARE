package ui;

import dao.EquipoProyectoDAO;
import dao.PersonalCargoPryDAO;
import dao.PersonalDAO;
import dao.ProyectoDAO;
import model.EquipoProyecto;
import model.Personal;
import model.PersonalCargoPry;
import model.Proyecto;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Mantenimiento para P3T_EQUIPO_PROYECTO.
 * Asigna miembros (Personal + su Cargo de Proyecto) a un Proyecto.
 *
 * PK de 5 partes: EqpProCliCod | EqpProTipCod | EqpProSec | EqpPerCod | EqpCarCod
 * FKs via JComboBox:
 *   - Proyecto (3 partes): cmbProyecto
 *   - PersonalCargoPry (2 partes): cmbPersonalCargo  (muestra "perCod|carCod – nombre personal – cargo")
 * Estado lógico EqpEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoEquipoProyectoFrame extends MantenimientoBaseFrame<EquipoProyecto, String> {

    private JComboBox<Proyecto>         cmbProyecto;     // FK 3-partes → P2M_PROYECTO
    private JComboBox<PersonalCargoPry> cmbPersonalCargo; // FK 2-partes → P3T_PERSONAL_CARGO_PRY
    private JTextField txtEstReg;

    // Para mostrar nombres legibles en el combo de PersonalCargo
    private List<Personal> listaPersonal;

    @Override
    protected EquipoProyectoDAO getDao() { return new EquipoProyectoDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3T_EQUIPO_PROYECTO"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"CliCod", "TipPro", "SecPro", "PerCod", "CarCod", "Est.Reg."};
    }

    @Override
    protected Object[] modelToRow(EquipoProyecto e) {
        String est = switch (e.getEqpEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> e.getEqpEstReg();
        };
        return new Object[]{e.getEqpProCliCod(), e.getEqpProTipCod(), e.getEqpProSec(),
                e.getEqpPerCod(), e.getEqpCarCod(), est};
    }

    @Override
    protected void cargarModeloEnCampos(EquipoProyecto e) {
        // Seleccionar proyecto
        for (int i = 0; i < cmbProyecto.getItemCount(); i++) {
            Proyecto p = cmbProyecto.getItemAt(i);
            if (p.getProCliCod() == e.getEqpProCliCod() && p.getProTipProCod() == e.getEqpProTipCod()
                    && p.getProSecPro() == e.getEqpProSec()) {
                cmbProyecto.setSelectedIndex(i); break;
            }
        }
        // Seleccionar personal+cargo
        for (int i = 0; i < cmbPersonalCargo.getItemCount(); i++) {
            PersonalCargoPry pc = cmbPersonalCargo.getItemAt(i);
            if (pc.getPerCarPerCod() == e.getEqpPerCod() && pc.getPerCarCodCar() == e.getEqpCarCod()) {
                cmbPersonalCargo.setSelectedIndex(i); break;
            }
        }
        txtEstReg.setText(e.getEqpEstReg());
    }

    @Override
    protected EquipoProyecto obtenerModeloDeCampos() throws IllegalArgumentException {
        Proyecto proSel = (Proyecto) cmbProyecto.getSelectedItem();
        if (proSel == null) throw new IllegalArgumentException("Seleccione un proyecto.");
        PersonalCargoPry pcSel = (PersonalCargoPry) cmbPersonalCargo.getSelectedItem();
        if (pcSel == null) throw new IllegalArgumentException("Seleccione un Personal/Cargo.");
        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";
        return new EquipoProyecto(proSel.getProCliCod(), proSel.getProTipProCod(), proSel.getProSecPro(),
                pcSel.getPerCarPerCod(), pcSel.getPerCarCodCar(), estReg);
    }

    @Override
    protected void limpiarCampos() {
        if (cmbProyecto.getItemCount() > 0) cmbProyecto.setSelectedIndex(0);
        if (cmbPersonalCargo.getItemCount() > 0) cmbPersonalCargo.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd = "ADD".equals(operacion);
        cmbProyecto.setEnabled(esAdd);
        cmbPersonalCargo.setEnabled(esAdd);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        cmbProyecto.setEnabled(editable); cmbPersonalCargo.setEnabled(editable);
    }

    @Override
    protected void setEditableNombre(boolean editable) {}

    @Override
    protected String getCodigoFromModel(EquipoProyecto model) {
        return model.getEqpProCliCod() + "|" + model.getEqpProTipCod() + "|" + model.getEqpProSec()
             + "|" + model.getEqpPerCod() + "|" + model.getEqpCarCod();
    }

    @Override
    protected String getNombreFromModel(EquipoProyecto model) { return String.valueOf(model.getEqpPerCod()); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        registroSeleccionadoId = tableModel.getValueAt(row, 0) + "|"
                + tableModel.getValueAt(row, 1) + "|" + tableModel.getValueAt(row, 2) + "|"
                + tableModel.getValueAt(row, 3) + "|" + tableModel.getValueAt(row, 4);
        try {
            EquipoProyecto e = dao.obtenerPorId(registroSeleccionadoId);
            if (e != null) { cargarModeloEnCampos(e); registroSeleccionado = e; }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            EquipoProyectoDAO eDao = (EquipoProyectoDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    eDao.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Miembro agregado al equipo.");
                    break;
                case "DELETE":
                    eDao.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado (*).");
                    break;
                case "INACTIVATE":
                    eDao.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    eDao.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Registro reactivado (A).");
                    break;
                case "MOD":
                    JOptionPane.showMessageDialog(this, "La PK de esta tabla no es modificable.");
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
        panel.setBorder(BorderFactory.createTitledBorder("Equipo de Proyecto"));

        panel.add(new JLabel("Proyecto (Cli|Tip|Sec):"));
        cmbProyecto = new JComboBox<>(); cargarComboProyecto(); panel.add(cmbProyecto);

        panel.add(new JLabel("Personal / Cargo Pry.:"));
        cmbPersonalCargo = new JComboBox<>(); cargarComboPersonalCargo(); panel.add(cmbPersonalCargo);

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

    private void cargarComboPersonalCargo() {
        cmbPersonalCargo.removeAllItems();
        try {
            // Cargar lista de personal para renderizar nombres en el combo
            listaPersonal = new PersonalDAO().listarActivos();
            List<PersonalCargoPry> lista = new PersonalCargoPryDAO().listarActivos();
            // Usar un renderer personalizado para mostrar descripción
            for (PersonalCargoPry pc : lista) {
                cmbPersonalCargo.addItem(pc);
            }
            // Renderer: muestra "perCod – NombrePersonal"
            cmbPersonalCargo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof PersonalCargoPry pc) {
                        String nomPer = listaPersonal.stream()
                                .filter(p -> p.getCodigo() == pc.getPerCarPerCod())
                                .map(Personal::getNombre).findFirst().orElse("?");
                        setText(pc.getPerCarPerCod() + "|" + pc.getPerCarCodCar() + " – " + nomPer);
                    }
                    return this;
                }
            });
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Personal/Cargo: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoEquipoProyectoFrame().setVisible(true));
    }
}
