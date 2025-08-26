package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Feedback;

public class FeedbackDAO {

    private Connection abrirConexao() throws SQLException {
        DbConnection dbConnection = new DbConnection();
        return dbConnection.getConnection();
    }

    private void executarUpdate(String sql, Object... params) throws SQLException {
        try (Connection conexao = abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    public List<Feedback> listarFeedbacks() throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM feedback";

        try (Connection conexao = abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String comentario = rs.getString("comentario");

                feedbacks.add(new Feedback(id, comentario));
            }
        }

        return feedbacks;
    }

    public void addFeedback(String comentario) throws SQLException {
        String sql = "INSERT INTO feedback (comentario) VALUES (?)";
        executarUpdate(sql, comentario);
    }

    public void delFeedback(int id) throws SQLException {
        String sql = "DELETE FROM feedback WHERE id = ?";
        executarUpdate(sql, id);
    }
}
