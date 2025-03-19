package serveasy.model;

import java.util.List;

public class Mesa {
    private int id;                   
    private int numero;               
    private boolean ocupada;          
    private List<Pedido> pedidos;     
    private float valorGasto;         
    private boolean pago;             


    public Mesa(int id, int numero, boolean ocupada, float valorGasto, boolean pago){
        this.id = id;
        this.numero = numero;
        this.ocupada = false;
        this.valorGasto = 0.0f;
        this.pago = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public float getValorGasto() {
        return valorGasto;
    }

    public void setValorGasto(float valorGasto) {
        this.valorGasto = valorGasto;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }
}