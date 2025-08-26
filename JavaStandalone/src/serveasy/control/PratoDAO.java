package serveasy.control;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Prato;
import serveasy.view.TelaErro;

public class PratoDAO {

    @FunctionalInterface
    private interface PreparedStatementSetter {

        void setParameters(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    private interface ResultSetHandler<T> {

        T handle(ResultSet rs) throws SQLException;
    }

    private void executarUpdate(String sql, PreparedStatementSetter pss) throws SQLException {
        DbConnection dbConnection = new DbConnection();
        try (Connection conexao = dbConnection.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            pss.setParameters(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
            throw ex;
        }
    }

    private <T> T executarQueryUnica(String sql, PreparedStatementSetter pss, ResultSetHandler<T> rsh) throws SQLException {
        DbConnection dbConnection = new DbConnection();
        try (Connection conexao = dbConnection.getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            if (pss != null) {
                pss.setParameters(stmt);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return rsh.handle(rs);
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
            throw ex;
        }
    }

    public void addPrato(Prato prato) throws SQLException {
        String sql = "INSERT INTO prato (nome, preco, descricao) VALUES (?, ?, ?)";
        executarUpdate(sql, stmt -> {
            stmt.setString(1, prato.getNome());
            stmt.setFloat(2, prato.getPreco());
            stmt.setString(3, prato.getDescricao());
        });
    }

    public void delPrato(int id) throws SQLException {
        String sql = "DELETE FROM prato WHERE id = ?";
        executarUpdate(sql, stmt -> stmt.setInt(1, id));
    }

    public Prato getPrato(int id) throws SQLException {
        String sql = "SELECT * FROM prato WHERE id = ?";
        return executarQueryUnica(sql, stmt -> stmt.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Prato(
                        id,
                        rs.getString("nome"),
                        rs.getFloat("preco"),
                        rs.getString("descricao")
                );
            }
            return null;
        });
    }

    public List<Prato> listarPratos() throws SQLException {
        String sql = "SELECT * FROM prato";
        return executarQueryUnica(sql, null, rs -> {
            List<Prato> lista = new ArrayList<>();
            while (rs.next()) {
                Prato prato = new Prato(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getFloat("preco"),
                        rs.getString("descricao")
                );
                lista.add(prato);
            }
            return lista;
        });
    }

    public String getNomePrato(int id) throws SQLException {
        String sql = "SELECT nome FROM prato WHERE id = ?";
        return executarQueryUnica(sql, stmt -> stmt.setInt(1, id), rs -> rs.next() ? rs.getString("nome") : null);
    }

    public float getPrecoPrato(int id) throws SQLException {
        String sql = "SELECT preco FROM prato WHERE id = ?";
        return executarQueryUnica(sql, stmt -> stmt.setInt(1, id), rs -> rs.next() ? rs.getFloat("preco") : 0.0f);
    }
}
