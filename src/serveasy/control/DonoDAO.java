package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import serveasy.banco.DbConnection;

public class DonoDAO {

    private Connection abrirConexao() throws SQLException {
        DbConnection dbConnection = new DbConnection();
        return dbConnection.getConnection();
    }

    private String executarQueryString(String sql, String coluna) throws SQLException {
        try (Connection conexao = abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString(coluna);
            }
            return null;
        }
    }

    private double executarQueryDouble(String sql, String coluna) throws SQLException {
        try (Connection conexao = abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(coluna);
            }
            return 0.0;
        }
    }

    private void executarUpdate(String sql, Object... params) throws SQLException {
        try (Connection conexao = abrirConexao(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    public String getEstadoRestaurante() throws SQLException {
        String sql = "SELECT estado_restaurante FROM processo_dono WHERE id = 1";
        return executarQueryString(sql, "estado_restaurante");
    }

    public double getTotalGanho() throws SQLException {
        String sql = "SELECT total_ganho FROM processo_dono WHERE id = 1";
        return executarQueryDouble(sql, "total_ganho");
    }

    public String getNota() throws SQLException {
        String sql = "SELECT nota FROM processo_dono WHERE id = 1";
        return executarQueryString(sql, "nota");
    }

    public double getGanhoHoje() throws SQLException {
        String sql = "SELECT ganho_hoje FROM processo_dono WHERE id = 1";
        return executarQueryDouble(sql, "ganho_hoje");
    }

    public void setEstadoRestaurante(String novoEstado) throws SQLException {
        String sql = "UPDATE processo_dono SET estado_restaurante = ? WHERE id = 1";
        executarUpdate(sql, novoEstado);
    }

    public void setNota(String novaNota) throws SQLException {
        String sql = "UPDATE processo_dono SET nota = ? WHERE id = 1";
        executarUpdate(sql, novaNota);
    }

    public void setGanhoHoje(double novoGanhoHoje) throws SQLException {
        String sql = "UPDATE processo_dono SET ganho_hoje = ? WHERE id = 1";
        executarUpdate(sql, novoGanhoHoje);
    }

    public void adicionarGanhoTotal() throws SQLException {
        String selectSql = "SELECT total_ganho, ganho_hoje FROM processo_dono WHERE id = 1";
        try (Connection conexao = abrirConexao(); PreparedStatement stmtSelect = conexao.prepareStatement(selectSql); ResultSet rs = stmtSelect.executeQuery()) {

            if (rs.next()) {
                double totalGanho = rs.getDouble("total_ganho");
                double ganhoHoje = rs.getDouble("ganho_hoje");
                totalGanho += ganhoHoje;

                String updateSql = "UPDATE processo_dono SET total_ganho = ?, ganho_hoje = 0 WHERE id = 1";
                try (PreparedStatement stmtUpdate = conexao.prepareStatement(updateSql)) {
                    stmtUpdate.setDouble(1, totalGanho);
                    stmtUpdate.executeUpdate();
                }
            }
        }
    }

    public void adicionarGanhoHoje(double valor) throws SQLException {
        String selectSql = "SELECT ganho_hoje FROM processo_dono WHERE id = 1";
        try (Connection conexao = abrirConexao(); PreparedStatement stmtSelect = conexao.prepareStatement(selectSql); ResultSet rs = stmtSelect.executeQuery()) {

            if (rs.next()) {
                double ganhoHojeAtual = rs.getDouble("ganho_hoje");
                ganhoHojeAtual += valor;

                String updateSql = "UPDATE processo_dono SET ganho_hoje = ? WHERE id = 1";
                try (PreparedStatement stmtUpdate = conexao.prepareStatement(updateSql)) {
                    stmtUpdate.setDouble(1, ganhoHojeAtual);
                    stmtUpdate.executeUpdate();
                }
            }
        }
    }

}
