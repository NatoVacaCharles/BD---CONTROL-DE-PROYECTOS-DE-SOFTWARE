package ui;

import dao.ActividadDAO;
import dao.EtapaDAO;
import model.Actividad;
import model.Etapa;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana de mantenimiento para P3M_ACTIVIDAD.
 *
 * PK compuesta: ActEtaCod (FK → P3M_ETAPA via JComboBox) + ActCod (manual TINYINT).
 * FK gestionada con JComboBox:
 *   - ActEtaCod → P3M_ETAPA
 * Estado lógico ActEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoActividadFrame extends MantenimientoBaseFrame<Actividad, String> {

    // PK compuesta
    private JComboBox<Etapa> cmbEtapa;  // FK ActEtaCod → P3M_ETAPA
    private JTextField txtActCod;       // TINYINT UNSIGNED (manual)

    // Campos de datos
    private JTextField txtNombre;
    private JTextField txtTiempoEst;
    private JTextField txtEstReg;

    @Override
    protected ActividadDAO getDao() { return new ActividadDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3M_ACTIVIDAD"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Cód. Etapa", "Cód. Act.", "Nombre Actividad", "Tpo. Est. (h)", "Est. Reg."};
    }

    @Override
    protected Object[] modelToRow(Actividad a) {
        String estLabel = switch (a.getActEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> a.getActEstReg();
        };
        return new Object[]{a.getActEtaCod(), a.getActCod(), a.getActNom(), a.getActTpoEst(), estLabel};
    }

    @Override
    protected void cargarModeloEnCampos(Actividad a) {
        for (int i = 0; i < cmbEtapa.getItemCount(); i++) {
            if (cmbEtapa.getItemAt(i).getCodigo() == a.getActEtaCod()) { cmbEtapa.setSelectedIndex(i); break; }
        }
        txtActCod.setText(String.valueOf(a.getActCod()));
        txtNombre.setText(a.getActNom());
        txtTiempoEst.setText(a.getActTpoEst() != null ? a.getActTpoEst().toPlainString() : "");
        txtEstReg.setText(a.getActEstReg());
    }

    @Override
    protected Actividad obtenerModeloDeCampos() throws IllegalArgumentException {
        Etapa etaSel = (Etapa) cmbEtapa.getSelectedItem();
        if (etaSel == null) throw new IllegalArgumentException("Seleccione una etapa.");
        int actCod;
        try { actCod = Integer.parseInt(txtActCod.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Cód. Actividad debe ser entero."); }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        BigDecimal tpo;
        try { tpo = new BigDecimal(txtTiempoEst.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Tiempo estimado debe ser numérico > 0."); }
        if (tpo.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Tiempo estimado debe ser > 0.");
        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";
        return new Actividad(etaSel.getCodigo(), actCod, nombre, tpo, estReg);
    }

    @Override
    protected void limpiarCampos() {
        if (cmbEtapa.getItemCount() > 0) cmbEtapa.setSelectedIndex(0);
        txtActCod.setText(""); txtNombre.setText("");
        txtTiempoEst.setText(""); txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        boolean esAdd = "ADD".equals(operacion);
        boolean esEdit = esAdd || "MOD".equals(operacion);
        cmbEtapa.setEnabled(esAdd);      // PK parte 1: solo editable en ADD
        txtActCod.setEditable(esAdd);    // PK parte 2: solo editable en ADD
        txtNombre.setEditable(esEdit);
        txtTiempoEst.setEditable(esEdit);
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        cmbEtapa.setEnabled(editable); txtActCod.setEditable(editable);
    }

    @Override
    protected void setEditableNombre(boolean editable) { txtNombre.setEditable(editable); }

    @Override
    protected String getCodigoFromModel(Actividad model) {
        return model.getActEtaCod() + "|" + model.getActCod();
    }

    @Override
    protected String getNombreFromModel(Actividad model) { return model.getActNom(); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    // -------------------------------------------------------------------------
    // Selección en grilla → construir clave compuesta
    // -------------------------------------------------------------------------
    @Override
    protected void cargarRegistroSeleccionadoEnCampos() {
        int row = tblRegistros.getSelectedRow();
        if (row == -1) return;
        int etaCod = (int) tableModel.getValueAt(row, 0);
        int actCod = (int) tableModel.getValueAt(row, 1);
        registroSeleccionadoId = etaCod + "|" + actCod;
        try {
            Actividad a = dao.obtenerPorId(registroSeleccionadoId);
            if (a != null) { cargarModeloEnCampos(a); registroSeleccionado = a; }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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
            ActividadDAO actDAO = (ActividadDAO) dao;
            switch (operacionPendiente) {
                case "ADD":
                    actDAO.insertar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Actividad registrada correctamente.");
                    break;
                case "MOD":
                    actDAO.actualizar(obtenerModeloDeCampos());
                    JOptionPane.showMessageDialog(this, "Actividad modificada correctamente.");
                    break;
                case "DELETE":
                    actDAO.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Actividad eliminada lógicamente (*).");
                    break;
                case "INACTIVATE":
                    actDAO.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Actividad inactivada (I).");
                    break;
                case "REACTIVATE":
                    actDAO.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Actividad reactivada (A).");
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
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Actividad"));

        panel.add(new JLabel("Etapa (FK):"));
        cmbEtapa = new JComboBox<>(); cargarComboEtapa(); panel.add(cmbEtapa);

        panel.add(new JLabel("Cód. Actividad (TINYINT):"));
        txtActCod = new JTextField(); panel.add(txtActCod);

        panel.add(new JLabel("Nombre Actividad:")); txtNombre = new JTextField(); panel.add(txtNombre);
        panel.add(new JLabel("Tiempo Estimado h (>0):")); txtTiempoEst = new JTextField(); panel.add(txtTiempoEst);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField(); txtEstReg.setEditable(false); txtEstReg.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstReg);

        return panel;
    }

    private void cargarComboEtapa() {
        cmbEtapa.removeAllItems();
        try {
            List<Etapa> lista = new EtapaDAO().listarActivas();
            lista.forEach(cmbEtapa::addItem);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando Etapas: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoActividadFrame().setVisible(true));
    }
}
