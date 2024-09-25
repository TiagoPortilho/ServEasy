package classes;

public interface ServicoCliente {
        void enviarPedido(Cardapio _cardapio,Mesa[][] mesas);
        void enviarPedido(Cardapio _cardapio);

        void cancelarPedido(Mesa[][] mesas);
        void cancelarPedido();
}
