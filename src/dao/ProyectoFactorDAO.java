package dao;

import model.ProyectoFactor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P2T_PROYECTO_FACTOR.
 * PK compuesta de 4 partes — clave: "cliCod|tipCod|sec|facCod".
 */
public class ProyectoFactorDAO extends BaseDAO<ProyectoFactor, String> {

    @Override protected String getTableName() { return "P2T_PROYECTO_FACTOR"; }
    @Override protected String getCodigoColumnName() { return "ProFacCliCod"; }
    @Override protected String getNombreColumnName() { return "ProFacCod"; }
    @Override protected String getEstadoColumnName() { return "ProFacEstReg"; }

    @Override
    protected ProyectoFactor mapResultSetToModel(ResultSet rs) throws SQLException {
        ProyectoFactor pf = new ProyectoFactor();
        pf.setProFacCliCod(rs.getInt("ProFacCliCod"));
        pf.setProFacTipCod(rs.getInt("ProFacTipCod"));
        pf.setProFacSec(rs.getInt("ProFacSec"));
        pf.setProFacCod(rs.getInt("ProFacCod"));
        pf.setProFacPorApl(rs.getBigDecimal("ProFacPorApl"));
        pf.setProFacEstReg(rs.getString("ProFacEstReg"));
        return pf;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, ProyectoFactor model) throws SQLException {}

    @Override
    public List<ProyectoFactor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P2T_PROYECTO_FACTOR ORDER BY ProFacCliCod, ProFacTipCod, ProFacSec, ProFacCod";
        List<ProyectoFactor> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public List<ProyectoFactor> listarSinEliminados() throws SQLException {
        String sql = "SELECT * FROM P2T_PROYECTO_FACTOR WHERE ProFacEstReg != '*' ORDER BY ProFacCliCod, ProFacTipCod, ProFacSec, ProFacCod";
        List<ProyectoFactor> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public ProyectoFactor obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P2T_PROYECTO_FACTOR WHERE ProFacCliCod=? AND ProFacTipCod=? AND ProFacSec=? AND ProFacCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 4; i++) ps.setInt(i + 1, Integer.parseInt(p[i]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    @Override
    public void insertar(ProyectoFactor pf) throws SQLException {
        String sql = "INSERT INTO P2T_PROYECTO_FACTOR (ProFacCliCod, ProFacTipCod, ProFacSec, ProFacCod, ProFacPorApl, ProFacEstReg) VALUES (?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pf.getProFacCliCod()); ps.setInt(2, pf.getProFacTipCod());
            ps.setInt(3, pf.getProFacSec());    ps.setInt(4, pf.getProFacCod());
            ps.setBigDecimal(5, pf.getProFacPorApl());
            ps.executeUpdate();
        }
    }

    /** UPDATE: solo actualiza el porcentaje de aplicación. */
    public void actualizar(ProyectoFactor pf) throws SQLException {
        String sql = "UPDATE P2T_PROYECTO_FACTOR SET ProFacPorApl=? WHERE ProFacCliCod=? AND ProFacTipCod=? AND ProFacSec=? AND ProFacCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, pf.getProFacPorApl());
            ps.setInt(2, pf.getProFacCliCod()); ps.setInt(3, pf.getProFacTipCod());
            ps.setInt(4, pf.getProFacSec());    ps.setInt(5, pf.getProFacCod());
            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P2T_PROYECTO_FACTOR SET ProFacEstReg=? WHERE ProFacCliCod=? AND ProFacTipCod=? AND ProFacSec=? AND ProFacCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            for (int i = 0; i < 4; i++) ps.setInt(i + 2, Integer.parseInt(p[i]));
            ps.executeUpdate();
        }
    }
}
