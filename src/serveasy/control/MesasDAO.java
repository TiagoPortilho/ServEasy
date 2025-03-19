package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Mesa;
import serveasy.view.TelaErro;

public class MesasDAO {

     public List<Mesa> listarMesas() {
        List<Mesa> lista = new ArrayList<>();
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "SELECT * FROM mesa";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int numero = rs.getInt("numero");
                boolean ocupada = rs.getBoolean("ocupada");
                float valorGasto = rs.getFloat("valor_gasto");
                boolean pago = rs.getBoolean("pago");

                
                Mesa mesa = new Mesa(id, numero, ocupada, valorGasto, pago);
                lista.add(mesa);
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


    public void criarMesas(int quantidade) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sqlDelete = "DELETE FROM mesa WHERE id != 1";
            PreparedStatement stmtDelete = conexao.prepareStatement(sqlDelete);
            stmtDelete.executeUpdate();

            for (int i = 0; i < quantidade; i++) {
                int numeroMesa = (int) (Math.random() * 900) + 100;

                String sqlInsert = "INSERT INTO mesa (numero, ocupada, valor_gasto, pago) VALUES (?, false, 0.0, false)";
                PreparedStatement stmtInsert = conexao.prepareStatement(sqlInsert);
                stmtInsert.setInt(1, numeroMesa);
                stmtInsert.executeUpdate();
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

    public boolean getOcupada(int numero) {
        boolean ocupada = false;
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "SELECT ocupada FROM mesa WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ocupada = rs.getBoolean("ocupada");
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
        return ocupada;
    }

    public void setOcupada(int numero, boolean ocupada) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE mesa SET ocupada = ? WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setBoolean(1, ocupada);
            stmt.setInt(2, numero);
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

    public boolean getPago(int numero) {
        boolean pago = false;
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "SELECT pago FROM mesa WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pago = rs.getBoolean("pago");
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
        return pago;
    }

    public void setPago(int numero, boolean pago) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE mesa SET pago = ? WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setBoolean(1, pago);
            stmt.setInt(2, numero);
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

    public float getValorGasto(int numero) {
        float valorGasto = 0.0f;
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "SELECT valor_gasto FROM mesa WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                valorGasto = rs.getFloat("valor_gasto");
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
        return valorGasto;
    }

    public void setValorGasto(int numero, float valorGasto) {
        DbConnection dbConnection = new DbConnection();
        Connection conexao = dbConnection.getConnection();

        try {
            String sql = "UPDATE mesa SET valor_gasto = ? WHERE numero = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setFloat(1, valorGasto);
            stmt.setInt(2, numero);
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
    
    public Mesa buscarMesa(int numeroMesa) {
    DbConnection dbConnection = new DbConnection();
    Connection conexao = dbConnection.getConnection();
    Mesa mesa = null;

    try {
        String sql = "SELECT * FROM mesa WHERE numero = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, numeroMesa);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            mesa = new Mesa(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getBoolean("ocupada"),
                rs.getFloat("valor_gasto"),
                rs.getBoolean("pago")
            );
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
    return mesa;
    }
    
    
    
}
