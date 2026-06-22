package dao;

import model.ProyectoEstadoHistoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P2H_PROYECTO_ESTADO.
 * PK compuesta de 4 partes — clave: "cliCod|tipCod|sec|secCam".
 * HisProSecCam se calcula automáticamente como MAX()+1 por proyecto.
 * Tabla histórica: no existe UPDATE de datos, solo cambio de estado lógico.
 */
public class ProyectoEstadoHistoriaDAO extends BaseDAO<ProyectoEstadoHistoria, String> {

    @Override protected String getTableName()        { return "P2H_PROYECTO_ESTADO"; }
    @Override protected String getCodigoColumnName() { return "HisProCliCod"; }
    @Override protected String getNombreColumnName() { return "HisProSecCam"; }
    @Override protected String getEstadoColumnName() { return "HisProEstReg"; }

    @Override
    protected ProyectoEstadoHistoria mapResultSetToModel(ResultSet rs) throws SQLException {
        ProyectoEstadoHistoria h = new ProyectoEstadoHistoria();
        h.setHisProCliCod(rs.getInt("HisProCliCod"));
        h.setHisProTipCod(rs.getInt("HisProTipCod"));
        h.setHisProSec(rs.getInt("HisProSec"));
        h.setHisProSecCam(rs.getInt("HisProSecCam"));
        h.setHisProEstAnt(rs.getInt("HisProEstAnt"));
        h.setHisProEstNue(rs.getInt("HisProEstNue"));
        Date d = rs.getDate("HisProFecCam");
        h.setHisProFecCam(d != null ? d.toLocalDate() : null);
        h.setHisProPerCod(rs.getInt("HisProPerCod"));
        h.setHisProEstReg(rs.getString("HisProEstReg"));
        return h;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, ProyectoEstadoHistoria model) throws SQLException {}

    @Override
    public List<ProyectoEstadoHistoria> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P2H_PROYECTO_ESTADO ORDER BY HisProCliCod, HisProTipCod, HisProSec, HisProSecCam";
        List<ProyectoEstadoHistoria> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Clave: "cliCod|tipCod|sec|secCam" */
    @Override
    public ProyectoEstadoHistoria obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P2H_PROYECTO_ESTADO WHERE HisProCliCod=? AND HisProTipCod=? AND HisProSec=? AND HisProSecCam=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 4; i++) ps.setInt(i + 1, Integer.parseInt(p[i]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    /**
     * INSERT: HisProSecCam se calcula automáticamente como MAX()+1 por proyecto.
     * Estado lógico = 'A'.
     */
    @Override
    public void insertar(ProyectoEstadoHistoria h) throws SQLException {
        int secCam = calcularSiguienteSecCam(h.getHisProCliCod(), h.getHisProTipCod(), h.getHisProSec());
        h.setHisProSecCam(secCam);
        String sql = "INSERT INTO P2H_PROYECTO_ESTADO "
                + "(HisProCliCod, HisProTipCod, HisProSec, HisProSecCam, "
                + "HisProEstAnt, HisProEstNue, HisProFecCam, HisProPerCod, HisProEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, h.getHisProCliCod()); ps.setInt(2, h.getHisProTipCod());
            ps.setInt(3, h.getHisProSec());    ps.setInt(4, h.getHisProSecCam());
            ps.setInt(5, h.getHisProEstAnt()); ps.setInt(6, h.getHisProEstNue());
            ps.setDate(7, h.getHisProFecCam() != null ? Date.valueOf(h.getHisProFecCam()) : null);
            ps.setInt(8, h.getHisProPerCod());
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico: clave de 4 partes. */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P2H_PROYECTO_ESTADO SET HisProEstReg=? WHERE HisProCliCod=? AND HisProTipCod=? AND HisProSec=? AND HisProSecCam=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            for (int i = 0; i < 4; i++) ps.setInt(i + 2, Integer.parseInt(p[i]));
            ps.executeUpdate();
        }
    }

    /** Calcula el siguiente HisProSecCam para el grupo proyecto (3 partes). */
    public int calcularSiguienteSecCam(int cliCod, int tipCod, int sec) throws SQLException {
        String sql = "SELECT COALESCE(MAX(HisProSecCam), 0) + 1 FROM P2H_PROYECTO_ESTADO WHERE HisProCliCod=? AND HisProTipCod=? AND HisProSec=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        }
    }
}
