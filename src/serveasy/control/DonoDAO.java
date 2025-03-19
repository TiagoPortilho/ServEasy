package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import serveasy.banco.DbConnection;
import serveasy.view.TelaErro;

public class DonoDAO {

    public String getEstadoRestaurante() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        String estadoRestaurante = null;

        try {
            String sql = "SELECT estado_restaurante FROM processo_dono WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                estadoRestaurante = rs.getString("estado_restaurante");
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

        return estadoRestaurante;
    }

    public double getTotalGanho() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        double totalGanho = 0.0;

        try {
            String sql = "SELECT total_ganho FROM processo_dono WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalGanho = rs.getDouble("total_ganho");
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

        return totalGanho;
    }

    public String getNota() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        String nota = null;

        try {
            String sql = "SELECT nota FROM processo_dono WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nota = rs.getString("nota");
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

        return nota;
    }

    public double getGanhoHoje() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();
        double ganhoHoje = 0.0;

        try {
            String sql = "SELECT ganho_hoje FROM processo_dono WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ganhoHoje = rs.getDouble("ganho_hoje");
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

        return ganhoHoje;
    }

    public void setEstadoRestaurante(String novoEstado) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE processo_dono SET estado_restaurante = ? WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, novoEstado);
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

    public void setNota(String novaNota) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE processo_dono SET nota = ? WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setObject(1, novaNota);
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

    public void setGanhoHoje(double novoGanhoHoje) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE processo_dono SET ganho_hoje = ? WHERE id = 1";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setDouble(1, novoGanhoHoje);
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

    public void adicionarGanhoTotal() {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String selectSql = "SELECT total_ganho, ganho_hoje FROM processo_dono WHERE id = 1";
            PreparedStatement stmtSelect = conexao.prepareStatement(selectSql);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                double totalGanho = rs.getDouble("total_ganho");
                double ganhoHoje = rs.getDouble("ganho_hoje");

                totalGanho += ganhoHoje;

                String updateSql = "UPDATE processo_dono SET total_ganho = ?, ganho_hoje = 0 WHERE id = 1";
                PreparedStatement stmtUpdate = conexao.prepareStatement(updateSql);
                stmtUpdate.setDouble(1, totalGanho);
                stmtUpdate.executeUpdate();
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
    
    public void adicionarGanhoHoje(double valor) {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();

    try {
        // Recupera o ganho_hoje atual
        String selectSql = "SELECT ganho_hoje FROM processo_dono WHERE id = 1";
        PreparedStatement stmtSelect = conexao.prepareStatement(selectSql);
        ResultSet rs = stmtSelect.executeQuery();

        if (rs.next()) {
            double ganhoHojeAtual = rs.getDouble("ganho_hoje");
            // Soma o valor passado ao ganho_hoje atual
            ganhoHojeAtual += valor;

            // Atualiza o ganho_hoje no banco
            String updateSql = "UPDATE processo_dono SET ganho_hoje = ? WHERE id = 1";
            PreparedStatement stmtUpdate = conexao.prepareStatement(updateSql);
            stmtUpdate.setDouble(1, ganhoHojeAtual);
            stmtUpdate.executeUpdate();
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
    }
    
    
}
