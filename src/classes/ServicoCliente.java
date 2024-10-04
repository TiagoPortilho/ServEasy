package classes;

public interface ServicoCliente {
        Pedido enviarPedido(Cardapio _cardapio,Mesa[][] mesas);
        void cancelarPedido(Mesa[][] mesas);
        void pagarMesa(Mesa[][] mesas);
        void darFeedback(Mesa[][] mesas, Feedback feedbacklist);
}
