package dao;

import model.TipoProyecto;
import java.sql.*;

public class TipoProyectoDAO extends BaseDAO<TipoProyecto, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_TIPO_PROYECTO";
    }

    @Override
    protected String getCodigoColumnName() {
        return "TipProCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "TipProNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "TipProEstReg";
    }

    @Override
    protected TipoProyecto mapResultSetToModel(ResultSet rs) throws SQLException {
        TipoProyecto tp = new TipoProyecto();
        tp.setCodigo(rs.getInt("TipProCod"));
        tp.setNombre(rs.getString("TipProNom"));
        tp.setEstado(rs.getString("TipProEstReg"));
        return tp;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, TipoProyecto model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }
}