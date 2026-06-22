package dao;

import model.Etapa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para P3M_ETAPA.
 * PK simple (EtaCod) con ingreso manual (no auto-increment).
 * No tiene FKs adicionales. Estado lógico: EtaEstReg (A/I/*).
 */
public class EtapaDAO extends BaseDAO<Etapa, Integer> {

    @Override
    protected String getTableName() { return "P3M_ETAPA"; }

    @Override
    protected String getCodigoColumnName() { return "EtaCod"; }

    @Override
    protected String getNombreColumnName() { return "EtaNom"; }

    @Override
    protected String getEstadoColumnName() { return "EtaEstReg"; }

    @Override
    protected Etapa mapResultSetToModel(ResultSet rs) throws SQLException {
        Etapa e = new Etapa();
        e.setCodigo(rs.getInt("EtaCod"));
        e.setNombre(rs.getString("EtaNom"));
        e.setEstReg(rs.getString("EtaEstReg"));
        return e;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Etapa model) throws SQLException {
        ps.setInt(1, model.getCodigo());
        ps.setString(2, model.getNombre());
    }

    /** Listar etapas activas para ComboBoxes en otras ventanas. */
    public List<Etapa> listarActivas() throws SQLException {
        String sql = "SELECT * FROM P3M_ETAPA WHERE EtaEstReg = 'A' ORDER BY EtaCod";
        List<Etapa> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapResultSetToModel(rs));
        }
        return lista;
    }
}
