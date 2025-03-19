package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import serveasy.model.Usuario;
import serveasy.banco.DbConnection;
import serveasy.view.TelaErro;

/**
 * Classe responsável por realizar operações de autenticação de usuários no banco de dados.
 * 
 * @author Tingos
 */
public class UsuarioDAO {

    public Usuario autenticar(Usuario usuario) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        String login = usuario.getLogin();
        String senha = usuario.getSenha();

        try {
            String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuarioAutenticado = new Usuario(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getInt("idFuncionario"),
                        rs.getInt("idMesa")
                );
                return usuarioAutenticado;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            String e = ex.getMessage();
            new TelaErro(e).setVisible(true);
            return null;
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                new TelaErro().setVisible(true);
            }
        }
    }
}