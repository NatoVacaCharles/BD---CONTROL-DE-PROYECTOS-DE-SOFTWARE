package dao;

import model.TipoEstandar;
import java.sql.*;

public class TipoEstandarDAO extends BaseDAO<TipoEstandar, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_TIPO_ESTANDAR";
    }

    @Override
    protected String getCodigoColumnName() {
        return "TipEstCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "TipEstNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "TipEstEstReg";
    }

    @Override
    protected TipoEstandar mapResultSetToModel(ResultSet rs) throws SQLException {
        TipoEstandar te = new TipoEstandar();
        te.setCodigo(rs.getInt("TipEstCod"));
        te.setNombre(rs.getString("TipEstNom"));
        te.setUnidadDefinicion(rs.getString("TipEstUniDef"));
        te.setEstado(rs.getString("TipEstEstReg"));
        return te;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, TipoEstandar model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
        ps.setString(3, model.getUnidadDefinicion());
    }

    // SOBRESCRIBIR el método insertar para manejar los 3 campos
    @Override
    public void insertar(TipoEstandar model) throws SQLException {
        String sql = "INSERT INTO " + getTableName() + " ("
                + getCodigoColumnName() + ", "
                + getNombreColumnName() + ", "
                + "TipEstUniDef, "
                + getEstadoColumnName()
                + ") VALUES (?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, model.getCodigo());
            ps.setString(2, model.getNombre());
            ps.setString(3, model.getUnidadDefinicion());
            ps.executeUpdate();
        }
    }

    // Método adicional para actualizar solo la unidad de definición
    public void actualizarUnidadDefinicion(int codigo, String nuevaUnidad) throws SQLException {
        String sql = "UPDATE " + getTableName() + " SET TipEstUniDef = ? WHERE " + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaUnidad);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
    }
}