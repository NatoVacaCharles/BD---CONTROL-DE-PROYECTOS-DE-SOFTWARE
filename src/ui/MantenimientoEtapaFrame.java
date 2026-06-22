package ui;

import dao.EtapaDAO;
import model.Etapa;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de mantenimiento para P3M_ETAPA.
 *
 * PK simple EtaCod (manual, SMALLINT).
 * No tiene FKs de negocio (solo EstReg gestionado internamente).
 * Estado lógico EtaEstReg: A / I / * (sin DELETE físico).
 */
public class MantenimientoEtapaFrame extends MantenimientoBaseFrame<Etapa, Integer> {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtEstReg;

    @Override
    protected EtapaDAO getDao() { return new EtapaDAO(); }

    @Override
    protected String getTituloVentana() { return "Mantenimiento - P3M_ETAPA"; }

    @Override
    protected String[] getNombresColumnas() {
        return new String[]{"Código", "Nombre de la Etapa", "Estado Reg."};
    }

    @Override
    protected Object[] modelToRow(Etapa e) {
        String estLabel = switch (e.getEstReg()) {
            case "A" -> "Activo"; case "I" -> "Inactivo"; case "*" -> "Eliminado";
            default  -> e.getEstReg();
        };
        return new Object[]{e.getCodigo(), e.getNombre(), estLabel};
    }

    @Override
    protected void cargarModeloEnCampos(Etapa e) {
        txtCodigo.setText(String.valueOf(e.getCodigo()));
        txtNombre.setText(e.getNombre());
        txtEstReg.setText(e.getEstReg());
    }

    @Override
    protected Etapa obtenerModeloDeCampos() throws IllegalArgumentException {
        int cod;
        try { cod = Integer.parseInt(txtCodigo.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("El código debe ser un número entero."); }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        String estReg = txtEstReg.getText().trim();
        if (estReg.isEmpty()) estReg = "A";
        return new Etapa(cod, nombre, estReg);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText(""); txtNombre.setText(""); txtEstReg.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        switch (operacion) {
            case "ADD"  -> { txtCodigo.setEditable(true); txtNombre.setEditable(true); }
            case "MOD"  -> { txtCodigo.setEditable(false); txtNombre.setEditable(true); }
            default     -> { txtCodigo.setEditable(false); txtNombre.setEditable(false); }
        }
        txtEstReg.setEditable(false);
    }

    @Override
    protected void setEditableCodigo(boolean editable) { txtCodigo.setEditable(editable); }

    @Override
    protected void setEditableNombre(boolean editable) { txtNombre.setEditable(editable); }

    @Override
    protected Integer getCodigoFromModel(Etapa model) { return model.getCodigo(); }

    @Override
    protected String getNombreFromModel(Etapa model) { return model.getNombre(); }

    @Override
    protected void setEstadoEnCampos(String estado) { txtEstReg.setText(estado); }

    @Override
    protected String getEstadoFromCampos() { return txtEstReg.getText(); }

    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Etapa"));

        panel.add(new JLabel("Código (SMALLINT):"));
        txtCodigo = new JTextField(); panel.add(txtCodigo);

        panel.add(new JLabel("Nombre de la Etapa:"));
        txtNombre = new JTextField(); panel.add(txtNombre);

        panel.add(new JLabel("Estado Reg. (A/I/*):"));
        txtEstReg = new JTextField(); txtEstReg.setEditable(false);
        txtEstReg.setBackground(Color.LIGHT_GRAY); panel.add(txtEstReg);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoEtapaFrame().setVisible(true));
    }
}
