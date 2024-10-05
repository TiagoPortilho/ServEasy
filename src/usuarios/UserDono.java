package usuarios;

import classes.*;

import java.util.Random;
import java.util.Scanner;

public class UserDono {
    public void donoProgram(Mesa[][] mesas, Cardapio cardapio, Feedback feedbacklist){
        Scanner input = new Scanner(System.in);
        int escolha;

        // Feedbacks for tests
        feedbacklist.addList("--feedback 1--");
        feedbacklist.addList("--feedback 2--");
        feedbacklist.addList("--feedback 3--");

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

        //For mostrarGanhos and controleMesas tests.
        new Thread(() -> {
            try {
                Random random = new Random();
                int i = random.nextInt(mesas.length); // Inicializar 'i'
                int j = random.nextInt(mesas[i].length); // Inicializar 'j'

                while (true) {
                    Thread.sleep(20000); // Pausa de 20 segundos


                    if (mesas[i][j] != null && mesas[i][j].ocupada && !(mesas[i][j].pago)) {
                        mesas[i][j].valor_gasto = 30 + random.nextInt(121); // 30 a 150
                        mesas[i][j].pago = true;
                    }


                    i = random.nextInt(mesas.length);
                    j = random.nextInt(mesas[i].length);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Erro nos índices da matriz mesas: " + e.getMessage());
            }
        }).start();

        System.out.println("Digite seu nome:");
        String nome = input.nextLine();
        System.out.println("Digite seu cpf:");
        String cpf = input.nextLine();
        System.out.println("Digite sua idade:");
        int idade = input.nextInt();input.nextLine();

        Dono you = new Dono(nome,cpf,idade);

        do {
            System.out.println("""
                    \nSelecione a opção desejada:
                    1 - Editar Cardápio
                    2 - Mostrar Ganhos
                    3 - Controle de Mesas
                    4 - Ver Feedbacks
                    5 - Sair
                    """);
            escolha = input.nextInt();input.nextLine();
            switch (escolha){
                case 1:
                    you.editarCardapio(cardapio);
                    break;
                case 2:
                    you.mostrarGanhos(mesas);
                    break;
                case 3:
                    you.controleMesas(mesas);
                    break;
                case 4:
                    feedbacklist.verFeedbacks();
                    System.out.println("Aperte ENTER para continuar");
                    input.nextLine();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Selecione uma opção válida.");
                    break;
            }
        }while (escolha != 5);
    }
}
