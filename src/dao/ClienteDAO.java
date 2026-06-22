package dao;

import model.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla P1M_CLIENTE.
 * Extiende BaseDAO con lógica personalizada ya que:
 *  - La PK es AUTO_INCREMENT (no se envía en INSERT)
 *  - Tiene múltiples FKs y campos de fecha
 *  - El UPDATE modifica varios campos (no solo nombre)
 *  - Gestión lógica de estado (A, I, *)
 */
public class ClienteDAO extends BaseDAO<Cliente, Integer> {

    @Override
    protected String getTableName() { return "P1M_CLIENTE"; }

    @Override
    protected String getCodigoColumnName() { return "CliCod"; }

    @Override
    protected String getNombreColumnName() { return "CliNom"; }

    @Override
    protected String getEstadoColumnName() { return "CliEstReg"; }

    @Override
    protected Cliente mapResultSetToModel(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodigo(rs.getInt("CliCod"));
        c.setTipClienteCod(rs.getInt("CliTipCod"));
        c.setNombre(rs.getString("CliNom"));
        // Leer fechas con soporte nullable
        Date fecIng = rs.getDate("CliFecIng");
        c.setFechaIngreso(fecIng != null ? fecIng.toLocalDate() : null);
        Date fecCes = rs.getDate("CliFecCes");
        c.setFechaCese(fecCes != null ? fecCes.toLocalDate() : null);
        Date fecUlt = rs.getDate("CliFecUltProCer");
        c.setFechaUltProCer(fecUlt != null ? fecUlt.toLocalDate() : null);
        c.setEstCli(rs.getString("CliEstCli"));
        c.setEstReg(rs.getString("CliEstReg"));
        return c;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Cliente model) throws SQLException {
        // No se usa: INSERT es completamente personalizado abajo
    }

    /**
     * INSERT: CliCod es AUTO_INCREMENT, no se envía.
     * Se inserta con CliEstReg = 'A' por defecto.
     */
    @Override
    public void insertar(Cliente model) throws SQLException {
        String sql = "INSERT INTO P1M_CLIENTE "
                + "(CliTipCod, CliNom, CliFecIng, CliFecCes, CliFecUltProCer, CliEstCli, CliEstReg) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'A')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, model.getTipClienteCod());
            ps.setString(2, model.getNombre());
            ps.setDate(3, model.getFechaIngreso() != null ? Date.valueOf(model.getFechaIngreso()) : null);
            ps.setDate(4, model.getFechaCese() != null ? Date.valueOf(model.getFechaCese()) : null);
            ps.setDate(5, model.getFechaUltProCer() != null ? Date.valueOf(model.getFechaUltProCer()) : null);
            ps.setString(6, model.getEstCli());
            ps.executeUpdate();
        }
    }

    /**
     * UPDATE: actualiza todos los campos editables del cliente.
     */
    public void actualizar(Cliente model) throws SQLException {
        String sql = "UPDATE P1M_CLIENTE SET "
                + "CliTipCod = ?, CliNom = ?, CliFecIng = ?, CliFecCes = ?, "
                + "CliFecUltProCer = ?, CliEstCli = ? "
                + "WHERE CliCod = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, model.getTipClienteCod());
            ps.setString(2, model.getNombre());
            ps.setDate(3, model.getFechaIngreso() != null ? Date.valueOf(model.getFechaIngreso()) : null);
            ps.setDate(4, model.getFechaCese() != null ? Date.valueOf(model.getFechaCese()) : null);
            ps.setDate(5, model.getFechaUltProCer() != null ? Date.valueOf(model.getFechaUltProCer()) : null);
            ps.setString(6, model.getEstCli());
            ps.setInt(7, model.getCodigo());
            ps.executeUpdate();
        }
    }

    /**
     * Listar solo los activos para ComboBoxes en otras ventanas.
     */
    public List<Cliente> listarActivos() throws SQLException {
        String sql = "SELECT * FROM P1M_CLIENTE WHERE CliEstReg = 'A' ORDER BY CliNom";
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }
}
