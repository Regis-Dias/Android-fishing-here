package com.fh.miltec;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        gravarPrefereciasConfiguracao();

        Configuracao conf = new Configuracao(getBaseContext());

        if(conf.existeConfiguracao()) {
            conf.lerPrefereciasConfiguracao();

            TextView ip = findViewById(R.id.txtIpServidor);
            ip.setText(conf.ip);
            TextView porta = findViewById(R.id.txtPorta);
            porta.setText(conf.porta.toString());
            TextView intervalo = findViewById(R.id.txtTemporizadorMensagemServidor);
            intervalo.setText(conf.intervalo.toString());
        }

    }


    private void gravarPrefereciasConfiguracao() {
        Button btPreferenciasConfiguracao = (Button) findViewById(R.id.btnSalvaPreferenciasConfiguracao);
        btPreferenciasConfiguracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = ((TextView) findViewById(R.id.txtIpServidor)).getText().toString();
                int port = Integer.parseInt(((TextView) findViewById(R.id.txtPorta)).getText().toString());
                int intervalo  = Integer.parseInt(((TextView) findViewById(R.id.txtTemporizadorMensagemServidor)).getText().toString());

                Configuracao conf = new Configuracao(ip, port, intervalo, getBaseContext());
                conf.gravaConfiguracoes();
            }
        });
    }

}