package ui;

import dao.TipoEstandarDAO;
import model.TipoEstandar;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MantenimientoTipoEstandarFrame extends MantenimientoBaseFrame<TipoEstandar, Integer> {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtUnidad;
    private JTextField txtEstado;

    @Override
    protected TipoEstandarDAO getDao() {
        return new TipoEstandarDAO();
    }

    @Override
    protected String getTituloVentana() {
        return "Mantenimiento - GZZ_TIPO_ESTANDAR";
    }

    @Override
    protected String[] getNombresColumnas() {
        return new String[] { "Código", "Nombre", "Unidad Definición", "Estado" };
    }

    @Override
    protected Object[] modelToRow(TipoEstandar model) {
        String estadoMostrado = switch (model.getEstado()) {
            case "A" -> "Activo";
            case "I" -> "Inactivo";
            case "*" -> "Eliminado";
            default -> model.getEstado();
        };
        return new Object[] { model.getCodigo(), model.getNombre(), model.getUnidadDefinicion(), estadoMostrado };
    }

    @Override
    protected void cargarModeloEnCampos(TipoEstandar model) {
        txtCodigo.setText(String.valueOf(model.getCodigo()));
        txtNombre.setText(model.getNombre());
        txtUnidad.setText(model.getUnidadDefinicion());
        txtEstado.setText(model.getEstado());
    }

    @Override
    protected TipoEstandar obtenerModeloDeCampos() throws IllegalArgumentException {
        int codigo = Integer.parseInt(txtCodigo.getText().trim());
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        String unidad = txtUnidad.getText().trim();
        if (unidad.isEmpty()) {
            throw new IllegalArgumentException("La unidad de definición no puede estar vacía.");
        }
        String estado = txtEstado.getText().trim();
        if (estado.isEmpty())
            estado = "A";
        return new TipoEstandar(codigo, nombre, unidad, estado);
    }

    @Override
    protected void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtUnidad.setText("");
        txtEstado.setText("");
    }

    @Override
    protected void protegerCamposSegunOperacion(String operacion) {
        switch (operacion) {
            case "ADD":
                txtCodigo.setEditable(true);
                txtNombre.setEditable(true);
                txtUnidad.setEditable(true);
                break;
            case "MOD":
                txtCodigo.setEditable(false);
                txtNombre.setEditable(true);
                txtUnidad.setEditable(true);
                break;
            case "DELETE":
            case "INACTIVATE":
            case "REACTIVATE":
                txtCodigo.setEditable(false);
                txtNombre.setEditable(false);
                txtUnidad.setEditable(false);
                break;
            default:
                txtCodigo.setEditable(false);
                txtNombre.setEditable(false);
                txtUnidad.setEditable(false);
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
    protected Integer getCodigoFromModel(TipoEstandar model) {
        return model.getCodigo();
    }

    @Override
    protected String getNombreFromModel(TipoEstandar model) {
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
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Tipo Estándar"));
        panel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        panel.add(new JLabel("Unidad Definición:"));
        txtUnidad = new JTextField();
        panel.add(txtUnidad);
        panel.add(new JLabel("Estado (A/I/*):"));
        txtEstado = new JTextField();
        txtEstado.setEditable(false);
        txtEstado.setBackground(Color.LIGHT_GRAY);
        panel.add(txtEstado);
        return panel;
    }

    @Override
    protected void ejecutarActualizar() {
        if (CarFlaAct == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado un comando para actualizar un registro de la BD",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            switch (operacionPendiente) {
                case "ADD":
                    TipoEstandar nuevo = obtenerModeloDeCampos();
                    dao.insertar(nuevo);
                    JOptionPane.showMessageDialog(this, "Registro añadido correctamente.");
                    break;

                case "MOD":
                    TipoEstandar modeloMod = obtenerModeloDeCampos();
                    // Actualizar nombre y unidad
                    ((TipoEstandarDAO) dao).actualizarNombre(getCodigoFromModel(modeloMod),
                            getNombreFromModel(modeloMod));
                    ((TipoEstandarDAO) dao).actualizarUnidadDefinicion(getCodigoFromModel(modeloMod),
                            modeloMod.getUnidadDefinicion());
                    JOptionPane.showMessageDialog(this, "Registro modificado correctamente.");
                    break;

                case "DELETE":
                    dao.cambiarEstado(registroSeleccionadoId, "*");
                    JOptionPane.showMessageDialog(this, "Registro eliminado lógicamente (*).");
                    break;

                case "INACTIVATE":
                    dao.cambiarEstado(registroSeleccionadoId, "I");
                    JOptionPane.showMessageDialog(this, "Registro inactivado (I).");
                    break;

                case "REACTIVATE":
                    dao.cambiarEstado(registroSeleccionadoId, "A");
                    JOptionPane.showMessageDialog(this, "Registro reactivado (A).");
                    break;
            }
            cargarTabla();
            resetearDespuesDeOperacion();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MantenimientoTipoEstandarFrame().setVisible(true));
    }
}