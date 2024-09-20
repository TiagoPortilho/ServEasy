package classes;
import java.util.List;

public class Cardapio {
    List<Prato> pratos;

    public void mostrarCardapio(){
        int i = 0;
        for (Prato _prato : pratos){
            System.out.println("\nPrato " + i + ":");
            _prato.mostrarPrato();
            System.out.println(" ");
            i++;
        }
    }

    public void addList(Prato _prato){
        pratos.add(_prato);
    }

    public void removeList(int i){
        pratos.remove(i);
    }
}
