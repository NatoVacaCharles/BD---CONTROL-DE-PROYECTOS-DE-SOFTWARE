package dao;

import model.CargoProyecto;
import java.sql.*;

public class CargoProyectoDAO extends BaseDAO<CargoProyecto, Integer> {

    @Override
    protected String getTableName() {
        return "GZZ_CARGO_PROYECTO";
    }

    @Override
    protected String getCodigoColumnName() {
        return "CarProCod";
    }

    @Override
    protected String getNombreColumnName() {
        return "CarProNom";
    }

    @Override
    protected String getEstadoColumnName() {
        return "CarProEstReg";
    }

    @Override
    protected CargoProyecto mapResultSetToModel(ResultSet rs) throws SQLException {
        CargoProyecto cp = new CargoProyecto();
        cp.setCodigo(rs.getInt("CarProCod"));
        cp.setNombre(rs.getString("CarProNom"));
        cp.setEstado(rs.getString("CarProEstReg"));
        return cp;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, CargoProyecto model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }
}