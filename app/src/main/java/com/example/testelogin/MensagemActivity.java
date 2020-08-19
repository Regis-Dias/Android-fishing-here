package com.example.testelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fh.miltec.dao.MensagemDao;
import fh.miltec.model.Mensagem;
import fh.miltec.model.Usuario;
import fh.miltec.util.MensagemSocket;


public class MensagemActivity extends AppCompatActivity {


    private static final String NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        prepararListaMensagens();
    }

    private void prepararListaMensagens() {

        ListView lista = (ListView) findViewById(R.id.lvMensagem);

        final ArrayList<String> mensagens= preencherDados();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mensagens);
        lista.setAdapter(arrayAdapter);

        //persistencia das credenciais do usuairo logado.
        final SharedPreferences prefs = getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);

        final String dadosPersistidos  =  String.valueOf(prefs.getInt("id", 0)) + " - " + prefs.getString("nome", null) ;

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enviarMensagem("Mensagem: " + mensagens.get(position) + " Usuario: " + dadosPersistidos);

                //Toast.makeText(getApplicationContext(), "Mensagem: " + mensagens.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> preencherDados() {

        List<Mensagem> listaMensagem = new MensagemDao(getBaseContext()).consultaMensagem();
        ArrayList<String> dados = new ArrayList<String>();

        for (Mensagem m : listaMensagem) {
            dados.add(m.getId().toString() + " " + m.getMensagem());
        }
        return dados;
    }


    private void enviarMensagem(String mensagem) {
        try {
            MensagemSocket ms = new MensagemSocket(mensagem);
            ms.enviar();
            Toast.makeText(null, "Mensagem enviada", Toast.LENGTH_LONG);

        } catch( Exception e){
            e.printStackTrace();
        }
    }


}