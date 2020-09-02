package fh.miltec.util;

import android.content.Context;

import com.fh.miltec.Configuracao;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MensagemSocket {

    private String mensagem;
    private Boolean erro;

    public MensagemSocket( String mensagem){
        this.setMensagem(mensagem);
        this.setErro(false);

    }

    public Boolean getErro() {
        return erro;
    }

    public void setErro(Boolean erro) {
        this.erro = erro;
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
                    if(soc.isConnected()) {
                        writer.write(mensagem.concat("<EOF>"));
                        writer.flush();
                        writer.close();
                        soc.close();
                        setErro(false);
                    }else {
                        setErro(true);
                    }
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    setErro(true);
                    //e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    setErro(true);
                }
            }
        }).start();
    }
}
