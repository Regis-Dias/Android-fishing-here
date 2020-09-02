package com.fh.miltec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fh.miltec.dao.MensagemDao;
import fh.miltec.model.Mensagem;
import fh.miltec.util.MensagemSocket;


public class MensagemActivity extends AppCompatActivity {


    private static final String NOME_PREFERENCE = "ActivityPreferenciaUsuario";
    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        prepararListaMensagens();
        fecharSistema();

        super.onResume();
    }

    private void prepararListaMensagens() {
        TypedArray images = getResources().obtainTypedArray(R.array.random_images_mensagem);
        ListView lista = (ListView) findViewById(R.id.lvMensagem);
        final ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>(new MensagemDao(getBaseContext()).consultaMensagem());
        AdapterMensagem adm = new AdapterMensagem(getBaseContext(), mensagens, images );

        lista.setAdapter(adm);

        //persistencia das credenciais do usuairo logado.
        final SharedPreferences prefs = getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                enviarMensagem(String.format("%02d", prefs.getInt("id", 0)) +
                        String.format("%02d", mensagens.get(position).getId()), true);

                //Toast.makeText(getApplicationContext(), "Mensagem: " + mensagens.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ArrayList<String> preencherDados() {

        List<Mensagem> mensagens = new MensagemDao(getBaseContext()).consultaMensagem();
        ArrayList<String> dados = new ArrayList<String>();

        for (Mensagem m : mensagens)
            dados.add(String.valueOf(m.getId()) + " - " + m.getMensagem());

        return dados;
    }


    private void enviarMensagem(String mensagem, Boolean feedback) {
        try {
            MensagemSocket ms = new MensagemSocket(mensagem);
            ms.enviar(getBaseContext());
            if (ms.getErro()) {
                if(feedback)
                 alerta("Erro ao enviar a mensagem.");
            }
            else {
                if(feedback)
                alerta("Mensagem enviada com sucesso.");
            }
        } catch (Exception e) {
            //e.printStackTrace();

        }
    }

    private void alerta(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        Configuracao conf = new Configuracao(getBaseContext());
        conf.lerPrefereciasConfiguracao();
        final int delay = conf.intervalo * 1000;

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                enviarMensagem(String.format("%02d", getBaseContext().getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).getInt("id", 0))
                        + "02",false);
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    private void fecharSistema() {

        Button btFechar = (Button) findViewById(R.id.btnFechar);
        btFechar.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
              /*  Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(myIntent);*/
                finish();
               // System.exit(0);
            }
        });
    }

}