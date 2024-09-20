package classes;

import java.util.List;

public class Mesa implements ServicoCliente{
    int numero;
    boolean ocupada = false;//default value
    List<Pedido> pedidos;
    float valor_gasto;
    boolean pago;

    public Mesa(int numero, boolean ocupada, List<Pedido> pedidos, float valor_gasto, boolean pago) {
        this.numero = numero;
        this.ocupada = ocupada;
        this.pedidos = pedidos;
        this.valor_gasto = valor_gasto;
        this.pago = pago;
    }
}
