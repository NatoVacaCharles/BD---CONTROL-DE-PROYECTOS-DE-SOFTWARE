package ui;

import dao.CargoPersonalDAO;
import dao.PersonalDAO;
import model.CargoPersonal;
import model.Personal;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana de mantenimiento para P3M_PERSONAL.
 *
 * FKs gestionadas con JComboBox:
 *  - PerCodCar → GZZ_CARGO_PERSONAL (JComboBox dinámico)
 * PerCod es AUTO_INCREMENT (solo lectura).
 * Estado lógico PerEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoPersonalFrame extends MantenimientoBaseFrame<Personal, Integer> {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTextField txtCodigo;             // AUTO_INCREMENT, solo lectura
    private JComboBox<CargoPersonal> cmbCargo; // FK → GZZ_CARGO_PERSONAL
    private JTextField txtNombre;
    private JTextField txtCostoHora;
    private JTextField txtFechaIngreso;
    private JTextField txtEstReg;             // Estado lógico (A/I/*), solo lectura

    @Override
    protected PersonalDAO getDao() { return new PersonalDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3M_PERSONAL"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Cód.", "Cargo", "Nombre", "Costo/Hora", "Fec. Ingreso", "Est. Reg."};
    }

    @Override
    protected Object[] modelToRow(Personal p) {
        String estLabel = switch (p.getEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> p.getEstReg();
        };
        return new Object[]{p.getCodigo(), p.getCodCargo(), p.getNombre(),
                p.getCostoHoraCargo(),
                p.getFechaIngreso() != null ? p.getFechaIngreso().format(DATE_FMT) : "",
                estLabel};
    }

    @Override
    protected void cargarModeloEnCampos(Personal p) {
        txtCodigo.setText(String.valueOf(p.getCodigo()));
        for (int i = 0; i < cmbCargo.getItemCount(); i++) {
            if (cmbCargo.getItemAt(i).getCodigo() == p.getCodCargo()) {
                cmbCargo.setSelectedIndex(i); break;
            }
        }
        txtNombre.setText(p.getNombre());
        txtCostoHora.setText(p.getCostoHoraCargo() != null ? p.getCostoHoraCargo().toPlainString() : "");
        txtFechaIngreso.setText(p.getFechaIngreso() != null ? p.getFechaIngreso().format(DATE_FMT) : "");
        txtEstReg.setText(p.getEstReg());
    }

    @Override
    protected Personal obtenerModeloDeCampos() throws IllegalArgumentException {
        CargoPersonal cargoSel = (CargoPersonal) cmbCargo.getSelectedItem();
        if (cargoSel == null) throw new IllegalArgumentException("Seleccione un cargo.");
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        BigDecimal costo;
        try { costo = new BigDecimal(txtCostoHora.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Costo/Hora debe ser numérico (ej. 25.50)."); }
        if (costo.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Costo/Hora debe ser mayor que 0.");
        String fecStr = txtFechaIngreso.getText().trim();
        if (fecStr.isEmpty()) throw new IllegalArgumentException("La fecha de ingreso es obligatoria (yyyy-MM-dd).");
        LocalDate fecIng;
        try { fecIng = LocalDate.parse(fecStr, DATE_FMT); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Fecha de ingreso inválida. Use yyyy-MM-dd."); }
        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";
        int cod = 0;
        String codStr = txtCodigo.getText().trim();
        if (!codStr.isEmpty()) { try { cod = Integer.parseInt(codStr); } catch (NumberFormatException ignored) {} }
        return new Personal(cod, cargoSel.getCodigo(), nombre, costo, fecIng, estReg);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        if (cmbCargo.getItemCount() > 0) cmbCargo.setSelectedIndex(0);
        txtNombre.setText(""); txtCostoHora.setText("");
        txtFechaIngreso.setText(""); txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean edit = "ADD".equals(operacion) || "MOD".equals(operacion);
        cmbCargo.setEnabled(edit);
        txtNombre.setEditable(edit);
        txtCostoHora.setEditable(edit);
        txtFechaIngreso.setEditable(edit);
        txtCodigo.setEditable(false);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) { txtCodigo.setEditable(false); }

    @Override
    protected void setEditableNombre(boolean editable) { txtNombre.setEditable(editable); }

    @Override
    protected Integer getCodigoFromModel(Personal model) { return model.getCodigo(); }

    @Override
    protected String getNombreFromModel(Personal model) { return model.getNombre(); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this, "No hay operación pendiente.", "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            PersonalDAO perDAO = (PersonalDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    perDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Personal registrado correctamente.");
                    break;
                case "MOD":
                    perDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Personal modificado correctamente.");
                    break;
                case "DELETE":
                    perDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Personal eliminado lógicamente (*).");
                    break;
                case "INACTIVATE":
                    perDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Personal inactivado (I).");
                    break;
                case "REACTIVATE":
                    perDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Personal reactivado (A).");
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Personal"));

        panel.add(new JLabel("Código (auto):"));
        txtCodigo = new JTextField(); txtCodigo.setEditable(false); txtCodigo.setBackground(Color.LIGHT_GRAY);
        panel.add(txtCodigo);

        panel.add(new JLabel("Cargo Personal:"));
        cmbCargo = new JComboBox<>(); cargarComboCargo(); panel.add(cmbCargo);

        panel.add(new JLabel("Nombre:")); txtNombre = new JTextField(); panel.add(txtNombre);
        panel.add(new JLabel("Costo/Hora (>0):")); txtCostoHora = new JTextField(); panel.add(txtCostoHora);
        panel.add(new JLabel("Fecha Ingreso (yyyy-MM-dd):")); txtFechaIngreso = new JTextField(); panel.add(txtFechaIngreso);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField(); txtEstReg.setEditable(false); txtEstReg.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstReg);

        return panel;
    }

    private void cargarComboCargo() {
        cmbCargo.removeAllItems();
        try {
            List<CargoPersonal> lista = new CargoPersonalDAO().listarTodos();
            lista.stream().filter(c -> "A".equals(c.getEstado())).forEach(cmbCargo::addItem);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Cargos de Personal: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoPersonalFrame().setVisible(true));
    }
}
