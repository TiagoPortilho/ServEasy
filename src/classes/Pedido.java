package classes;

public class Pedido {
    Prato prato;
    int num_mesa;
    boolean confirmado = false;

    public Pedido(Prato prato, int num_mesa) {
        this.prato = prato;
        this.num_mesa = num_mesa;
    }
}
