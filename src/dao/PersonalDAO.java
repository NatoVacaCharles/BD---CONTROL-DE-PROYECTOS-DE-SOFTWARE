package dao;

import model.Personal;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3M_PERSONAL.
 * PK auto-increment → INSERT no incluye PerCod.
 * FK: PerCodCar → GZZ_CARGO_PERSONAL.
 */
public class PersonalDAO extends BaseDAO<Personal, Integer> {

    @Override
    protected String getTableName() { return "P3M_PERSONAL"; }

    @Override
    protected String getCodigoColumnName() { return "PerCod"; }

    @Override
    protected String getNombreColumnName() { return "PerNom"; }

    @Override
    protected String getEstadoColumnName() { return "PerEstReg"; }

    @Override
    protected Personal mapResultSetToModel(ResultSet rs) throws SQLException {
        Personal p = new Personal();
        p.setCodigo(rs.getInt("PerCod"));
        p.setCodCargo(rs.getInt("PerCodCar"));
        p.setNombre(rs.getString("PerNom"));
        p.setCostoHoraCargo(rs.getBigDecimal("PerCosHorCar"));
        Date d = rs.getDate("PerFecIng");
        p.setFechaIngreso(d != null ? d.toLocalDate() : null);
        p.setEstReg(rs.getString("PerEstReg"));
        return p;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Personal model) throws SQLException {
        // Delegado a insertar()
    }

    /** INSERT: PerCod es AUTO_INCREMENT, se inserta con PerEstReg = 'A'. */
    @Override
    public void insertar(Personal p) throws SQLException {
        String sql = "INSERT INTO P3M_PERSONAL (PerCodCar, PerNom, PerCosHorCar, PerFecIng, PerEstReg) "
                   + "VALUES (?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCodCargo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getCostoHoraCargo());
            ps.setDate(4, p.getFechaIngreso() != null ? Date.valueOf(p.getFechaIngreso()) : null);
            ps.executeUpdate();
        }
    }

    /** UPDATE: modifica cargo, nombre, costo/hora y fecha de ingreso. */
    public void actualizar(Personal p) throws SQLException {
        String sql = "UPDATE P3M_PERSONAL SET PerCodCar=?, PerNom=?, PerCosHorCar=?, PerFecIng=? "
                   + "WHERE PerCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCodCargo());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getCostoHoraCargo());
            ps.setDate(4, p.getFechaIngreso() != null ? Date.valueOf(p.getFechaIngreso()) : null);
            ps.setInt(5, p.getCodigo());
            ps.executeUpdate();
        }
    }

    /** Listar personal activo para ComboBoxes en otras ventanas. */
    public List<Personal> listarActivos() throws SQLException {
        String sql = "SELECT * FROM P3M_PERSONAL WHERE PerEstReg = 'A' ORDER BY PerNom";
        List<Personal> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }
}
