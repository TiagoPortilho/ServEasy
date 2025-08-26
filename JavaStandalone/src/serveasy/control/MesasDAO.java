package serveasy.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import serveasy.banco.DbConnection;
import serveasy.model.Mesa;

public class MesasDAO {

    @FunctionalInterface
    interface ResultSetMapper<T> {

        T map(ResultSet rs) throws SQLException;
    }

    private <T> List<T> executarQueryLista(String sql, Object[] params, ResultSetMapper<T> mapper) throws SQLException {
        List<T> lista = new ArrayList<>();
        try (Connection conexao = new DbConnection().getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapper.map(rs));
                }
            }
        }
        return lista;
    }

    private <T> T executarQueryUnica(String sql, Object[] params, ResultSetMapper<T> mapper) throws SQLException {
        try (Connection conexao = new DbConnection().getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
                return null;
            }
        }
    }

    private void executarUpdate(String sql, Object... params) throws SQLException {
        try (Connection conexao = new DbConnection().getConnection(); PreparedStatement stmt = conexao.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    public List<Mesa> listarMesas() throws SQLException {
        String sql = "SELECT * FROM mesa";
        return executarQueryLista(sql, new Object[]{}, rs -> new Mesa(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getBoolean("ocupada"),
                rs.getFloat("valor_gasto"),
                rs.getBoolean("pago")
        ));
    }

    public void criarMesas(int quantidade) throws SQLException {
        executarUpdate("DELETE FROM mesa WHERE id != 1");

        for (int i = 0; i < quantidade; i++) {
            int numeroMesa = (int) (Math.random() * 900) + 100;
            executarUpdate("INSERT INTO mesa (numero, ocupada, valor_gasto, pago) VALUES (?, false, 0.0, false)", numeroMesa);
        }
    }

    public Mesa buscarMesa(int numeroMesa) throws SQLException {
        String sql = "SELECT * FROM mesa WHERE numero = ?";
        return executarQueryUnica(sql, new Object[]{numeroMesa}, rs -> new Mesa(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getBoolean("ocupada"),
                rs.getFloat("valor_gasto"),
                rs.getBoolean("pago")
        ));
    }

    private boolean getBooleanColuna(int numero, String coluna) throws SQLException {
        String sql = "SELECT " + coluna + " FROM mesa WHERE numero = ?";
        Boolean resultado = executarQueryUnica(sql, new Object[]{numero}, rs -> rs.getBoolean(coluna));
        return resultado != null && resultado;
    }

    private void setBooleanColuna(int numero, String coluna, boolean valor) throws SQLException {
        String sql = "UPDATE mesa SET " + coluna + " = ? WHERE numero = ?";
        executarUpdate(sql, valor, numero);
    }

    public boolean getOcupada(int numero) throws SQLException {
        return getBooleanColuna(numero, "ocupada");
    }

    public void setOcupada(int numero, boolean ocupada) throws SQLException {
        setBooleanColuna(numero, "ocupada", ocupada);
    }

    public boolean getPago(int numero) throws SQLException {
        return getBooleanColuna(numero, "pago");
    }

    public void setPago(int numero, boolean pago) throws SQLException {
        setBooleanColuna(numero, "pago", pago);
    }

    public float getValorGasto(int numero) throws SQLException {
        String sql = "SELECT valor_gasto FROM mesa WHERE numero = ?";
        Float resultado = executarQueryUnica(sql, new Object[]{numero}, rs -> rs.getFloat("valor_gasto"));
        return resultado != null ? resultado : 0.0f;
    }

    public void setValorGasto(int numero, float valorGasto) throws SQLException {
        String sql = "UPDATE mesa SET valor_gasto = ? WHERE numero = ?";
        executarUpdate(sql, valorGasto, numero);
    }
}
