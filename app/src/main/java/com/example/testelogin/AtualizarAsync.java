package com.example.testelogin;
import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import fh.miltec.model.Usuario;

public class AtualizarAsync extends AsyncTask<Void,Void, List<Usuario>> {

        public List<Usuario> usuarioList;
        private final ObjectMapper objectMapper;
        private final Context context;

        public AtualizarAsync( ObjectMapper om, Context co) {
            this.context = co;
            this.objectMapper = om;

        }
        @Override
        protected void onPreExecute(){
           // Toast.makeText(null,"Atualizando Informacoes do Servidor",Toast.LENGTH_LONG).show();
        }

        @Override
        protected List<Usuario> doInBackground(Void... params) {

            AtualizarTabelas at = new AtualizarTabelas(objectMapper, context);

            at.processarAtualizacaoMensagem("http://fishingheresmal.azurewebsites.net/api/mensagem");

           return at.processarAtualizacaoUsuario("http://fishingheresmal.azurewebsites.net/api/usuario");
        }

        @Override
        protected void onPostExecute(List<Usuario> usuarioList){
             this.usuarioList = usuarioList;
        }

}
