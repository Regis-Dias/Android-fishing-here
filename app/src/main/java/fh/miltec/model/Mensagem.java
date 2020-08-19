package fh.miltec.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Mensagem {

    @JsonProperty("cd_mensagem")
    private Integer id;

    @JsonProperty("ds_mensagem")
    private String mensagem;

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}



