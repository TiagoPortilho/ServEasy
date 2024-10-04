package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mesa implements ServicoCliente{
    public int numero;
    public boolean ocupada = false;// default value
    public List<Pedido> pedidos = new ArrayList<>();
    public float valor_gasto = 0;// default value
    public boolean pago = false;// default value

    public Mesa(int numero) {
        this.numero = numero;
    }

    @Override
    public Pedido enviarPedido(Cardapio _cardapio, Mesa[][] mesas) {
        Scanner input = new Scanner(System.in);
        Pedido pedido;
        int j;//Select
        Prato _prato;
        int num_mesa = this.numero;
        System.out.println("Mostrando cardápio:");
        _cardapio.mostrarCardapio();
        System.out.println("Selecione o prato desejado:");
        j = input.nextInt();input.nextLine();
        _prato = _cardapio.selectList(j);
        pedido = new Pedido(_prato,num_mesa);
        pedidos.add(pedido);
        //the path to kitchen will be implemented soon
        valor_gasto += _prato.preco;//it can be subtracted if the order is canceled
        System.out.println("Pedido feito! Aperte ENTER para continuar");
        input.nextLine();
        return pedido;
    }

    @Override
    public void cancelarPedido(Mesa[][] mesas) {
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
            valor_gasto -= _pedido.prato.preco;
        }
    }

    @Override
    public void pagarMesa(Mesa[][] mesas) {
        Scanner input = new Scanner(System.in);
        String i;
        int escolha;
        System.out.println("Deseja fechar a conta?(S)Sim (N)Não");
        i = input.nextLine().toUpperCase();
        if (i.equals("S")){
            System.out.println("Valor gasto: R$" + valor_gasto);
            do {
                System.out.println("""
                    Escolha a forma de pagamento:
                    1 - PIX
                    2 - Crédito
                    3 - Débito
                    4 - Aplicativo de pagamento
                    """);
                escolha = input.nextInt();input.nextLine();
                switch (escolha){
                    case 1:
                        System.out.println("Pagamento efetuado!");
                    case 2:
                        System.out.println("Pagamento efetuado!");
                    case 3:
                        System.out.println("Pagamento efetuado!");
                    case 4:
                        System.out.println("Pagamento efetuado!");
                    default:
                        System.out.println("Selecione uma opção válida");
                }
            }while (!(escolha == 5));

        }

    }

    @Override
    public void darFeedback(Mesa[][] mesas, Feedback feedbacklist){
        Scanner input = new Scanner(System.in);
        String _feedback;
        System.out.println("Escreva seu feedback:");
        _feedback = input.nextLine();
        feedbacklist.addList(_feedback);
        System.out.println("Seu feedback foi enviado! Aperte ENTER para continuar.");
        input.nextLine();
    }
}
