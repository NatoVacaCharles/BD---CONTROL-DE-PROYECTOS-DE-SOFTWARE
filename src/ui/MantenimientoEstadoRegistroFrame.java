package ui;

import dao.EstadoRegistroDAO;
import model.EstadoRegistro;
import javax.swing.*;
import java.awt.*;

public class MantenimientoEstadoRegistroFrame extends MantenimientoBaseFrame<EstadoRegistro, String> {

    private JTextField txtCodigo;
    private JTextField txtNombre;

    @Override
    protected EstadoRegistroDAO getDao() {
        return new EstadoRegistroDAO();
    }

    @Override
    protected String getTituloVentana() {
        return "Mantenimiento - GZZ_ESTADO_REGISTRO";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[] { "Código", "Nombre" };
    }

    @Override
    protected Object[] modelToRow(EstadoRegistro model) {
        return new Object[] { model.getCodigo(), model.getNombre() };
    }

    @Override
    protected void cargarModeloEnCampos(EstadoRegistro model) {
        txtCodigo.setText(model.getCodigo());
        txtNombre.setText(model.getNombre());
    }

    @Override
    protected EstadoRegistro obtenerModeloDeCampos() throws IllegalArgumentException {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        if (codigo.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException("Código y nombre son obligatorios.");
        }
        return new EstadoRegistro(codigo, nombre);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        switch (operacion) {
            case "ADD":
                txtCodigo.setEditable(true);
                txtNombre.setEditable(true);
                break;
            case "MOD":
                txtCodigo.setEditable(false);
                txtNombre.setEditable(true);
                break;
            default:
                txtCodigo.setEditable(false);
                txtNombre.setEditable(false);
                break;
        }
    }

    @Override
    protected void setEditableCodigo(boolean editable) {
        txtCodigo.setEditable(editable);
    }

    @Override
    protected void setEditableNombre(boolean editable) {
        txtNombre.setEditable(editable);
    }

    @Override
    protected String getCodigoFromModel(EstadoRegistro model) {
        return model.getCodigo();
    }

    @Override
    protected String getNombreFromModel(EstadoRegistro model) {
        return model.getNombre();
    }

    @Override
    protected void setEstadoEnCampos(String estado) {
        // No aplica
    }

    @Override
    protected String getEstadoFromCampos() {
        return null;
    }

    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Estado Registro"));
        panel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        return panel;
    }

    @Override
    protected void aplicarEstadoInicial() {
        super.aplicarEstadoInicial();
        // Deshabilitar botones de eliminación lógica (no aplica)
        btnEliminar.setEnabled(false);
        btnInactivar.setEnabled(false);
        btnReactivar.setEnabled(false);
        // Ocultarlos opcionalmente:
        // btnEliminar.setVisible(false);
        // btnInactivar.setVisible(false);
        // btnReactivar.setVisible(false);
    }

    // Sobrescribir métodos de inicio para evitar uso
    @Override
    protected void iniciarEliminar() {
        JOptionPane.showMessageDialog(this,
                "Esta tabla no permite eliminación lógica. Use el botón Actualizar para cambios.");
    }

    @Override
    protected void iniciarInactivar() {
        JOptionPane.showMessageDialog(this, "Esta tabla no permite inactivación.");
    }

    @Override
    protected void iniciarReactivar() {
        JOptionPane.showMessageDialog(this, "Esta tabla no permite reactivación.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoEstadoRegistroFrame().setVisible(true));
    }
}