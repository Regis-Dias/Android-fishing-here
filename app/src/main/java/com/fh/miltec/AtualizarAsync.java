package com.fh.miltec;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
            Configuracao conf = new Configuracao(context);

            if(conf.existeConfiguracao()) {
                conf.lerPrefereciasConfiguracao();
                at.processarAtualizacaoMensagem(conf.urlMensagem);
               return at.processarAtualizacaoUsuario(conf.urlUsuario);
            }
            else
            {
                Toast.makeText(null, "Configurações nao foram estabelecidas", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Usuario> usuarioList){
             this.usuarioList = usuarioList;
        }

}

//url Mensagem
//"http://fishingheresmal.azurewebsites.net/api/mensagem"
//url Usuario
//"http://fishingheresmal.azurewebsites.net/api/usuario"