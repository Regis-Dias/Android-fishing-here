package com.fh.miltec;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;


public class Configuracao {

   private static final String NOME_PREFERENCE = "ActivityPreferencias";

    private  Context context;
    public String ip;
    public Integer porta;
    public Integer intervalo;
    public String urlUsuario;
    public String urlMensagem;

   public Configuracao(String ip, int port, int intervalo, String url_Usuario, String url_Mensagem, Context co)
   {
      this.ip = ip;
      this.porta = port;
      this.intervalo = intervalo;
      this.urlUsuario = url_Usuario;
      this.urlMensagem = url_Mensagem;
      this.context = co;
   }


    public Configuracao(Context co) {
        this.context = co;
    }

    public void gravaConfiguracoes() {

        SharedPreferences.Editor editor =  context.getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("ip", ip );
        editor.putInt("porta", porta );
        editor.putInt("intervalo", intervalo );
        editor.putString("urlUsuario", urlUsuario);
        editor.putString("urlMensagem", urlMensagem);
        editor.commit();

    }

    public void lerPrefereciasConfiguracao(){

        SharedPreferences prefs = context.getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);

        this.ip = prefs.getString("ip", "");
        this.porta = prefs.getInt("porta", 0);
        this.intervalo = prefs.getInt("intervalo", 0);
        this.urlUsuario = prefs.getString("urlUsuario", "");
        this.urlMensagem = prefs.getString("urlMensagem", "");
    }


    public boolean existeConfiguracao(){

        File f = new File("/data/data/com.fh.miltec/shared_prefs/ActivityPreferencias.xml");
        if (f.exists())
          return true;
        else
            return false;
    }



}
