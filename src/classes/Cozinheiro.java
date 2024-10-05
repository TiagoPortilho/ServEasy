package classes;
import java.util.Scanner;

public class Cozinheiro extends Funcionarios{
    private double Salario = 1500.00f; //default value

    public Cozinheiro(String _nome, String _cpf, int _idade) {
        super(_nome, _cpf, _idade);
    }

    public double getSalario() {
        return Salario;
    }

    public void setSalario(double salario) {
        Salario = salario;
    }

    public void receberPedido(Pedido _pedido){
        System.out.println("Novo pedido\nMesa: " + _pedido.num_mesa + "\nPrato: "
                + _pedido.prato.nome + "\n Aperte ENTER para confirmar o pedido");

        Scanner input = new Scanner(System.in);//this is for the ENTER to confirm
        input.nextLine();

        _pedido.confirmado = true;
    }
}
