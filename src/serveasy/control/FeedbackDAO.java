package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Feedback;
import serveasy.view.TelaErro;


public class FeedbackDAO {

    public List<Feedback> listarFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "SELECT * FROM feedback";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String comentario = rs.getString("comentario");

                // Adiciona o feedback na lista
                Feedback feedback = new Feedback(id, comentario);
                feedbacks.add(feedback);
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
        
        return feedbacks;
    }

    
    public void addFeedback(String comentario) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "INSERT INTO feedback (comentario) VALUES (?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, comentario);
            stmt.executeUpdate();
            System.out.println("Feedback adicionado com sucesso!");
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

    public void delFeedback(int id) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "DELETE FROM feedback WHERE id = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Feedback excluído com sucesso!");
            } else {
                System.out.println("Feedback não encontrado!");
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
    }
}