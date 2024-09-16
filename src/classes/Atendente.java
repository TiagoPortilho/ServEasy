package classes;

public class Atendente extends Funcionarios implements ServicoCliente{
    private double Salario = 1000.00f; //default value

    public Atendente(String _nome, String _cpf, int _idade, double salario) {
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
