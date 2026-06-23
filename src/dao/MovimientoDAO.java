package dao;

import model.Movimiento;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P4T_MOVIMIENTO.
 * PK compuesta de 7 partes — clave: "cliCod|tipCod|sec|perCod|carCod|etaCod|etaSec".
 * MovEtaSec se calcula automáticamente en INSERT con MAX()+1 por grupo (cli+tip+sec+per+car+eta).
 */
public class MovimientoDAO extends BaseDAO<Movimiento, String> {

    @Override protected String getTableName()        { return "P4T_MOVIMIENTO"; }
    @Override protected String getCodigoColumnName() { return "MovCliCod"; }
    @Override protected String getNombreColumnName() { return "MovEtaCod"; }
    @Override protected String getEstadoColumnName() { return "MovEstReg"; }

    @Override
    protected Movimiento mapResultSetToModel(ResultSet rs) throws SQLException {
        Movimiento m = new Movimiento();
        m.setMovCliCod(rs.getInt("MovCliCod"));
        m.setMovTipProCod(rs.getInt("MovTipProCod"));
        m.setMovSecPro(rs.getInt("MovSecPro"));
        m.setMovPerCod(rs.getInt("MovPerCod"));
        m.setMovCarCod(rs.getInt("MovCarCod"));
        m.setMovEtaCod(rs.getInt("MovEtaCod"));
        m.setMovEtaSec(rs.getInt("MovEtaSec"));
        Date d = rs.getDate("MovFecRegEta");
        m.setMovFecRegEta(d != null ? d.toLocalDate() : null);
        m.setMovEtaHrsTra(rs.getInt("MovEtaHrsTra"));
        m.setMovEtaMinTra(rs.getInt("MovEtaMinTra"));
        m.setMovEstReg(rs.getString("MovEstReg"));
        return m;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Movimiento model) throws SQLException {}

    @Override
    public List<Movimiento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P4T_MOVIMIENTO "
                + "ORDER BY MovCliCod, MovTipProCod, MovSecPro, MovPerCod, MovCarCod, MovEtaCod, MovEtaSec";
        List<Movimiento> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public List<Movimiento> listarSinEliminados() throws SQLException {
        String sql = "SELECT * FROM P4T_MOVIMIENTO WHERE MovEstReg != '*' "
                + "ORDER BY MovCliCod, MovTipProCod, MovSecPro, MovPerCod, MovCarCod, MovEtaCod, MovEtaSec";
        List<Movimiento> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Clave: "cliCod|tipCod|sec|perCod|carCod|etaCod|etaSec" */
    @Override
    public Movimiento obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P4T_MOVIMIENTO "
                + "WHERE MovCliCod=? AND MovTipProCod=? AND MovSecPro=? "
                + "AND MovPerCod=? AND MovCarCod=? AND MovEtaCod=? AND MovEtaSec=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 7; i++) ps.setInt(i + 1, Integer.parseInt(p[i]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    /**
     * INSERT: MovEtaSec se calcula como MAX()+1 por grupo.
     * Estado lógico = 'A'.
     */
    @Override
    public void insertar(Movimiento m) throws SQLException {
        int etaSec = calcularSiguienteEtaSec(m.getMovCliCod(), m.getMovTipProCod(), m.getMovSecPro(),
                m.getMovPerCod(), m.getMovCarCod(), m.getMovEtaCod());
        m.setMovEtaSec(etaSec);
        String sql = "INSERT INTO P4T_MOVIMIENTO "
                + "(MovCliCod, MovTipProCod, MovSecPro, MovPerCod, MovCarCod, MovEtaCod, MovEtaSec, "
                + "MovFecRegEta, MovEtaHrsTra, MovEtaMinTra, MovEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getMovCliCod());    ps.setInt(2, m.getMovTipProCod());
            ps.setInt(3, m.getMovSecPro());    ps.setInt(4, m.getMovPerCod());
            ps.setInt(5, m.getMovCarCod());    ps.setInt(6, m.getMovEtaCod());
            ps.setInt(7, m.getMovEtaSec());
            ps.setDate(8, m.getMovFecRegEta() != null ? Date.valueOf(m.getMovFecRegEta()) : null);
            ps.setInt(9,  m.getMovEtaHrsTra());
            ps.setInt(10, m.getMovEtaMinTra());
            ps.executeUpdate();
        }
    }

    /** UPDATE: modifica fecha, horas y minutos trabajados. */
    public void actualizar(Movimiento m) throws SQLException {
        String sql = "UPDATE P4T_MOVIMIENTO SET MovFecRegEta=?, MovEtaHrsTra=?, MovEtaMinTra=? "
                + "WHERE MovCliCod=? AND MovTipProCod=? AND MovSecPro=? "
                + "AND MovPerCod=? AND MovCarCod=? AND MovEtaCod=? AND MovEtaSec=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, m.getMovFecRegEta() != null ? Date.valueOf(m.getMovFecRegEta()) : null);
            ps.setInt(2,  m.getMovEtaHrsTra());
            ps.setInt(3,  m.getMovEtaMinTra());
            ps.setInt(4,  m.getMovCliCod());    ps.setInt(5, m.getMovTipProCod());
            ps.setInt(6,  m.getMovSecPro());    ps.setInt(7, m.getMovPerCod());
            ps.setInt(8,  m.getMovCarCod());    ps.setInt(9, m.getMovEtaCod());
            ps.setInt(10, m.getMovEtaSec());
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico: clave de 7 partes. */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P4T_MOVIMIENTO SET MovEstReg=? "
                + "WHERE MovCliCod=? AND MovTipProCod=? AND MovSecPro=? "
                + "AND MovPerCod=? AND MovCarCod=? AND MovEtaCod=? AND MovEtaSec=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            for (int i = 0; i < 7; i++) ps.setInt(i + 2, Integer.parseInt(p[i]));
            ps.executeUpdate();
        }
    }

    /** Calcula el siguiente MovEtaSec para el grupo de 6 dimensiones. */
    public int calcularSiguienteEtaSec(int cli, int tip, int sec, int per, int car, int eta) throws SQLException {
        String sql = "SELECT COALESCE(MAX(MovEtaSec), 0) + 1 FROM P4T_MOVIMIENTO "
                + "WHERE MovCliCod=? AND MovTipProCod=? AND MovSecPro=? AND MovPerCod=? AND MovCarCod=? AND MovEtaCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cli); ps.setInt(2, tip); ps.setInt(3, sec);
            ps.setInt(4, per); ps.setInt(5, car); ps.setInt(6, eta);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        }
    }
}
