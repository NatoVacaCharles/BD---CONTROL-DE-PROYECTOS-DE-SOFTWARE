package dao;

import model.ProyectoEtapa;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3T_PROYECTO_ETAPA.
 * PK compuesta de 4 partes — clave: "cliCod|tipCod|sec|etaCod".
 */
public class ProyectoEtapaDAO extends BaseDAO<ProyectoEtapa, String> {

    @Override protected String getTableName() { return "P3T_PROYECTO_ETAPA"; }
    @Override protected String getCodigoColumnName() { return "ProEtaCliCod"; }
    @Override protected String getNombreColumnName() { return "ProEtaCod"; }
    @Override protected String getEstadoColumnName() { return "ProEtaEstReg"; }

    @Override
    protected ProyectoEtapa mapResultSetToModel(ResultSet rs) throws SQLException {
        ProyectoEtapa pe = new ProyectoEtapa();
        pe.setProEtaCliCod(rs.getInt("ProEtaCliCod"));
        pe.setProEtaTipCod(rs.getInt("ProEtaTipCod"));
        pe.setProEtaSec(rs.getInt("ProEtaSec"));
        pe.setProEtaCod(rs.getInt("ProEtaCod"));
        pe.setProEtaTpoEstAju(rs.getBigDecimal("ProEtaTpoEstAju"));
        Date fi = rs.getDate("ProEtaFecIni"); pe.setProEtaFecIni(fi != null ? fi.toLocalDate() : null);
        Date ff = rs.getDate("ProEtaFecFin"); pe.setProEtaFecFin(ff != null ? ff.toLocalDate() : null);
        pe.setProEtaEstReg(rs.getString("ProEtaEstReg"));
        return pe;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, ProyectoEtapa model) throws SQLException {}

    @Override
    public List<ProyectoEtapa> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3T_PROYECTO_ETAPA ORDER BY ProEtaCliCod, ProEtaTipCod, ProEtaSec, ProEtaCod";
        List<ProyectoEtapa> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public List<ProyectoEtapa> listarSinEliminados() throws SQLException {
        String sql = "SELECT * FROM P3T_PROYECTO_ETAPA WHERE ProEtaEstReg != '*' ORDER BY ProEtaCliCod, ProEtaTipCod, ProEtaSec, ProEtaCod";
        List<ProyectoEtapa> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public ProyectoEtapa obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3T_PROYECTO_ETAPA WHERE ProEtaCliCod=? AND ProEtaTipCod=? AND ProEtaSec=? AND ProEtaCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 4; i++) ps.setInt(i + 1, Integer.parseInt(p[i]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    @Override
    public void insertar(ProyectoEtapa pe) throws SQLException {
        String sql = "INSERT INTO P3T_PROYECTO_ETAPA "
                + "(ProEtaCliCod, ProEtaTipCod, ProEtaSec, ProEtaCod, ProEtaTpoEstAju, ProEtaFecIni, ProEtaFecFin, ProEtaEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pe.getProEtaCliCod()); ps.setInt(2, pe.getProEtaTipCod());
            ps.setInt(3, pe.getProEtaSec());    ps.setInt(4, pe.getProEtaCod());
            ps.setBigDecimal(5, pe.getProEtaTpoEstAju());
            ps.setDate(6, toDate(pe.getProEtaFecIni()));
            ps.setDate(7, toDate(pe.getProEtaFecFin()));
            ps.executeUpdate();
        }
    }

    public void actualizar(ProyectoEtapa pe) throws SQLException {
        String sql = "UPDATE P3T_PROYECTO_ETAPA SET ProEtaTpoEstAju=?, ProEtaFecIni=?, ProEtaFecFin=? "
                + "WHERE ProEtaCliCod=? AND ProEtaTipCod=? AND ProEtaSec=? AND ProEtaCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, pe.getProEtaTpoEstAju());
            ps.setDate(2, toDate(pe.getProEtaFecIni()));
            ps.setDate(3, toDate(pe.getProEtaFecFin()));
            ps.setInt(4, pe.getProEtaCliCod()); ps.setInt(5, pe.getProEtaTipCod());
            ps.setInt(6, pe.getProEtaSec());    ps.setInt(7, pe.getProEtaCod());
            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3T_PROYECTO_ETAPA SET ProEtaEstReg=? WHERE ProEtaCliCod=? AND ProEtaTipCod=? AND ProEtaSec=? AND ProEtaCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            for (int i = 0; i < 4; i++) ps.setInt(i + 2, Integer.parseInt(p[i]));
            ps.executeUpdate();
        }
    }

    /** Listar etapas activas de un proyecto para ComboBoxes en P4T_MOVIMIENTO. */
    public List<ProyectoEtapa> listarActivasPorProyecto(int cliCod, int tipCod, int sec) throws SQLException {
        String sql = "SELECT * FROM P3T_PROYECTO_ETAPA WHERE ProEtaCliCod=? AND ProEtaTipCod=? AND ProEtaSec=? AND ProEtaEstReg='A'";
        List<ProyectoEtapa> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSetToModel(rs));
            }
        }
        return lista;
    }

    private Date toDate(LocalDate ld) { return ld != null ? Date.valueOf(ld) : null; }
}
