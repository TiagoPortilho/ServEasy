package serveasy.control;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Pedido;
import serveasy.view.TelaErro;

public class PedidoDAO {

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

    private <T> T executarQueryLista(String sql, PreparedStatementSetter pss, ResultSetHandler<T> rsh) throws SQLException {
        return executarQueryUnica(sql, pss, rsh);
    }

    public void addPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedido (id_prato, id_mesa, confirmado) VALUES (?, ?, ?)";
        executarUpdate(sql, stmt -> {
            stmt.setInt(1, pedido.getIdPrato());
            stmt.setInt(2, pedido.getIdMesa());
            stmt.setBoolean(3, pedido.isConfirmado());
        });
    }

    public void delPedido(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        executarUpdate(sql, stmt -> stmt.setInt(1, id));
    }

    public Pedido getPedido(int id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        return executarQueryUnica(sql, stmt -> stmt.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Pedido(
                        id,
                        rs.getInt("id_prato"),
                        rs.getInt("id_mesa"),
                        rs.getBoolean("confirmado"),
                        rs.getBoolean("entregue")
                );
            }
            return null;
        });
    }

    public List<Pedido> listarPedidos() throws SQLException {
        String sql = "SELECT p.id AS id_pedido, m.numero AS numero_mesa, pr.nome AS nome_prato, p.confirmado, p.entregue "
                + "FROM pedido p "
                + "JOIN mesa m ON p.id_mesa = m.id "
                + "JOIN prato pr ON p.id_prato = pr.id";
        return executarQueryLista(sql, null, rs -> {
            List<Pedido> lista = new ArrayList<>();
            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        0, // idPrato não é usado aqui
                        rs.getInt("numero_mesa"),
                        rs.getBoolean("confirmado"),
                        rs.getBoolean("entregue")
                );
                pedido.setNomePrato(rs.getString("nome_prato"));
                lista.add(pedido);
            }
            return lista;
        });
    }

    public List<Pedido> listarPedidoStatus(int idMesa) throws SQLException {
        String sql = "SELECT p.id, p.id_prato, p.id_mesa, pr.nome, p.confirmado "
                + "FROM pedido p "
                + "JOIN prato pr ON p.id_prato = pr.id "
                + "WHERE p.id_mesa = ?";
        return executarQueryLista(sql, stmt -> stmt.setInt(1, idMesa), rs -> {
            List<Pedido> lista = new ArrayList<>();
            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getInt("id_prato"),
                        rs.getInt("id_mesa"),
                        rs.getBoolean("confirmado")
                );
                pedido.setNomePrato(rs.getString("nome"));
                lista.add(pedido);
            }
            return lista;
        });
    }

    public void setConfirmado(int id, boolean confirmado) throws SQLException {
        String sql = "UPDATE pedido SET confirmado = ? WHERE id = ?";
        executarUpdate(sql, stmt -> {
            stmt.setBoolean(1, confirmado);
            stmt.setInt(2, id);
        });
    }

    public void setEntregue(int id, boolean entregue) throws SQLException {
        String sql = "UPDATE pedido SET entregue = ? WHERE id = ?";
        executarUpdate(sql, stmt -> {
            stmt.setBoolean(1, entregue);
            stmt.setInt(2, id);
        });
    }
    
    public double fecharContaPorNumeroMesa(int numeroMesa) throws SQLException {
    String sqlFindMesa = "SELECT id FROM mesa WHERE numero = ?";
    String sqlCheck = "SELECT COUNT(*) AS qtd, COALESCE(SUM(pr.preco), 0) AS total "
                    + "FROM pedido p LEFT JOIN prato pr ON p.id_prato = pr.id "
                    + "WHERE p.id_mesa = ?";
    String sqlDelete = "DELETE FROM pedido WHERE id_mesa = ?";

    try (Connection conexao = new DbConnection().getConnection()) {

        Integer mesaId = null;
        try (PreparedStatement ps = conexao.prepareStatement(sqlFindMesa)) {
            ps.setInt(1, numeroMesa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mesaId = rs.getInt("id");
                } else {
                    // mesa não existe
                    throw new SQLException("Mesa não encontrada: " + numeroMesa);
                }
            }
        }

        int qtd = 0;
        double total = 0.0;
        try (PreparedStatement ps = conexao.prepareStatement(sqlCheck)) {
            ps.setInt(1, mesaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    qtd = rs.getInt("qtd");
                    total = rs.getDouble("total"); 
                }
            }
        }

        if (qtd == 0) {
            return 0.0;
        }

        boolean originalAutoCommit = conexao.getAutoCommit();
        try {
            conexao.setAutoCommit(false);

            try (PreparedStatement psDel = conexao.prepareStatement(sqlDelete)) {
                psDel.setInt(1, mesaId);
                int deleted = psDel.executeUpdate();
                // opcional: checar deleted == qtd
            }

            conexao.commit();
        } catch (SQLException exTx) {
            try { conexao.rollback(); } catch (Exception ignore) {}
            throw exTx;
        } finally {
            conexao.setAutoCommit(originalAutoCommit);
        }

        return total;
    }
}
    
}

