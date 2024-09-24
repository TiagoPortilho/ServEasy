import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import classes.*;

public class Main {
    public static void main(String[] args) {
        List<Pedido> pedidos = new ArrayList<>(); // Lista vazia de pedidos por simplicidade
        Mesa[][] mesas = new Mesa[10][10];
        Cardapio cardapio = new Cardapio();
        // Inicializar as mesas (algumas ocupadas, outras não)
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boolean ocupada = (i + j) % 2 == 0; // Alterna entre ocupada e não ocupada
                mesas[i][j] = new Mesa(i * 10 + j + 1, ocupada, pedidos, 100, false);
            }
        }
        Atendente atendente = new Atendente("Tingos", "52577379811", 19,10000);
        atendente.enviarPedido(cardapio,mesas);
    }
}