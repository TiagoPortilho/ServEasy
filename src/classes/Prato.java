package classes;

public class Prato {
    public String nome;
    public float preco;
    public String descricao;

    public Prato(String nome, float preco, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }

    public void mostrarPrato(){
        System.out.println("Nome: " + nome);
        System.out.println("Preço: R$" + preco);
        System.out.println("Descrição: " + descricao);
    }
}
