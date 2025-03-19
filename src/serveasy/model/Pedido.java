package serveasy.model;

public class Pedido {
    private int id;
    private int id_prato;
    private int id_mesa;
    private boolean confirmado;
    private String nomePrato;  
    
    
    public Pedido(int id_prato, int id_mesa) {
        this.id_prato = id_prato;
        this.id_mesa = id_mesa;
    }
    
    public Pedido(int id, int id_prato, int id_mesa, boolean confirmado) {
        this.id = id;
        this.id_prato = id_prato;
        this.id_mesa = id_mesa;
        this.confirmado = confirmado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPrato() {
        return id_prato;
    }

    public void setIdPrato(int id_prato) {
        this.id_prato = id_prato;
    }

    public int getIdMesa() {
        return id_mesa;
    }

    public void setIdMesa(int id_mesa) {
        this.id_mesa = id_mesa;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }
}