package classes;
import java.util.Scanner;

public class Dono extends Funcionarios{
    public Dono(String _nome, String _cpf, int _idade) {
        super(_nome, _cpf, _idade);
    }

    public void editarCardapio(Cardapio _cardapio){
        int funcao, rmv_prato;
        String nome_prato,descricao_prato;
        float preco_prato;
        Scanner input = new Scanner(System.in);
        System.out.println("Cardápio:");
        _cardapio.mostrarCardapio();

        do {
            System.out.println("""
                    Oque deseja editar?:
                    1 - Adicionar Prato
                    2 - Remover Prato
                    3 - Sair
                    """);
            funcao = input.nextInt();input.nextLine();
            switch (funcao){
                case 1:
                    System.out.println("Digite o nome do prato:");
                    nome_prato = input.nextLine();
                    System.out.println("Digite o valor do prato:");
                    preco_prato = input.nextFloat();input.nextLine();
                    System.out.println("Digite a descrição do prato:");
                    descricao_prato = input.nextLine();

                    Prato novo_prato = new Prato(nome_prato,preco_prato,descricao_prato);
                    _cardapio.addList(novo_prato);
                    System.out.println("Prato adicionado!\nAperte ENTER para continuar");
                    input.nextLine();
                    break;

                case 2:
                    _cardapio.mostrarCardapio();
                    System.out.println("Selecione o prato que deseja remover:");
                    rmv_prato = input.nextInt();input.nextLine();
                    _cardapio.removeList(rmv_prato);
                    System.out.println("Prato removido!\nAperte ENTER para continuar");
                    input.nextLine();
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Selecione uma opção válida.");
                    break;
            }
        }while (!(funcao == 3));

    }

    public void mostrarGanhos(Mesa[][] mesas){
        float ganhos = 0;
        for (Mesa[] mesa : mesas) {
            for (Mesa value : mesa) {
                if (value.pago) {
                    ganhos = ganhos + value.valor_gasto;
                }
            }
        }
        System.out.println("O valor total ganho no dia foi de R$" + ganhos);
    }

    public void controleMesas(Mesa[][] mesas){
        System.out.println("Mostrando mesas:(L)Livre (O)Ocupada");
        for (Mesa[] mesa : mesas) {
            System.out.println(" ");
            for (Mesa value : mesa) {
                if (value.ocupada){
                    System.out.print("|O|");//ocupada
                }
                else {
                    System.out.print("|L|");//livre
                }
            }
        }
    }
}
