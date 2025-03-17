package serveasy.model;

import java.util.List;

public class Cardapio {
    private List<Prato> pratos;

    public Cardapio(List<Prato> pratos) {
        this.pratos = pratos;
    }

    public List<Prato> getPratos() {
        return pratos;
    }

    public void setPratos(List<Prato> pratos) {
        this.pratos = pratos;
    }

    public void adicionarPrato(Prato prato) {
        this.pratos.add(prato);
    }

    public void removerPrato(Prato prato) {
        this.pratos.remove(prato);
    }
}
