package classes;

import java.util.List;
import java.util.Scanner;

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

    public void addList(Pedido _pedido){
        pedidos.add(_pedido);
    }

    public void removeList(Pedido _pedido){
        pedidos.remove(_pedido);
    }

    @Override
    public void enviarPedido(Cardapio _cardapio, Mesa[][] mesas) {
        //don't need to use, this is for interface implementation.
    }

    @Override
    public void enviarPedido(Cardapio _cardapio) {
        Scanner input = new Scanner(System.in);
        int j;//Select
        Prato _prato;
        int num_mesa = this.numero;
        System.out.println("Mostrando cardápio:");
        _cardapio.mostrarCardapio();
        System.out.println("Selecione o prato desejado:");
        j = input.nextInt();input.nextLine();
        _prato = _cardapio.selectList(j);
        pedidos.add(new Pedido(_prato,num_mesa));
        //the path to kitchen will be implemented soon
        valor_gasto =+ _prato.preco;//it can be subtracted if the order is canceled
        System.out.println("Pedido feito! Aperte ENTER para continuar");
        input.nextLine();
    }


}
