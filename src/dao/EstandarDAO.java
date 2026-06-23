package dao;

import model.Estandar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3M_ESTANDAR.
 * PK compuesta (EstActEtaCod, EstActActCod, EstCod) — clave: "etaCod|actCod|estCod".
 * FK: (EstActEtaCod, EstActActCod) → P3M_ACTIVIDAD
 *     EstTipCod → GZZ_TIPO_ESTANDAR
 * CHECK: EstValNum y EstValTxt son mutuamente excluyentes (exactamente uno debe ser NOT NULL).
 */
public class EstandarDAO extends BaseDAO<Estandar, String> {

    @Override
    protected String getTableName() { return "P3M_ESTANDAR"; }

    @Override
    protected String getCodigoColumnName() { return "EstActEtaCod"; }

    @Override
    protected String getNombreColumnName() { return "EstNom"; }

    @Override
    protected String getEstadoColumnName() { return "EstEstReg"; }

    @Override
    protected Estandar mapResultSetToModel(ResultSet rs) throws SQLException {
        Estandar e = new Estandar();
        e.setEstActEtaCod(rs.getInt("EstActEtaCod"));
        e.setEstActActCod(rs.getInt("EstActActCod"));
        e.setEstCod(rs.getInt("EstCod"));
        e.setEstTipCod(rs.getInt("EstTipCod"));
        e.setEstNom(rs.getString("EstNom"));
        e.setEstValNum(rs.getBigDecimal("EstValNum"));
        e.setEstValTxt(rs.getString("EstValTxt"));
        e.setEstUni(rs.getString("EstUni"));
        e.setEstEstReg(rs.getString("EstEstReg"));
        return e;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Estandar model) throws SQLException {
        // Delegado a insertar()
    }

    @Override
    public List<Estandar> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P3M_ESTANDAR ORDER BY EstActEtaCod, EstActActCod, EstCod";
        List<Estandar> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    @Override
    public List<Estandar> listarSinEliminados() throws SQLException {
        String sql = "SELECT * FROM P3M_ESTANDAR WHERE EstEstReg != '*' ORDER BY EstActEtaCod, EstActActCod, EstCod";
        List<Estandar> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Obtener por PK compuesta "etaCod|actCod|estCod". */
    @Override
    public Estandar obtenerPorId(String clave) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "SELECT * FROM P3M_ESTANDAR WHERE EstActEtaCod=? AND EstActActCod=? AND EstCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(p[0]));
            ps.setInt(2, Integer.parseInt(p[1]));
            ps.setInt(3, Integer.parseInt(p[2]));
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapResultSetToModel(rs) : null; }
        }
    }

    /** INSERT. Estado lógico = 'A'. CHECK de valores: exactamente uno NOT NULL. */
    @Override
    public void insertar(Estandar e) throws SQLException {
        String sql = "INSERT INTO P3M_ESTANDAR "
                + "(EstActEtaCod, EstActActCod, EstCod, EstTipCod, EstNom, EstValNum, EstValTxt, EstUni, EstEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getEstActEtaCod());
            ps.setInt(2, e.getEstActActCod());
            ps.setInt(3, e.getEstCod());
            ps.setInt(4, e.getEstTipCod());
            ps.setString(5, e.getEstNom());
            ps.setBigDecimal(6, e.getEstValNum());   // null si es texto
            ps.setString(7, e.getEstValTxt());        // null si es numérico
            ps.setString(8, e.getEstUni());
            ps.executeUpdate();
        }
    }

    /** UPDATE de campos editables. */
    public void actualizar(Estandar e) throws SQLException {
        String sql = "UPDATE P3M_ESTANDAR SET EstTipCod=?, EstNom=?, EstValNum=?, EstValTxt=?, EstUni=? "
                + "WHERE EstActEtaCod=? AND EstActActCod=? AND EstCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getEstTipCod());
            ps.setString(2, e.getEstNom());
            ps.setBigDecimal(3, e.getEstValNum());
            ps.setString(4, e.getEstValTxt());
            ps.setString(5, e.getEstUni());
            ps.setInt(6, e.getEstActEtaCod());
            ps.setInt(7, e.getEstActActCod());
            ps.setInt(8, e.getEstCod());
            ps.executeUpdate();
        }
    }

    /** Cambiar estado lógico por PK compuesta "etaCod|actCod|estCod". */
    @Override
    public void cambiarEstado(String clave, String nuevoEstado) throws SQLException {
        String[] p = clave.split("\\|");
        String sql = "UPDATE P3M_ESTANDAR SET EstEstReg=? WHERE EstActEtaCod=? AND EstActActCod=? AND EstCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(p[0]));
            ps.setInt(3, Integer.parseInt(p[1]));
            ps.setInt(4, Integer.parseInt(p[2]));
            ps.executeUpdate();
        }
    }
}
