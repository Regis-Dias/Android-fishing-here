package com.example.testelogin;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import fh.miltec.dao.UsuarioDao;
import fh.miltec.model.Usuario;
import fh.miltec.util.MensagemSocket;

public class MainActivity extends AppCompatActivity {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginAutomatico la = new LoginAutomatico(getBaseContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepararBotaoAcessar();
        prepararBotaoAtualizarTabelas();

        //recuperar login automático.
        la.recuperaLoginAutomaticoPreferencia();

        //prepararBotaoEnviarMensagem();

    }

    private void prepararBotaoAcessar() {
        Button btLogin = (Button) findViewById(R.id.btnLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsuario = ((TextView) findViewById(R.id.txtLogin)).getText().toString();
                String txtSenha = ((TextView) findViewById(R.id.txtPassword)).getText().toString();

               if (validarUsuario(txtUsuario, txtSenha)) {
                   Intent it = new Intent(MainActivity.this, MensagemActivity.class);
                   startActivity(it);
                    alerta("Acesso Concedido...");
                } else {
                    alerta("Credenciais de acesso Inválidas");
                }
            }
        });
    }

    private void prepararBotaoAtualizarTabelas() {
        Button btAtualizar = (Button) findViewById(R.id.btnAtualizarTabelas);
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (atualizarTabela()) {
                    alerta("Tabelas Atualizadas...");
                } else {
                    alerta("Não foi possível atualizar Tabelas");
                }
            }
        });
    }

    private boolean validarUsuario(String usuario, String senha) {

        LoginAutomatico la = new LoginAutomatico(getBaseContext());

        boolean temRefUsuario = la.recuperaLoginAutomaticoPreferencia(),
                autentUsuairo = la.autenticarUsuario(usuario, senha);

        if(!temRefUsuario) {
            if (!autentUsuairo)
                alerta("Usuario ou Senha Inválida");
            else
                alerta("Acesso autorizado..");

            return autentUsuairo;
        }
        else
            return temRefUsuario;

    }



    public boolean atualizarTabela() {

        //AsyncTask<Void, Void, List<Usuario>> execute = (new AtualizarAsync(objectMapper, this)).execute();
        AsyncTask<Void, Void, List<Usuario>> execute = (new AtualizarAsync(objectMapper, this)).execute();

        return true;
    }

    private void alerta(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    // estes metodos foram transferidos para a classe Mensagem Activity.
    // e são acionadio ao clicar a mensagem na lista de mensagens.
    // deixei comentado estes metodos  aguardando decisão do Sidney,
    // para reutilização ou exclusão.
   /*
    private void prepararBotaoEnviarMensagem(){
        Button btMensagem = (Button) findViewById(R.id.btnEnviarMensagem);
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem("0102");
            }
        });
    }

    private void enviarMensagem(String mensagem) {
        try {
            MensagemSocket ms = new MensagemSocket(mensagem);
            ms.enviar();
            Toast.makeText(null, "Mensagem enviada", Toast.LENGTH_LONG);

        } catch( Exception e){
            e.printStackTrace();
        }
    }*/

}
