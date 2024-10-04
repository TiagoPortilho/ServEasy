package classes;
import java.util.ArrayList;
import java.util.List;

public class Cardapio {
    public List<Prato> pratos = new ArrayList<>();

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

    public Prato selectList(int j){
        return pratos.get(j);
    }
}
