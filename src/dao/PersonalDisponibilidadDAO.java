package dao;

import model.PersonalDisponibilidad;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3T_PERSONAL_DISPONIBILIDAD.
 * PK compuesta (PerDisPerCod, PerDisFecDes) — clave string: "perCod|yyyy-MM-dd".
 * FK: PerDisPerCod → P3M_PERSONAL
 *     PerDisEstDis → GZZ_ESTADO_DISPONIBILIDAD
 */
public class PersonalDisponibilidadDAO extends BaseDAO<PersonalDisponibilidad, String> {

    @Override protected String getTableName()        { return "P3T_PERSONAL_DISPONIBILIDAD"; }
    @Override protected String getCodigoColumnName() { return "PerDisPerCod"; }
    @Override protected String getNombreColumnName() { return "PerDisFecDes"; }
    @Override protected String getEstadoColumnName() { return "PerDisEstReg"; }

    @Override
    protected PersonalDisponibilidad mapResultSetToModel(ResultSet rs) throws SQLException {
        PersonalDisponibilidad pd = new PersonalDisponibilidad();
        pd.setPerDisPerCod(rs.getInt("PerDisPerCod"));
        Date d = rs.getDate("PerDisFecDes");
        pd.setPerDisFecDes(d != null ? d.toLocalDate() : null);
        Date h = rs.getDate("PerDisFecHas");
        pd.setPerDisFecHas(h != null ? h.toLocalDate() : null);
        pd.setPerDisEstDis(rs.getString("PerDisEstDis"));
        pd.setPerDisEstReg(rs.getString("PerDisEstReg"));
        return pd;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, PersonalDisponibilidad model) throws SQLException {}

    @Override
    public List<PersonalDisponibilidad> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3T_PERSONAL_DISPONIBILIDAD ORDER BY PerDisPerCod, PerDisFecDes";
        List<PersonalDisponibilidad> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Clave: "perCod|yyyy-MM-dd" */
    @Override
    public PersonalDisponibilidad obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3T_PERSONAL_DISPONIBILIDAD WHERE PerDisPerCod=? AND PerDisFecDes=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(p[0]));
            ps.setDate(2, Date.valueOf(p[1]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    /** INSERT con estado lógico 'A'. */
    @Override
    public void insertar(PersonalDisponibilidad pd) throws SQLException {
        String sql = "INSERT INTO P3T_PERSONAL_DISPONIBILIDAD "
                + "(PerDisPerCod, PerDisFecDes, PerDisFecHas, PerDisEstDis, PerDisEstReg) "
                + "VALUES (?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pd.getPerDisPerCod());
            ps.setDate(2, Date.valueOf(pd.getPerDisFecDes()));
            ps.setDate(3, pd.getPerDisFecHas() != null ? Date.valueOf(pd.getPerDisFecHas()) : null);
            ps.setString(4, pd.getPerDisEstDis());
            ps.executeUpdate();
        }
    }

    /** UPDATE: modifica fecha hasta y estado de disponibilidad. */
    public void actualizar(PersonalDisponibilidad pd) throws SQLException {
        String sql = "UPDATE P3T_PERSONAL_DISPONIBILIDAD SET PerDisFecHas=?, PerDisEstDis=? "
                + "WHERE PerDisPerCod=? AND PerDisFecDes=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, pd.getPerDisFecHas() != null ? Date.valueOf(pd.getPerDisFecHas()) : null);
            ps.setString(2, pd.getPerDisEstDis());
            ps.setInt(3, pd.getPerDisPerCod());
            ps.setDate(4, Date.valueOf(pd.getPerDisFecDes()));
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico: clave "perCod|yyyy-MM-dd". */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3T_PERSONAL_DISPONIBILIDAD SET PerDisEstReg=? WHERE PerDisPerCod=? AND PerDisFecDes=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(p[0]));
            ps.setDate(3, Date.valueOf(p[1]));
            ps.executeUpdate();
        }
    }
}
