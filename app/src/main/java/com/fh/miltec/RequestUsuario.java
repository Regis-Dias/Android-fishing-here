package com.fh.miltec;

import java.util.List;

import fh.miltec.model.Usuario;


public class RequestUsuario {

    private List<Usuario> usuarioList;
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }
    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

}
