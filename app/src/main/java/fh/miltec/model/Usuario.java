package fh.miltec.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Usuario {

    @JsonProperty("cd_usuario")
    private Integer id;

    @JsonProperty("nm_usuario")
    private String nome;

    private String nm_login;
    private String ds_senha;

    public String getNm_login() {
        return nm_login;
    }

    public void setNm_login(String nm_login) {
        this.nm_login = nm_login;
    }

    public String getDs_senha() {
        return ds_senha;
    }

    public void setDs_senha(String ds_senha) {
        this.ds_senha = ds_senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario(String usuario, String senhaCriptografada) {
        this.nm_login = usuario;
        this.ds_senha = senhaCriptografada;
    }

    public Usuario(){}


}