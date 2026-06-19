package dao;

import model.EstadoDisponibilidad;
import java.sql.*;

public class EstadoDisponibilidadDAO extends BaseDAO<EstadoDisponibilidad, String> {

    @Override
    protected String getTableName() {
        return "GZZ_ESTADO_DISPONIBILIDAD";
    }

    @Override
    protected String getCodigoColumnName() {
        return "EstDisCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "EstDisNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "EstDisEstReg";
    }

    @Override
    protected EstadoDisponibilidad mapResultSetToModel(ResultSet rs) throws SQLException {
        EstadoDisponibilidad ed = new EstadoDisponibilidad();
        ed.setCodigo(rs.getString("EstDisCod"));
        ed.setNombre(rs.getString("EstDisNom"));
        ed.setEstado(rs.getString("EstDisEstReg"));
        return ed;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EstadoDisponibilidad model) throws SQLException {
        ps.setString(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }
}