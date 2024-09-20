package classes;
import java.util.Scanner;

public class Cozinheiro extends Funcionarios{
    private double Salario = 1500.00f; //default value

    public Cozinheiro(String _nome, String _cpf, int _idade, double salario) {
        super(_nome, _cpf, _idade);
        Salario = salario;
    }

    public double getSalario() {
        return Salario;
    }

    public void setSalario(double salario) {
        Salario = salario;
    }

    //temporary method for receiving orders
    public void receberPedido(Pedido _pedido){
        System.out.println("Novo pedido\nMesa: " + _pedido.num_mesa + "\nPrato: "
                + _pedido.prato + "\n Aperte ENTER para confirmar o pedido");

        Scanner input = new Scanner(System.in);//this is for the ENTER to confirm
        input.nextLine();

        _pedido.confirmado = true;

    }
}
