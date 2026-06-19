package dao;

import model.CargoPersonal;
import java.sql.*;

public class CargoPersonalDAO extends BaseDAO<CargoPersonal, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_CARGO_PERSONAL";
    }

    @Override
    protected String getCodigoColumnName() {
        return "CarPerCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "CarPerNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "CarPerEstReg";
    }

    @Override
    protected CargoPersonal mapResultSetToModel(ResultSet rs) throws SQLException {
        CargoPersonal cp = new CargoPersonal();
        cp.setCodigo(rs.getInt("CarPerCod"));
        cp.setNombre(rs.getString("CarPerNom"));
        cp.setEstado(rs.getString("CarPerEstReg"));
        return cp;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, CargoPersonal model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }
}