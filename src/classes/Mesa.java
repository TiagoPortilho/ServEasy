package classes;

import java.util.List;
import java.util.Scanner;

public class Mesa implements ServicoCliente{
    int numero;
    boolean ocupada = false;//default value
    List<Pedido> pedidos;
    float valor_gasto;
    boolean pago;

    public Mesa(int numero, float valor_gasto, boolean pago) {
        this.numero = numero;
        this.valor_gasto = valor_gasto;
        this.pago = pago;
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
        valor_gasto += _prato.preco;//it can be subtracted if the order is canceled
        System.out.println("Pedido feito! Aperte ENTER para continuar");
        input.nextLine();
    }

    @Override
    public void cancelarPedido(Mesa[][] mesas) {
        //don't need to use, this is for interface implementation.
    }

    @Override
    public void cancelarPedido() {
        Scanner input = new Scanner(System.in);
        int j, cont = 0;
        Pedido _pedido;
        System.out.println("Mostrando pedidos:");
        for (Pedido pedido : pedidos){
            System.out.println("\nPedido - " + cont);
            pedido.mostrarPedido();
            cont++;
        }
        System.out.println("\nSelecione o pedido que deseja cancelar:");
        j = input.nextInt();input.nextLine();
        _pedido = pedidos.get(j);
        if (!_pedido.confirmado){
            System.out.println("Seu pedido já foi confirmado e está em preparo, não será possível cancelar.");
        }
        else {
            pedidos.remove(_pedido);
            System.out.println("Seu pedido foi cancelado.");
        }
    }


}
