package serveasy.model;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private int idFuncionario;
    private int idMesa;

    
    public Usuario(int id, String login, String senha, Integer idFuncionario, Integer idMesa) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.idFuncionario = idFuncionario;
        this.idMesa = idMesa;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }
}