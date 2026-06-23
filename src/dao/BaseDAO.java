package dao;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base genérica para DAOs de tablas referenciales.
 * 
 * @param <T>  Tipo del modelo (ej. EstadoCliente)
 * @param <ID> Tipo de la clave primaria (String, Integer, etc.)
 */
public abstract class BaseDAO<T, ID> {

    // Métodos abstractos que cada DAO debe implementar
    protected abstract String getTableName();

    protected abstract String getCodigoColumnName();

    protected abstract String getNombreColumnName();

    protected abstract String getEstadoColumnName(); // puede ser null si no usa estado lógico

    protected abstract T mapResultSetToModel(ResultSet rs) throws SQLException;

    protected abstract void setInsertParameters(PreparedStatement ps, T model) throws SQLException;

    // Para tablas con estado lógico, el estado inicial es 'A'
    protected boolean hasLogicalDelete() {
        return getEstadoColumnName() != null;
    }

    protected void validarFilasAfectadas(int filasAfectadas, ID codigo, String operacion) throws SQLException {
        if (filasAfectadas > 0) {
            return;
        }

        if (existeCodigo(codigo)) {
            throw new SQLException("No se realizaron cambios al " + operacion
                    + ". Verifique que el nuevo valor sea distinto al actual.");
        }

        throw new SQLException("No se encontró el registro con código: " + codigo);
    }

    // Operación común: listar todos los registros (ordenados por código)
    public List<T> listarTodos() throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY " + getCodigoColumnName();
        List<T> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToModel(rs));
            }
        }
        return lista;
    }

    /**
     * Lista todos los registros EXCEPTO los eliminados lógicamente (estado = '*').
     * Es el método que usa la grilla de mantenimiento — los registros con '*'
     * desaparecen de la vista tras una eliminación lógica, igual que en cualquier
     * sistema empresarial.
     * Las subclases pueden sobrescribirlo si tienen un ORDER BY más complejo.
     */
    public List<T> listarSinEliminados() throws SQLException {
        String estadoCol = getEstadoColumnName();
        // Si la tabla no tiene columna de estado, devolvemos todos
        if (estadoCol == null) return listarTodos();
        String sql = "SELECT * FROM " + getTableName()
                + " WHERE " + estadoCol + " != '*'"
                + " ORDER BY " + getCodigoColumnName();
        List<T> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToModel(rs));
            }
        }
        return lista;
    }

    // Insertar nuevo registro
    public void insertar(T model) throws SQLException {
        String sql;
        if (hasLogicalDelete()) {
            sql = "INSERT INTO " + getTableName() + " (" + getCodigoColumnName() + ", "
                    + getNombreColumnName() + ", " + getEstadoColumnName() + ") VALUES (?, ?, 'A')";
        } else {
            sql = "INSERT INTO " + getTableName() + " (" + getCodigoColumnName() + ", "
                    + getNombreColumnName() + ") VALUES (?, ?)";
        }
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            setInsertParameters(ps, model);
            ps.executeUpdate();
        }
    }

    // Actualizar solo el nombre/descripción
    public void actualizarNombre(ID codigo, String nuevoNombre) throws SQLException {
        String sql = "UPDATE " + getTableName() + " SET " + getNombreColumnName() + " = ? WHERE "
                + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setObject(2, codigo);
            int filasAfectadas = ps.executeUpdate();
            validarFilasAfectadas(filasAfectadas, codigo, "modificar");
        }
    }

    // Cambiar estado lógico (A, I, *)
    public void cambiarEstado(ID codigo, String nuevoEstado) throws SQLException {
        if (!hasLogicalDelete()) {
            throw new UnsupportedOperationException("Esta tabla no soporta eliminación lógica. Use eliminarFisico().");
        }
        String sql = "UPDATE " + getTableName() + " SET " + getEstadoColumnName() + " = ? WHERE "
                + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setObject(2, codigo);
            int filasAfectadas = ps.executeUpdate();
            validarFilasAfectadas(filasAfectadas, codigo, "cambiar el estado de");
        }
    }

    // Eliminación físico
    public void eliminarFisico(ID codigo) throws SQLException {
        if (hasLogicalDelete()) {
            throw new UnsupportedOperationException("Use cambiarEstado() para eliminación lógica.");
        }
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, codigo);
            int filasAfectadas = ps.executeUpdate();
            validarFilasAfectadas(filasAfectadas, codigo, "eliminar");
        }
    }

    // Verificar existencia de un código
    public boolean existeCodigo(ID codigo) throws SQLException {
        String sql = "SELECT 1 FROM " + getTableName() + " WHERE " + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public T obtenerPorId(ID id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToModel(rs);
                }
                return null;
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
