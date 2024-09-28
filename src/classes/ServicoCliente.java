package classes;

public interface ServicoCliente {
        void enviarPedido(Cardapio _cardapio,Mesa[][] mesas);
        void cancelarPedido(Mesa[][] mesas);
        void pagarMesa(Mesa[][] mesas);
}
