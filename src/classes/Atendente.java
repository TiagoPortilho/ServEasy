package classes;

import java.util.Scanner;

public class Atendente extends Funcionarios implements ServicoCliente{
    private double Salario = 1000.00f; //default value

    public Atendente(String _nome, String _cpf, int _idade) {
        super(_nome, _cpf, _idade);

    }

    public double getSalario() {
        return Salario;
    }

    public void setSalario(double salario) {
        Salario = salario;
    }

    @Override
    public void enviarPedido(Cardapio _cardapio) {
        //don't need to use, it is for interface implementation.
    }

    @Override
    public void enviarPedido(Cardapio _cardapio, Mesa[][] mesas) {
        Scanner input = new Scanner(System.in);
        int num_mesa;
        int j,l = 0;//Select
        Mesa _mesa = null;
        Prato _prato;
        do {
            System.out.println("Digite o número da mesa:");
            num_mesa = input.nextInt();input.nextLine();
            for (Mesa[] mesa : mesas) {
                for (Mesa value : mesa) {
                    if (value != null && value.numero == num_mesa && value.ocupada) {
                        _mesa = value;
                        l = 1;//Leave the loop
                        break;//It means that table number exists
                    }
                }
            }
            System.out.println("O número dessa mesa não existe ou não está ocupada.");
        } while (l != 1);

        System.out.println("Mostrando cardápio:");
        _cardapio.mostrarCardapio();
        System.out.println("Selecione o prato desejado:");
        j = input.nextInt();input.nextLine();
        _prato = _cardapio.selectList(j);
        _mesa.pedidos.add(new Pedido(_prato,num_mesa));
        //the path to kitchen will be implemented soon
        _mesa.valor_gasto += _prato.preco;//it can be subtracted if the order is canceled

        System.out.println("Pedido feito! Aperte ENTER para continuar");
        input.nextLine();
    }

    @Override
    public void cancelarPedido() {
        //don't need to use, this is for interface implementation.
    }

    @Override
    public void cancelarPedido(Mesa[][] mesas) {
        Scanner input = new Scanner(System.in);
        int num_mesa;
        Pedido _pedido;
        int j,cont = 0,l = 0;//Select
        Mesa _mesa = null;

        do {
            System.out.println("Digite o número da mesa:");
            num_mesa = input.nextInt();input.nextLine();
            for (Mesa[] mesa : mesas) {
                for (Mesa value : mesa) {
                    if (value != null && value.numero == num_mesa && value.ocupada) {
                        _mesa = value;
                        l = 1;//Leave the loop
                        break;//It means that table number exists
                    }
                }
            }
            System.out.println("O número dessa mesa não existe ou não está ocupada.");
        } while (l != 1);
        System.out.println("Mostrando pedidos:");
        for (Pedido pedido : _mesa.pedidos){
            System.out.println("\nPedido - " + cont);
            pedido.mostrarPedido();
            cont++;
        }
        System.out.println("\nSelecione o pedido que deseja cancelar:");
        j = input.nextInt();input.nextLine();
        _pedido = _mesa.pedidos.get(j);
        if (!_pedido.confirmado){
            System.out.println("Seu pedido já foi confirmado e está em preparo, não será possível cancelar.");
        }
        else {
            _mesa.pedidos.remove(_pedido);
            System.out.println("Seu pedido foi cancelado.");
        }
    }


}
