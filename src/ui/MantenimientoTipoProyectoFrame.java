package ui;

import dao.TipoProyectoDAO;
import model.TipoProyecto;
import javax.swing.*;
import java.awt.*;

public class MantenimientoTipoProyectoFrame extends MantenimientoBaseFrame<TipoProyecto, Integer> {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtEstado;

    @Override
    protected TipoProyectoDAO getDao() {
        return new TipoProyectoDAO();
    }

    @Override
    protected String getTituloVentana() {
        return "Mantenimiento - GZZ_TIPO_PROYECTO";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[] { "Código", "Nombre", "Estado" };
    }

    @Override
    protected Object[] modelToRow(TipoProyecto model) {
        String estadoMostrado = switch (model.getEstado()) {
            case "A" -> "Activo";
            case "I" -> "Inactivo";
            case "*" -> "Eliminado";
            default -> model.getEstado();
        };
        return new Object[] { model.getCodigo(), model.getNombre(), estadoMostrado };
    }

    @Override
    protected void cargarModeloEnCampos(TipoProyecto model) {
        txtCodigo.setText(String.valueOf(model.getCodigo()));
        txtNombre.setText(model.getNombre());
        txtEstado.setText(model.getEstado());
    }

    @Override
    protected TipoProyecto obtenerModeloDeCampos() throws IllegalArgumentException {
        int codigo = Integer.parseInt(txtCodigo.getText().trim());
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        String estado = txtEstado.getText().trim();
        if (estado.isEmpty())
            estado = "A";
        return new TipoProyecto(codigo, nombre, estado);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEstado.setText("");
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
            case "DELETE":
            case "INACTIVATE":
            case "REACTIVATE":
                txtCodigo.setEditable(false);
                txtNombre.setEditable(false);
                break;
            default:
                txtCodigo.setEditable(false);
                txtNombre.setEditable(false);
                break;
        }
        txtEstado.setEditable(false);
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
    protected Integer getCodigoFromModel(TipoProyecto model) {
        return model.getCodigo();
    }

    @Override
    protected String getNombreFromModel(TipoProyecto model) {
        return model.getNombre();
    }

    @Override
    protected void setEstadoEnCampos(String estado) {
        txtEstado.setText(estado);
    }

    @Override
    protected String getEstadoFromCampos() {
        return txtEstado.getText();
    }

    @Override
    protected JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Tipo Proyecto"));
        panel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        panel.add(new JLabel("Estado (A/I/*):"));
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstado);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoTipoProyectoFrame().setVisible(true));
    }
}