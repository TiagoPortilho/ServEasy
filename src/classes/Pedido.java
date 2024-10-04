package classes;

public class Pedido {
    public Prato prato;
    public int num_mesa;
    public boolean confirmado = false;

    public Pedido(Prato prato, int num_mesa) {
        this.prato = prato;
        this.num_mesa = num_mesa;
    }

    public void mostrarPedido(){
        System.out.println("Prato: " + prato.nome);
        System.out.println("Mesa: " + num_mesa);
        System.out.println("Confirmado: " + (confirmado ? "Sim" : "Não"));
    }
}
