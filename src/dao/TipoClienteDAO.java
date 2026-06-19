package dao;

import model.TipoCliente;
import java.sql.*;

public class TipoClienteDAO extends BaseDAO<TipoCliente, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_TIPO_CLIENTE";
    }

    @Override
    protected String getCodigoColumnName() {
        return "TipCliCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "TipCliNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "TipCliEstReg";
    }

    @Override
    protected TipoCliente mapResultSetToModel(ResultSet rs) throws SQLException {
        TipoCliente tc = new TipoCliente();
        tc.setCodigo(rs.getInt("TipCliCod"));
        tc.setDescripcion(rs.getString("TipCliNom"));
        tc.setEstado(rs.getString("TipCliEstReg"));
        return tc;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, TipoCliente model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getDescripcion());
    }
}