package dao;

import model.Actividad;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3M_ACTIVIDAD.
 * PK compuesta (ActEtaCod, ActCod) — gestionada como String "etaCod|actCod".
 * FK: ActEtaCod → P3M_ETAPA.
 */
public class ActividadDAO extends BaseDAO<Actividad, String> {

    @Override
    protected String getTableName() { return "P3M_ACTIVIDAD"; }

    @Override
    protected String getCodigoColumnName() { return "ActEtaCod"; }

    @Override
    protected String getNombreColumnName() { return "ActNom"; }

    @Override
    protected String getEstadoColumnName() { return "ActEstReg"; }

    @Override
    protected Actividad mapResultSetToModel(ResultSet rs) throws SQLException {
        Actividad a = new Actividad();
        a.setActEtaCod(rs.getInt("ActEtaCod"));
        a.setActCod(rs.getInt("ActCod"));
        a.setActNom(rs.getString("ActNom"));
        a.setActTpoEst(rs.getBigDecimal("ActTpoEst"));
        a.setActEstReg(rs.getString("ActEstReg"));
        return a;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Actividad model) throws SQLException {
        // Delegado a insertar()
    }

    @Override
    public List<Actividad> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3M_ACTIVIDAD ORDER BY ActEtaCod, ActCod";
        List<Actividad> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Obtener por PK compuesta "etaCod|actCod". */
    @Override
    public Actividad obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3M_ACTIVIDAD WHERE ActEtaCod=? AND ActCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(p[0])); ps.setInt(2, Integer.parseInt(p[1]));
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapResultSetToModel(rs) : null; }
        }
    }

    /** INSERT con estado lógico 'A'. */
    @Override
    public void insertar(Actividad a) throws SQLException {
        String sql = "INSERT INTO P3M_ACTIVIDAD (ActEtaCod, ActCod, ActNom, ActTpoEst, ActEstReg) "
                   + "VALUES (?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getActEtaCod()); ps.setInt(2, a.getActCod());
            ps.setString(3, a.getActNom()); ps.setBigDecimal(4, a.getActTpoEst());
            ps.executeUpdate();
        }
    }

    /** UPDATE de nombre y tiempo estimado. */
    public void actualizar(Actividad a) throws SQLException {
        String sql = "UPDATE P3M_ACTIVIDAD SET ActEtaCod=?, ActNom=?, ActTpoEst=? WHERE ActEtaCod=? AND ActCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getActEtaCod()); ps.setString(2, a.getActNom());
            ps.setBigDecimal(3, a.getActTpoEst());
            ps.setInt(4, a.getActEtaCod()); ps.setInt(5, a.getActCod());
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico usando PK compuesta. */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3M_ACTIVIDAD SET ActEstReg=? WHERE ActEtaCod=? AND ActCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(p[0])); ps.setInt(3, Integer.parseInt(p[1]));
            ps.executeUpdate();
        }
    }

    /** Listar actividades activas para ComboBoxes (filtradas por etapa si se desea). */
    public List<Actividad> listarActivas() throws SQLException {
        String sql = "SELECT * FROM P3M_ACTIVIDAD WHERE ActEstReg='A' ORDER BY ActEtaCod, ActCod";
        List<Actividad> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }
}
