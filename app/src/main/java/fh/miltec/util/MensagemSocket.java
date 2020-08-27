package fh.miltec.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fh.miltec.Configuracao;
import com.fh.miltec.ConfiguracaoActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.content.Context.MODE_PRIVATE;

public class MensagemSocket {

    private String mensagem;



    public MensagemSocket( String mensagem){
        this.setMensagem(mensagem);

    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void enviar(Context co){

        final Context context = co;

        new Thread(new Runnable()
        {
            public void run() {
                try {

                    Configuracao conf = new Configuracao(context);
                    conf.lerPrefereciasConfiguracao();

                    Socket soc = new Socket(conf.ip, conf.porta);
                    PrintWriter writer = new PrintWriter(soc.getOutputStream());
                    writer.write( mensagem.concat("<EOF>") );
                    writer.flush();
                    writer.close();
                    soc.close();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
