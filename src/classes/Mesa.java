package classes;

import java.util.List;

public class Mesa implements ServicoCliente{
    int numero;
    boolean ocupada = false;//default value
    List<Pedido> pedidos;
    float valor_gasto;
    boolean pago;
}
