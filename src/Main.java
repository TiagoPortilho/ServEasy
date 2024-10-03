import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import classes.*;
import usuarios.UserAtendente;
import usuarios.UserCliente;
import usuarios.UserCozinheiro;
import usuarios.UserDono;

public class Main {
    public static void main(String[] args) {
        // This part and "usuarios" folder is an example of how the project will work.
        Scanner input = new Scanner(System.in);
        int a;

        // Table filler.
        Mesa[][] mesas = new Mesa[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                mesas[i][j] = new Mesa(i * 10 + j + 1);
            }
        }

        // Menu filler.
        Cardapio cardapio = new Cardapio();
        cardapio.addList(new Prato("Feijoada", 35.90f, "Prato típico brasileiro com feijão preto, carnes e acompanhamentos."));
        cardapio.addList(new Prato("Macarronada", 28.50f, "Massa ao molho de tomate caseiro e manjericão."));
        cardapio.addList(new Prato("Bife à Parmegiana", 42.00f, "Filé de carne empanado com queijo e molho de tomate."));
        cardapio.addList(new Prato("Salmão Grelhado", 55.00f, "Salmão grelhado acompanhado de legumes ao vapor."));
        cardapio.addList(new Prato("Frango com Quiabo", 30.75f, "Frango cozido com quiabo e temperos regionais."));

        // This is a way to keep the script organized, using classes to split the script.
        System.out.println("""
                Selecione o tipo de usuário para o teste:
                1 - Cliente.
                2 - Atendente.
                3 - Cozinheiro.
                4 - Dono.
                """);
        a = input.nextInt();input.nextLine();

        switch (a){
            case 1:
                UserCliente cliente = new UserCliente();
                cliente.clienteProgram(mesas, cardapio);
            case 2:
                UserAtendente atendente = new UserAtendente();
                atendente.atendenteProgram(mesas,cardapio);
            case 3:
                UserCozinheiro cozinheiro = new UserCozinheiro();
                cozinheiro.cozinheiroProgram(mesas,cardapio);
            case 4:
                UserDono dono = new UserDono();
                dono.donoProgram(mesas,cardapio);
        }
    }
}