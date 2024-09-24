package classes;
import java.util.Scanner;

public interface ServicoCliente {
    public void enviarPedido(Cardapio _cardapio,Mesa[][] mesas);
    public void enviarPedido(Cardapio _cardapio);
}
