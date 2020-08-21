package com.example.testelogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        prepararListaMensagens();
        super.onResume();
    }

    private void prepararListaMensagens() {

        ListView lista = (ListView) findViewById(R.id.lvMensagem);

        final ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>(new MensagemDao(getBaseContext()).consultaMensagem());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, preencherDados());
        lista.setAdapter(arrayAdapter);

        //persistencia das credenciais do usuairo logado.
        final SharedPreferences prefs = getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                enviarMensagem("Mensagem: " + String.format("%02d", mensagens.get(position).getId()) +
                        " Usuario: " + String.format("%02d", prefs.getInt("id", 0)));

                //Toast.makeText(getApplicationContext(), "Mensagem: " + mensagens.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> preencherDados() {

        List<Mensagem> mensagens =  new MensagemDao(getBaseContext()).consultaMensagem();
        ArrayList<String> dados = new ArrayList<String>();

        for(Mensagem m : mensagens)
            dados.add( String.valueOf(m.getId()) + " - " + m.getMensagem());

        return dados;
    }


    private void enviarMensagem(String mensagem) {
        try {
            MensagemSocket ms = new MensagemSocket(mensagem);
            ms.enviar();
            Toast.makeText(null, "Mensagem enviada", Toast.LENGTH_LONG);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {

            public void run() {
                handler.postDelayed(runnable, delay);
                enviarMensagem("Mensagem: " + String.format("%02d", new ArrayList<Mensagem>(new MensagemDao(getBaseContext()).consultaMensagem()).get(0).getId()) +
                        " Usuario: " + String.format("%02d",  getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).getInt("id", 0)));


               /* Toast.makeText(MainActivity.this, "This method is run every 10 seconds",
                        Toast.LENGTH_SHORT).show();*/

            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }


}