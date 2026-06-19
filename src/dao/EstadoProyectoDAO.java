package dao;

import model.EstadoProyecto;
import java.sql.*;

public class EstadoProyectoDAO extends BaseDAO<EstadoProyecto, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_ESTADO_PROYECTO";
    }

    @Override
    protected String getCodigoColumnName() {
        return "EstProCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "EstProNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "EstProEstReg";
    }

    @Override
    protected EstadoProyecto mapResultSetToModel(ResultSet rs) throws SQLException {
        EstadoProyecto ep = new EstadoProyecto();
        ep.setCodigo(rs.getInt("EstProCod"));
        ep.setNombre(rs.getString("EstProNom"));
        ep.setEstado(rs.getString("EstProEstReg"));
        return ep;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EstadoProyecto model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }
}