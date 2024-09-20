package classes;

public class Pedido {
    Prato prato;
    int num_mesa;
    boolean confirmado;

    public Pedido(Prato prato, int num_mesa, boolean confirmado) {
        this.prato = prato;
        this.num_mesa = num_mesa;
        this.confirmado = confirmado;
    }
}
