package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Pedido;
import serveasy.view.TelaErro;

public class PedidoDAO {

    public void addPedido(Pedido pedido) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "INSERT INTO pedido (id_prato, id_mesa, confirmado) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, pedido.getIdPrato());
            stmt.setInt(2, pedido.getIdMesa());
            stmt.setBoolean(3, pedido.isConfirmado());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
        }
    }

    public void delPedido(int id) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "DELETE FROM pedido WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
        }
    }

    public Pedido getPedido(int id) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        Pedido pedido = null;

        try {
            String sql = "SELECT * FROM pedido WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idPrato = rs.getInt("id_prato");
                int idMesa = rs.getInt("id_mesa");
                boolean confirmado = rs.getBoolean("confirmado");
                pedido = new Pedido(id, idPrato, idMesa, confirmado);
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                new TelaErro(ex.getMessage()).setVisible(true);
            }
        }
        return pedido;
    }

   public List<Pedido> listarPedidos() {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();
    List<Pedido> lista = new ArrayList<>();

    try {
        String sql = "SELECT p.id AS id_pedido, m.numero AS numero_mesa, pr.nome AS nome_prato, p.confirmado " +
                     "FROM pedido p " +
                     "JOIN mesa m ON p.id_mesa = m.id " +
                     "JOIN prato pr ON p.id_prato = pr.id";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int idPedido = rs.getInt("id_pedido");
            int numeroMesa = rs.getInt("numero_mesa");
            String nomePrato = rs.getString("nome_prato");
            boolean confirmado = rs.getBoolean("confirmado");
            
            Pedido pedido = new Pedido(idPedido, 0, numeroMesa, confirmado);
            pedido.setNomePrato(nomePrato);
            lista.add(pedido);
        }
    } catch (SQLException ex) {
        new TelaErro(ex.getMessage()).setVisible(true);
    } finally {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }
    return lista;
}
    
    public List<Pedido> listarPedidoStatus(int id_mesa) {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();
    List<Pedido> lista = new ArrayList<>();

    try {
        String sql = "SELECT p.id, p.id_prato, p.id_mesa, pr.nome, p.confirmado " +
                     "FROM pedido p " +
                     "JOIN prato pr ON p.id_prato = pr.id " +
                     "WHERE p.id_mesa = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, id_mesa);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int idPedido = rs.getInt("id");
            int idPrato = rs.getInt("id_prato");
            int idMesa = rs.getInt("id_mesa");
            String nomePrato = rs.getString("nome");
            boolean confirmado = rs.getBoolean("confirmado");

            Pedido pedido = new Pedido(idPedido, idPrato, idMesa, confirmado);
            pedido.setNomePrato(nomePrato);
            lista.add(pedido);
        }
    } catch (SQLException ex) {
        new TelaErro(ex.getMessage()).setVisible(true);
    } finally {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException ex) {
            new TelaErro(ex.getMessage()).setVisible(true);
        }
    }
    return lista;
}
}