package dao;

import model.EquipoProyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3T_EQUIPO_PROYECTO.
 * PK compuesta de 5 partes — clave string: "cliCod|tipCod|sec|perCod|carCod".
 */
public class EquipoProyectoDAO extends BaseDAO<EquipoProyecto, String> {

    @Override
    protected String getTableName() { return "P3T_EQUIPO_PROYECTO"; }

    @Override
    protected String getCodigoColumnName() { return "EqpProCliCod"; }

    @Override
    protected String getNombreColumnName() { return "EqpPerCod"; }

    @Override
    protected String getEstadoColumnName() { return "EqpEstReg"; }

    @Override
    protected EquipoProyecto mapResultSetToModel(ResultSet rs) throws SQLException {
        EquipoProyecto e = new EquipoProyecto();
        e.setEqpProCliCod(rs.getInt("EqpProCliCod"));
        e.setEqpProTipCod(rs.getInt("EqpProTipCod"));
        e.setEqpProSec(rs.getInt("EqpProSec"));
        e.setEqpPerCod(rs.getInt("EqpPerCod"));
        e.setEqpCarCod(rs.getInt("EqpCarCod"));
        e.setEqpEstReg(rs.getString("EqpEstReg"));
        return e;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, EquipoProyecto model) throws SQLException {}

    @Override
    public List<EquipoProyecto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3T_EQUIPO_PROYECTO ORDER BY EqpProCliCod, EqpProTipCod, EqpProSec, EqpPerCod, EqpCarCod";
        List<EquipoProyecto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public List<EquipoProyecto> listarSinEliminados() throws SQLException {
        String sql = "SELECT * FROM P3T_EQUIPO_PROYECTO WHERE EqpEstReg != '*' ORDER BY EqpProCliCod, EqpProTipCod, EqpProSec, EqpPerCod, EqpCarCod";
        List<EquipoProyecto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Clave: "cliCod|tipCod|sec|perCod|carCod" */
    @Override
    public EquipoProyecto obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3T_EQUIPO_PROYECTO WHERE EqpProCliCod=? AND EqpProTipCod=? AND EqpProSec=? AND EqpPerCod=? AND EqpCarCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < 5; i++) ps.setInt(i + 1, Integer.parseInt(p[i]));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    @Override
    public void insertar(EquipoProyecto m) throws SQLException {
        String sql = "INSERT INTO P3T_EQUIPO_PROYECTO (EqpProCliCod, EqpProTipCod, EqpProSec, EqpPerCod, EqpCarCod, EqpEstReg) VALUES (?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getEqpProCliCod()); ps.setInt(2, m.getEqpProTipCod());
            ps.setInt(3, m.getEqpProSec());    ps.setInt(4, m.getEqpPerCod());
            ps.setInt(5, m.getEqpCarCod());
            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3T_EQUIPO_PROYECTO SET EqpEstReg=? WHERE EqpProCliCod=? AND EqpProTipCod=? AND EqpProSec=? AND EqpPerCod=? AND EqpCarCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            for (int i = 0; i < 5; i++) ps.setInt(i + 2, Integer.parseInt(p[i]));
            ps.executeUpdate();
        }
    }

    /** Listar miembros activos de un proyecto para ComboBoxes en P4T_MOVIMIENTO. */
    public List<EquipoProyecto> listarActivosPorProyecto(int cliCod, int tipCod, int sec) throws SQLException {
        String sql = "SELECT * FROM P3T_EQUIPO_PROYECTO WHERE EqpProCliCod=? AND EqpProTipCod=? AND EqpProSec=? AND EqpEstReg='A'";
        List<EquipoProyecto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSetToModel(rs));
            }
        }
        return lista;
    }
}
