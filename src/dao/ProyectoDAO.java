package dao;

import model.Proyecto;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P2M_PROYECTO.
 * PK compuesta (ProCliCod, ProTipProCod, ProSecPro) — se gestiona manualmente.
 * No extiende BaseDAO directamente porque la PK no es simple; implementa las
 * operaciones mínimas requeridas por el Frame.
 *
 * Para satisfacer MantenimientoBaseFrame<Proyecto, String> se usa String como
 * clave compuesta con formato "cliCod|tipCod|sec".
 */
public class ProyectoDAO extends BaseDAO<Proyecto, String> {

    @Override
    protected String getTableName() { return "P2M_PROYECTO"; }

    @Override
    protected String getCodigoColumnName() { return "ProCliCod"; }  // PK principal

    @Override
    protected String getNombreColumnName() { return "ProFecCon"; }  // no aplica directo

    @Override
    protected String getEstadoColumnName() { return "ProEstReg"; }

    // -------------------------------------------------------------------------
    // Mapear ResultSet → Proyecto
    // -------------------------------------------------------------------------
    @Override
    protected Proyecto mapResultSetToModel(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setProCliCod(rs.getInt("ProCliCod"));
        p.setProTipProCod(rs.getInt("ProTipProCod"));
        p.setProSecPro(rs.getInt("ProSecPro"));
        Date d;
        d = rs.getDate("ProFecCon"); p.setProFecCon(d != null ? d.toLocalDate() : null);
        d = rs.getDate("ProFecPac"); p.setProFecPac(d != null ? d.toLocalDate() : null);
        d = rs.getDate("ProFecIni"); p.setProFecIni(d != null ? d.toLocalDate() : null);
        d = rs.getDate("ProFecEnt"); p.setProFecEnt(d != null ? d.toLocalDate() : null);
        d = rs.getDate("ProFecCie"); p.setProFecCie(d != null ? d.toLocalDate() : null);
        p.setProMonProCos(rs.getBigDecimal("ProMonProCos"));
        p.setProMonProGas(rs.getBigDecimal("ProMonProGas"));
        p.setProMonProUti(rs.getBigDecimal("ProMonProUti"));
        p.setProMonPro(rs.getBigDecimal("ProMonPro"));
        p.setProMonProCosRea(rs.getBigDecimal("ProMonProCosRea"));
        p.setProMonProGasRea(rs.getBigDecimal("ProMonProGasRea"));
        p.setProMonProUtiRea(rs.getBigDecimal("ProMonProUtiRea"));
        p.setProMonProRea(rs.getBigDecimal("ProMonProRea"));
        p.setProEstPro(rs.getInt("ProEstPro"));
        p.setProEstReg(rs.getString("ProEstReg"));
        return p;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Proyecto model) throws SQLException {
        // Implementado en insertar() directamente
    }

