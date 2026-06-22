package dao;

import model.PersonalCargoPry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3T_PERSONAL_CARGO_PRY.
 * PK compuesta (PerCarPerCod, PerCarCodCar) — clave string: "perCod|carCod".
 */
public class PersonalCargoPryDAO extends BaseDAO<PersonalCargoPry, String> {

    @Override
    protected String getTableName() { return "P3T_PERSONAL_CARGO_PRY"; }

    @Override
    protected String getCodigoColumnName() { return "PerCarPerCod"; }

    @Override
    protected String getNombreColumnName() { return "PerCarCodCar"; }

    @Override
    protected String getEstadoColumnName() { return "PerCarEstReg"; }

    @Override
    protected PersonalCargoPry mapResultSetToModel(ResultSet rs) throws SQLException {
        PersonalCargoPry p = new PersonalCargoPry();
        p.setPerCarPerCod(rs.getInt("PerCarPerCod"));
        p.setPerCarCodCar(rs.getInt("PerCarCodCar"));
        p.setPerCarEstReg(rs.getString("PerCarEstReg"));
        return p;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, PersonalCargoPry model) throws SQLException {}

    @Override
    public List<PersonalCargoPry> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3T_PERSONAL_CARGO_PRY ORDER BY PerCarPerCod, PerCarCodCar";
        List<PersonalCargoPry> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public PersonalCargoPry obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3T_PERSONAL_CARGO_PRY WHERE PerCarPerCod=? AND PerCarCodCar=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(p[0]));
            ps.setInt(2, Integer.parseInt(p[1]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    /** INSERT con estado lógico 'A'. */
    @Override
    public void insertar(PersonalCargoPry m) throws SQLException {
        String sql = "INSERT INTO P3T_PERSONAL_CARGO_PRY (PerCarPerCod, PerCarCodCar, PerCarEstReg) VALUES (?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getPerCarPerCod());
            ps.setInt(2, m.getPerCarCodCar());
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico. */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3T_PERSONAL_CARGO_PRY SET PerCarEstReg=? WHERE PerCarPerCod=? AND PerCarCodCar=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(p[0]));
            ps.setInt(3, Integer.parseInt(p[1]));
            ps.executeUpdate();
        }
    }

    /**
     * Listar registros activos para ser usados como FK compuesta en P3T_EQUIPO_PROYECTO.
     * Retorna los pares (perCod, carCod) activos.
     */
    public List<PersonalCargoPry> listarActivos() throws SQLException {
        String sql = "SELECT * FROM P3T_PERSONAL_CARGO_PRY WHERE PerCarEstReg='A' ORDER BY PerCarPerCod, PerCarCodCar";
        List<PersonalCargoPry> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }
}
