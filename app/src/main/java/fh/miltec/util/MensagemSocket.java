package fh.miltec.util;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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

    public void enviar(){
        new Thread(new Runnable()
        {
            public void run() {
                try {
                    Socket soc = new Socket("192.168.0.50", 11000);
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
