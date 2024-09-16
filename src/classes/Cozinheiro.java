package classes;

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
}
