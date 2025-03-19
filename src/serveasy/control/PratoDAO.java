package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Prato;
import serveasy.view.TelaErro;

public class PratoDAO {

    public void addPrato(Prato prato) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        String nome,descricao;
        float preco;
        nome = prato.getNome();
        preco = prato.getPreco();
        descricao = prato.getDescricao();

        try {
            String sql = "INSERT INTO prato (nome, preco, descricao) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setFloat(2, preco);
            stmt.setString(3, descricao);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            String e = ex.getMessage();
            new TelaErro(e).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
            }
        }
    }

    public void delPrato(int id) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "DELETE FROM prato WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            String e = ex.getMessage();
            new TelaErro(e).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
            }
        }
    }

    
    public Prato getPrato(int id) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        Prato prato = null;

        try {
            String sql = "SELECT * FROM prato WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                float preco = rs.getFloat("preco");
                String descricao = rs.getString("descricao");
                prato = new Prato(id, nome, preco, descricao);
            }
        } catch (SQLException ex) {
            String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
            }
        }
        return prato;
    }

    public List<Prato> listarPratos() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        List<Prato> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM prato";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                float preco = rs.getFloat("preco");
                String descricao = rs.getString("descricao");
                Prato prato = new Prato(id, nome, preco, descricao);
                lista.add(prato);
            }
        } catch (SQLException ex) {
            String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                String e = ex.getMessage();
                new TelaErro(e).setVisible(true);
            }
        }
        return lista;
    }
    
    public String getNomePrato(int id) {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();
    String nome = null;

    try {
        String sql = "SELECT nome FROM prato WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            nome = rs.getString("nome");
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
    return nome;
    }
    
    public float getPrecoPrato(int id) {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();
    float preco = 0.0f;

    try {
        String sql = "SELECT preco FROM prato WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            preco = rs.getFloat("preco");
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
    return preco;
    }
}
