package com.example.testelogin;

import java.util.List;

import fh.miltec.model.Mensagem;

public class RequestMensagem {

    private List<Mensagem> mensagemList;
    public List<Mensagem> getMensagemList() {
        return mensagemList;
    }
    public void setMensagemList(List<Mensagem>  mensagemList) {
        this.mensagemList = mensagemList;
    }

}
