package dao;

import model.EstadoRegistro;
import java.sql.*;

public class EstadoRegistroDAO extends BaseDAO<EstadoRegistro, String> {

    @Override
    protected String getTableName() {
        return "GZZ_ESTADO_REGISTRO";
    }

    @Override
    protected String getCodigoColumnName() {
        return "EstRegCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "EstRegNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return null; // No tiene columna de estado lógico
    }

    @Override
    protected EstadoRegistro mapResultSetToModel(ResultSet rs) throws SQLException {
        EstadoRegistro er = new EstadoRegistro();
        er.setCodigo(rs.getString("EstRegCod"));
        er.setNombre(rs.getString("EstRegNom"));
        return er;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EstadoRegistro model) throws SQLException {
        ps.setString(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }

    // Opcional: si se desea exponer el método eliminarFisico (ya está heredado)
}