package usuarios;

import classes.*;
import java.util.Scanner;

public class UserCliente {
    public void clienteProgram(Mesa[][] mesas, Cardapio cardapio, Feedback feedbacklist) {
        Scanner input = new Scanner(System.in);
        int escolha, k = 0;
        Mesa sua_mesa = null;

        // leaving some tables occupied
        mesas[0][1].ocupada = true;
        mesas[2][2].ocupada = true;
        mesas[1][2].ocupada = true;
        mesas[3][0].ocupada = true;


        System.out.println("Bem vindo(a) ao restaurante.");
        for (Mesa[] arrayMesa : mesas) {
            System.out.println(" ");
            for (Mesa value : arrayMesa) {
                if (value.ocupada) {
                    System.out.print("|X|");
                } else {
                    System.out.print("|" + value.numero + "|");
                }
            }
        }
        do {
            System.out.println("\nSelecione o número de uma mesa livre:");
            escolha = input.nextInt();
            input.nextLine();

            for (Mesa[] arrayMesa : mesas) {
                for (Mesa value : arrayMesa) {
                    if (escolha == value.numero && !value.ocupada) {
                        sua_mesa = value;
                        sua_mesa.ocupada = true;
                        k = 1;
                        break;
                    } else if (value.ocupada && escolha == value.numero) {
                        System.out.println("Essa mesa está ocupada");
                    }
                }
            }

        } while (k == 0);

        if (!sua_mesa.pago) {
            do {
                System.out.println("""
                        Selecione a opção desejada:
                        1 - Enviar pedido.
                        2 - Cancelar pedido.
                        3 - Ver pedidos.
                        4 - Pagar mesa.
                        5 - Dar feedback.
                        6 - Sair.
                        """);
                escolha = input.nextInt();
                input.nextLine();

                switch (escolha) {

                    case 1:
                        Pedido pedido = sua_mesa.enviarPedido(cardapio, mesas);
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
                    case 2:
                        sua_mesa.cancelarPedido(mesas);
                        break;
                    case 3:
                        sua_mesa.verPedidos();
                        System.out.println("Aperte ENTER para continuar.");
                        input.nextLine();
                        break;
                    case 4:
                        sua_mesa.pagarMesa(mesas);
                        break;
                    case 5:
                        sua_mesa.darFeedback(mesas, feedbacklist);
                        break;
                    case 6:
                        System.out.println("Você deve pagar antes de sair!");
                        break;
                    default:
                        System.out.println("Selecione uma opção válida.");
                        break;
                }
            } while (!sua_mesa.pago);
        }
    }
}