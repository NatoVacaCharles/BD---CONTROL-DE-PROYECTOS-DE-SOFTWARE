package ui;

import dao.EstadoDisponibilidadDAO;
import dao.PersonalDAO;
import dao.PersonalDisponibilidadDAO;
import model.EstadoDisponibilidad;
import model.Personal;
import model.PersonalDisponibilidad;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Mantenimiento para P3T_PERSONAL_DISPONIBILIDAD.
 * Registra períodos de disponibilidad del personal.
 *
 * PK compuesta: PerDisPerCod | PerDisFecDes (clave: "perCod|yyyy-MM-dd")
 * FKs via JComboBox:
 *   - PerDisPerCod → P3M_PERSONAL
 *   - PerDisEstDis → GZZ_ESTADO_DISPONIBILIDAD
 * Estado lógico PerDisEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoPersonalDisponibilidadFrame extends MantenimientoBaseFrame<PersonalDisponibilidad, String> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // PK parte 1 → JComboBox; parte 2 → campo de fecha
    private JComboBox<Personal>           cmbPersonal;
    private JTextField                    txtFecDes;    // DATE (parte de PK — readonly en MOD)

    // FK → JComboBox
    private JComboBox<EstadoDisponibilidad> cmbEstDis;

    // Fecha hasta (opcional)
    private JTextField txtFecHas;

    // Estado lógico (solo lectura)
    private JTextField txtEstReg;

    @Override
    protected PersonalDisponibilidadDAO getDao() { return new PersonalDisponibilidadDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3T_PERSONAL_DISPONIBILIDAD"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Cód. Personal", "Fec. Desde", "Fec. Hasta", "Est. Dis.", "Est. Reg."};
    }

    @Override
    protected Object[] modelToRow(PersonalDisponibilidad pd) {
        String est = switch (pd.getPerDisEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> pd.getPerDisEstReg();
        };
        return new Object[]{
                pd.getPerDisPerCod(),
                pd.getPerDisFecDes()  != null ? pd.getPerDisFecDes().format(DATE_FMT)  : "",
                pd.getPerDisFecHas() != null ? pd.getPerDisFecHas().format(DATE_FMT) : "",
                pd.getPerDisEstDis(),
                est
        };
    }

    @Override
    protected void cargarModeloEnCampos(PersonalDisponibilidad pd) {
        // Seleccionar personal en combobox
        for (int i = 0; i < cmbPersonal.getItemCount(); i++) {
            if (cmbPersonal.getItemAt(i).getCodigo() == pd.getPerDisPerCod()) {
                cmbPersonal.setSelectedIndex(i); break;
            }
        }
        txtFecDes.setText(pd.getPerDisFecDes() != null ? pd.getPerDisFecDes().format(DATE_FMT) : "");
        txtFecHas.setText(pd.getPerDisFecHas() != null ? pd.getPerDisFecHas().format(DATE_FMT) : "");
        // Seleccionar estado disponibilidad
        for (int i = 0; i < cmbEstDis.getItemCount(); i++) {
            if (cmbEstDis.getItemAt(i).getCodigo().equals(pd.getPerDisEstDis())) {
                cmbEstDis.setSelectedIndex(i); break;
            }
        }
        txtEstReg.setText(pd.getPerDisEstReg());
    }

    @Override
    protected PersonalDisponibilidad obtenerModeloDeCampos() throws IllegalArgumentException {
        Personal perSel = (Personal) cmbPersonal.getSelectedItem();
        if (perSel == null) throw new IllegalArgumentException("Seleccione un personal.");

        String fecDesStr = txtFecDes.getText().trim();
        if (fecDesStr.isEmpty()) throw new IllegalArgumentException("La Fecha Desde es obligatoria (yyyy-MM-dd).");
        LocalDate fecDes;
        try { fecDes = LocalDate.parse(fecDesStr, DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha Desde inválida. Use yyyy-MM-dd."); }

        LocalDate fecHas = null;
        String fecHasStr = txtFecHas.getText().trim();
        if (!fecHasStr.isEmpty()) {
            try { fecHas = LocalDate.parse(fecHasStr, DATE_FMT); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha Hasta inválida. Use yyyy-MM-dd."); }
        }
        if (fecHas != null && fecHas.isBefore(fecDes))
            throw new IllegalArgumentException("Fecha Hasta no puede ser anterior a Fecha Desde.");

        EstadoDisponibilidad estDisSel = (EstadoDisponibilidad) cmbEstDis.getSelectedItem();
        if (estDisSel == null) throw new IllegalArgumentException("Seleccione un estado de disponibilidad.");

        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";

        PersonalDisponibilidad pd = new PersonalDisponibilidad();
        pd.setPerDisPerCod(perSel.getCodigo());
        pd.setPerDisFecDes(fecDes);
        pd.setPerDisFecHas(fecHas);
        pd.setPerDisEstDis(estDisSel.getCodigo());
        pd.setPerDisEstReg(estReg);
        return pd;
    }

    @Override
    protected void limpiarCampos() {
        if (cmbPersonal.getItemCount() > 0) cmbPersonal.setSelectedIndex(0);
        txtFecDes.setText(""); txtFecHas.setText("");
        if (cmbEstDis.getItemCount() > 0) cmbEstDis.setSelectedIndex(0);
        txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd  = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbPersonal.setEnabled(esAdd);   // PK parte 1: bloqueada en MOD
        txtFecDes.setEditable(esAdd);    // PK parte 2 (fecha): bloqueada en MOD
        cmbEstDis.setEnabled(esEdit);
        txtFecHas.setEditable(esEdit);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        cmbPersonal.setEnabled(editable); txtFecDes.setEditable(editable);
    }

    @Override protected void setEditableNombre(boolean editable) { txtFecHas.setEditable(editable); }

    @Override
    protected String getCodigoFromModel(PersonalDisponibilidad model) {
        return model.getPerDisPerCod() + "|"
             + (model.getPerDisFecDes() != null ? model.getPerDisFecDes().format(DATE_FMT) : "");
    }

    @Override protected String getNombreFromModel(PersonalDisponibilidad m) {
        return m.getPerDisFecDes() != null ? m.getPerDisFecDes().format(DATE_FMT) : "";
    }

    @Override protected void setEstadoEnCampos(String e) { txtEstReg.setText(e); }
    @Override protected String getEstadoFromCampos()     { return txtEstReg.getText(); }

    // -------------------------------------------------------------------------
    // Selección en grilla — clave compuesta con fecha
    // -------------------------------------------------------------------------
    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        String perCod  = tableModel.getValueAt(row, 0).toString();
        String fecDes  = tableModel.getValueAt(row, 1).toString();
        registroSeleccionadoId = perCod + "|" + fecDes;
        try {
            PersonalDisponibilidad pd = dao.obtenerPorId(registroSeleccionadoId);
            if (pd != null) { cargarModeloEnCampos(pd); registroSeleccionado = pd; }
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
            PersonalDisponibilidadDAO pdDAO = (PersonalDisponibilidadDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    pdDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Disponibilidad registrada correctamente.");
                    break;
                case "MOD":
                    pdDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Disponibilidad modificada correctamente.");
                    break;
                case "DELETE":
                    pdDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    pdDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;
                case "REACTIVATE":
                    pdDAO.cambiarEstado(registroSeleccionadoId, "A");
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

    // -------------------------------------------------------------------------
    // Panel de campos
    // -------------------------------------------------------------------------
    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Disponibilidad del Personal"));

        panel.add(new JLabel("Personal (FK):"));
        cmbPersonal = new JComboBox<>(); cargarComboPersonal(); panel.add(cmbPersonal);

        panel.add(new JLabel("Fecha Desde (yyyy-MM-dd) *:"));
        txtFecDes = new JTextField(); panel.add(txtFecDes);

        panel.add(new JLabel("Fecha Hasta (yyyy-MM-dd, opc.):"));
        txtFecHas = new JTextField(); panel.add(txtFecHas);

        panel.add(new JLabel("Estado Disponibilidad:"));
        cmbEstDis = new JComboBox<>(); cargarComboEstDis(); panel.add(cmbEstDis);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField();
        txtEstReg.setEditable(false); txtEstReg.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstReg);

        return panel;
    }

    private void cargarComboPersonal() {
        cmbPersonal.removeAllItems();
        try { new PersonalDAO().listarActivos().forEach(cmbPersonal::addItem); }
        catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Personal: " + ex.getMessage()); }
    }

    private void cargarComboEstDis() {
        cmbEstDis.removeAllItems();
        try {
            List<EstadoDisponibilidad> lista = new EstadoDisponibilidadDAO().listarTodos();
            lista.stream().filter(e -> "A".equals(e.getEstado())).forEach(cmbEstDis::addItem);
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error cargando Estados Disponibilidad: " + ex.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoPersonalDisponibilidadFrame().setVisible(true));
    }
}
