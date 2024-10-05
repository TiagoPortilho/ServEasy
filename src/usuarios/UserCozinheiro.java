package usuarios;
import java.util.Random;
import classes.*;

import java.util.Scanner;

public class UserCozinheiro {
    public void cozinheiroProgram(Mesa[][] mesas, Cardapio cardapio, Feedback feedbacklist){
        Scanner input = new Scanner(System.in);

        //Random orders.
        Random random = new Random();
        int i = random.nextInt(mesas.length);
        int j = random.nextInt(mesas[i].length);
        int k = random.nextInt(cardapio.sizeList());

        Pedido pedido;
        Mesa mesa;

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

        Cozinheiro you = new Cozinheiro(nome,cpf,idade);

        for (int a = 0; a < 5; a++){
            System.out.println("Espere o pedido...");

            try {
                // Pausar a execução por 2 segundos (2000 milissegundos)
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                // Tratar exceção caso a thread seja interrompida
                System.out.println("A thread foi interrompida.");
            }
            if (mesas[i][j].ocupada) {
                mesa = mesas[i][j];
                pedido = new Pedido(cardapio.selectList(k), mesa.numero);
                you.receberPedido(pedido);
                i = random.nextInt(mesas.length);
                j = random.nextInt(mesas[i].length);
                k = random.nextInt(cardapio.sizeList());
            }
            else{
                i = random.nextInt(mesas.length);
                j = random.nextInt(mesas[i].length);
            }
        }
        System.out.println("Você acabou o exemplo.");


    }
}
