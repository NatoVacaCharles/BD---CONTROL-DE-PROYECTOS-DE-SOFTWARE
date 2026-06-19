package ui;

import dao.TipoClienteDAO;
import model.TipoCliente;
import javax.swing.*;
import java.awt.*;

public class MantenimientoTipoClienteFrame extends MantenimientoBaseFrame<TipoCliente, Integer> {

    // Componentes específicos de esta tabla
    private JTextField txtCodigo;
    private JTextField txtDescripcion;
    private JTextField txtEstado;

    @Override
    protected TipoClienteDAO getDao() {
        return new TipoClienteDAO();
    }

    @Override
    protected String getTituloVentana() {
        return "Mantenimiento - GZZ_TIPO_CLIENTE";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[] { "Código", "Descripción", "Estado" };
    }

    @Override
    protected Object[] modelToRow(TipoCliente model) {
        String estadoMostrado;
        switch (model.getEstado()) {
            case "A":
                estadoMostrado = "Activo";
                break;
            case "I":
                estadoMostrado = "Inactivo";
                break;
            case "*":
                estadoMostrado = "Eliminado";
                break;
            default:
                estadoMostrado = model.getEstado();
        }
        return new Object[] { model.getCodigo(), model.getDescripcion(), estadoMostrado };
    }

    @Override
    protected void cargarModeloEnCampos(TipoCliente model) {
        txtCodigo.setText(String.valueOf(model.getCodigo()));
        txtDescripcion.setText(model.getDescripcion());
        txtEstado.setText(model.getEstado());
    }

    @Override
    protected TipoCliente obtenerModeloDeCampos() throws IllegalArgumentException {
        int codigo = Integer.parseInt(txtCodigo.getText().trim());
        String descripcion = txtDescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        String estado = txtEstado.getText().trim();
        if (estado.isEmpty())
            estado = "A";
        return new TipoCliente(codigo, descripcion, estado);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtEstado.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        switch (operacion) {
            case "ADD":
                txtCodigo.setEditable(true);
                txtDescripcion.setEditable(true);
                break;
            case "MOD":
                txtCodigo.setEditable(false);
                txtDescripcion.setEditable(true);
                break;
            case "DELETE":
            case "INACTIVATE":
            case "REACTIVATE":
                txtCodigo.setEditable(false);
                txtDescripcion.setEditable(false);
                break;
            default:
                txtCodigo.setEditable(false);
                txtDescripcion.setEditable(false);
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
        txtDescripcion.setEditable(editable);
    }

    @Override
    protected Integer getCodigoFromModel(TipoCliente model) {
        return model.getCodigo();
    }

    @Override
    protected String getNombreFromModel(TipoCliente model) {
        return model.getDescripcion();
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Tipo Cliente"));
        panel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);
        panel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panel.add(txtDescripcion);
        panel.add(new JLabel("Estado (A/I/*):"));
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstado);
        return panel;
    }

    // Punto de entrada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoTipoClienteFrame().setVisible(true));
    }
}