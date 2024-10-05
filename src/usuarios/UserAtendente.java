package usuarios;

import classes.*;

import java.util.Scanner;

public class UserAtendente {
    public void atendenteProgram(Mesa[][] mesas, Cardapio cardapio, Feedback feedbacklist){
        Scanner input = new Scanner(System.in);
        int escolha;


        // leaving some tables occupied
        mesas[0][1].ocupada = true;
        mesas[2][2].ocupada = true;
        mesas[1][2].ocupada = true;
        mesas[3][0].ocupada = true;
        mesas[0][2].ocupada = true;
        mesas[2][3].ocupada = true;
        mesas[1][0].ocupada = true;
        mesas[3][3].ocupada = true;
        mesas[2][1].ocupada = true;
        mesas[3][2].ocupada = true;
        mesas[1][1].ocupada = true;
        mesas[1][3].ocupada = true;


        System.out.println("Digite seu nome:");
        String nome = input.nextLine();
        System.out.println("Digite seu cpf:");
        String cpf = input.nextLine();
        System.out.println("Digite sua idade:");
        int idade = input.nextInt();input.nextLine();

        Atendente you = new Atendente(nome,cpf,idade);

        do {
            System.out.println("""
                        \nSelecione a opção desejada:
                        1 - Estado do restaurante.
                        2 - Enviar pedido.
                        3 - Cancelar pedido.
                        4 - Pagar mesa.
                        5 - Dar feedback.
                        6 - Sair.
                        """);
            escolha = input.nextInt();
            input.nextLine();

            switch (escolha) {
                case 1:
                    System.out.println("Estado do restaurante: |X|Vazia |00|Ocupada");
                    for (Mesa[] arrayMesa : mesas) {
                        System.out.println(" ");
                        for (Mesa value : arrayMesa) {
                            if (value.ocupada) {
                                System.out.print("|" + value.numero + "|");
                            } else {
                                System.out.print("|X|");
                            }
                        }
                    }
                case 2:
                    Pedido pedido = you.enviarPedido(cardapio, mesas);
                    // 20 second counter to accept the order
                    new Thread(() -> {
                        try {
                            Thread.sleep(25000);
                            pedido.confirmado = true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    break;
                case 3:
                    you.cancelarPedido(mesas);
                    break;
                case 4:
                    you.pagarMesa(mesas);
                    break;
                case 5:
                    you.darFeedback(mesas, feedbacklist);
                    break;
                case 6:
                    System.out.println("Muito obrigado pelo serviço!");
                    break;
                default:
                    System.out.println("Selecione uma opção válida.");
                    break;
            }
        } while (!(escolha == 6));



    }
}
