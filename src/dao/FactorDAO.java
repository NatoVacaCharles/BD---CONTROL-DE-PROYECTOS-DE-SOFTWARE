package dao;

import model.Factor;
import java.math.BigDecimal;
import java.sql.*;

public class FactorDAO extends BaseDAO<Factor, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_FACTOR";
    }

    @Override
    protected String getCodigoColumnName() {
        return "FacCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "FacNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "FacEstReg";
    }

    @Override
    protected Factor mapResultSetToModel(ResultSet rs) throws SQLException {
        Factor f = new Factor();
        f.setCodigo(rs.getInt("FacCod"));
        f.setNombre(rs.getString("FacNom"));
        f.setPorcentajeBase(rs.getBigDecimal("FacPorBas"));
        f.setEstado(rs.getString("FacEstReg"));
        return f;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Factor model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
        ps.setBigDecimal(3, model.getPorcentajeBase());
    }

    // SOBRESCRIBIR el método insertar para manejar los 3 campos (código, nombre,
    // porcentaje)
    @Override
    public void insertar(Factor model) throws SQLException {
        String sql = "INSERT INTO " + getTableName() + " ("
                + getCodigoColumnName() + ", "
                + getNombreColumnName() + ", "
                + "FacPorBas, "
                + getEstadoColumnName()
                + ") VALUES (?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, model.getCodigo());
            ps.setString(2, model.getNombre());
            ps.setBigDecimal(3, model.getPorcentajeBase());
            ps.executeUpdate();
        }
    }

    // Método adicional para actualizar solo el porcentaje base
    public void actualizarPorcentajeBase(int codigo, BigDecimal nuevoPorcentaje) throws SQLException {
        String sql = "UPDATE " + getTableName() + " SET FacPorBas = ? WHERE " + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoPorcentaje);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
    }

    // Sobrescribir actualizarNombre para que solo actualice el nombre (el
    // porcentaje se maneja aparte)
    @Override
    public void actualizarNombre(Integer codigo, String nuevoNombre) throws SQLException {
        String sql = "UPDATE " + getTableName() + " SET " + getNombreColumnName() + " = ? WHERE "
                + getCodigoColumnName() + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
    }
}