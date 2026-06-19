package dao;

import model.EstadoCliente;
import java.sql.*;

public class EstadoClienteDAO extends BaseDAO<EstadoCliente, String> {

    @Override
    protected String getTableName() {
        return "GZZ_ESTADO_CLIENTE";
    }

    @Override
    protected String getCodigoColumnName() {
        return "EstCliCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "EstCliNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "EstCliEstReg";
    }

    @Override
    protected EstadoCliente mapResultSetToModel(ResultSet rs) throws SQLException {
        EstadoCliente ec = new EstadoCliente();
        ec.setCodigo(rs.getString("EstCliCod"));
        ec.setNombre(rs.getString("EstCliNom"));
        ec.setEstado(rs.getString("EstCliEstReg"));
        return ec;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EstadoCliente model) throws SQLException {
        ps.setString(1, model.getCodigo());
        ps.setString(2, model.getNombre());
        // El estado 'A' se asigna automáticamente en la sentencia SQL
    }
}