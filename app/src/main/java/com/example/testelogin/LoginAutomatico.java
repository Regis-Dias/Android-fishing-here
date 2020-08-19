package com.example.testelogin;

import android.content.Context;
import android.content.SharedPreferences;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fh.miltec.dao.UsuarioDao;
import fh.miltec.model.Usuario;

import static android.content.Context.MODE_PRIVATE;


public class LoginAutomatico {

    public static final String NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";
    private final Context context;

    public LoginAutomatico(Context co) {
        context = co;
    }


    //grava as preferencias de login do usuario.
    public void gravaLoginAutomaticoPreferencia(Usuario usuarioAtual) {

        SharedPreferences.Editor editor = context.getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();
        editor.putInt("id", usuarioAtual.getId());
        editor.putString("nome", usuarioAtual.getNome());
        editor.putString("login", usuarioAtual.getNm_login());
        editor.putString("senha", usuarioAtual.getDs_senha());
        editor.commit();
    }


    //recupera as preferencias de login do usuario armazenadas
    public boolean recuperaLoginAutomaticoPreferencia() {
        SharedPreferences prefs = context.getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        String login = prefs.getString("login", null);
        String senha = prefs.getString("senha", null);
        if (login != null && senha != null) {
            // existe configuração salvar
            return true;
        } else {
            // não existe configuração salvar
            return false;
        }
    }


    public boolean autenticarUsuario(String usuario, String senha) {

        String senhaCriptografada = new Criptografia().criptografarSenha(senha);
        Usuario usuarioAtual = new Usuario(usuario, senhaCriptografada);
        UsuarioDao dao = new UsuarioDao(context);
        usuarioAtual = dao.validarUsuarioSenha(usuario, senhaCriptografada);

        if (usuarioAtual.getId() == 0) { // not foud
            return false;
        } else {
            //gravar as preferencias do usuario.
            gravaLoginAutomaticoPreferencia(usuarioAtual);
            return true;
        }

    }
}