    // -------------------------------------------------------------------------
    // CRUD personalizado por PK compuesta
    // -------------------------------------------------------------------------
    /** Listar todos los proyectos. */
    @Override
    public List<Proyecto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM P2M_PROYECTO ORDER BY ProCliCod, ProTipProCod, ProSecPro";
        List<Proyecto> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }

    /** Obtener por PK compuesta (clave: "cliCod|tipCod|sec"). */
    @Override
    public Proyecto obtenerPorId(String claveCompuesta) throws SQLException {
        String[] partes = claveCompuesta.split("\\|");
        int cliCod = Integer.parseInt(partes[0]);
        int tipCod = Integer.parseInt(partes[1]);
        int sec    = Integer.parseInt(partes[2]);
        String sql = "SELECT * FROM P2M_PROYECTO WHERE ProCliCod=? AND ProTipProCod=? AND ProSecPro=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToModel(rs) : null;
            }
        }
    }

    /** INSERT completo para P2M_PROYECTO. Estado lógico = 'A'. */
    @Override
    public void insertar(Proyecto p) throws SQLException {
        String sql = "INSERT INTO P2M_PROYECTO "
                + "(ProCliCod, ProTipProCod, ProSecPro, ProFecCon, ProFecPac, "
                + "ProFecIni, ProFecEnt, ProFecCie, "
                + "ProMonProCos, ProMonProGas, ProMonProUti, ProMonPro, "
                + "ProMonProCosRea, ProMonProGasRea, ProMonProUtiRea, ProMonProRea, "
                + "ProEstPro, ProEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setAllParameters(ps, p);
            ps.executeUpdate();
        }
    }

    /** UPDATE de todos los campos editables de un proyecto. */
    public void actualizar(Proyecto p) throws SQLException {
        String sql = "UPDATE P2M_PROYECTO SET "
                + "ProFecCon=?, ProFecPac=?, ProFecIni=?, ProFecEnt=?, ProFecCie=?, "
                + "ProMonProCos=?, ProMonProGas=?, ProMonProUti=?, ProMonPro=?, "
                + "ProMonProCosRea=?, ProMonProGasRea=?, ProMonProUtiRea=?, ProMonProRea=?, "
                + "ProEstPro=? "
                + "WHERE ProCliCod=? AND ProTipProCod=? AND ProSecPro=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            ps.setDate(i++, toDate(p.getProFecCon()));
            ps.setDate(i++, toDate(p.getProFecPac()));
            ps.setDate(i++, toDate(p.getProFecIni()));
            ps.setDate(i++, toDate(p.getProFecEnt()));
            ps.setDate(i++, toDate(p.getProFecCie()));
            ps.setBigDecimal(i++, p.getProMonProCos());
            ps.setBigDecimal(i++, p.getProMonProGas());
            ps.setBigDecimal(i++, p.getProMonProUti());
            ps.setBigDecimal(i++, p.getProMonPro());
            ps.setBigDecimal(i++, p.getProMonProCosRea());
            ps.setBigDecimal(i++, p.getProMonProGasRea());
            ps.setBigDecimal(i++, p.getProMonProUtiRea());
            ps.setBigDecimal(i++, p.getProMonProRea());
            ps.setInt(i++, p.getProEstPro());
            // WHERE
            ps.setInt(i++, p.getProCliCod());
            ps.setInt(i++, p.getProTipProCod());
            ps.setInt(i,   p.getProSecPro());
            ps.executeUpdate();
        }
    }

    /** Cambiar el estado lógico usando PK compuesta "cliCod|tipCod|sec". */
    @Override
    public void cambiarEstado(String claveCompuesta, String nuevoEstado) throws SQLException {
        String[] partes = claveCompuesta.split("\\|");
        String sql = "UPDATE P2M_PROYECTO SET ProEstReg=? WHERE ProCliCod=? AND ProTipProCod=? AND ProSecPro=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(partes[0]));
            ps.setInt(3, Integer.parseInt(partes[1]));
            ps.setInt(4, Integer.parseInt(partes[2]));
            ps.executeUpdate();
        }
    }

    /** Calcular el siguiente ProSecPro para un cliente+tipo dado. */
    public int calcularSiguienteSecuencia(int cliCod, int tipCod) throws SQLException {
        String sql = "SELECT COALESCE(MAX(ProSecPro), 0) + 1 FROM P2M_PROYECTO WHERE ProCliCod=? AND ProTipProCod=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cliCod); ps.setInt(2, tipCod);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 1;
            }
        }
    }

    // --- Helpers ---
    private void setAllParameters(PreparedStatement ps, Proyecto p) throws SQLException {
        int i = 1;
        ps.setInt(i++, p.getProCliCod());
        ps.setInt(i++, p.getProTipProCod());
        ps.setInt(i++, p.getProSecPro());
        ps.setDate(i++, toDate(p.getProFecCon()));
        ps.setDate(i++, toDate(p.getProFecPac()));
        ps.setDate(i++, toDate(p.getProFecIni()));
        ps.setDate(i++, toDate(p.getProFecEnt()));
        ps.setDate(i++, toDate(p.getProFecCie()));
        ps.setBigDecimal(i++, p.getProMonProCos());
        ps.setBigDecimal(i++, p.getProMonProGas());
        ps.setBigDecimal(i++, p.getProMonProUti());
        ps.setBigDecimal(i++, p.getProMonPro());
        ps.setBigDecimal(i++, p.getProMonProCosRea());
        ps.setBigDecimal(i++, p.getProMonProGasRea());
        ps.setBigDecimal(i++, p.getProMonProUtiRea());
        ps.setBigDecimal(i++, p.getProMonProRea());
        ps.setInt(i,   p.getProEstPro()); // parámetro 17
    }

    private Date toDate(LocalDate ld) {
        return ld != null ? Date.valueOf(ld) : null;
    }
}
