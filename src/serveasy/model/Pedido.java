package serveasy.model;

public class Pedido {
    private Prato prato;
    private Mesa numMesa;
    private boolean confirmado;

    public Pedido(Prato prato, Mesa numMesa) {
        this.prato = prato;
        this.numMesa = numMesa;
        this.confirmado = false;
    }

    public Prato getPrato() {
        return prato;
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
    }

    public Mesa getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(Mesa numMesa) {
        this.numMesa = numMesa;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}