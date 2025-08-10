package serveasy.control;

import java.sql.*;
import serveasy.model.Usuario;
import serveasy.banco.DbConnection;
import serveasy.view.TelaErro;

public class UsuarioDAO {

    private <T> T executarQuery(String sql, PreparedStatementSetter pss, ResultSetHandler<T> rsh) throws SQLException {
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

    @FunctionalInterface
    private interface PreparedStatementSetter {

        void setParameters(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    private interface ResultSetHandler<T> {

        T handle(ResultSet rs) throws SQLException;
    }

    public Usuario autenticar(Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
        return executarQuery(sql,
                stmt -> {
                    stmt.setString(1, usuario.getLogin());
                    stmt.setString(2, usuario.getSenha());
                },
                rs -> {
                    if (rs.next()) {
                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("login"),
                                rs.getString("senha"),
                                rs.getInt("id_funcionario"),
                                rs.getInt("id_mesa")
                        );
                    }
                    return null;
                });
    }

    public String getTipoFuncionarioOuMesa(int idUsuario) throws SQLException {
        String sql = """
            SELECT u.id_funcionario, f.tipo_funcionario, u.id_mesa, m.numero
            FROM usuario u
            LEFT JOIN funcionario f ON u.id_funcionario = f.id
            LEFT JOIN mesa m ON u.id_mesa = m.id
            WHERE u.id = ?
        """;

        return executarQuery(sql,
                stmt -> stmt.setInt(1, idUsuario),
                rs -> {
                    if (rs.next()) {
                        int idFuncionario = rs.getInt("id_funcionario");
                        String tipoFuncionario = rs.getString("tipo_funcionario");
                        int idMesa = rs.getInt("id_mesa");
                        int numeroMesa = rs.getInt("numero");

                        if (idFuncionario > 0) {
                            return tipoFuncionario;
                        } else if (idMesa > 0) {
                            return String.valueOf(numeroMesa);
                        }
                    }
                    return "Usuário não encontrado ou sem função definida.";
                });
    }
}
