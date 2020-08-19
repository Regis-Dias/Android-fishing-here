package com.example.testelogin;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fh.miltec.dao.MensagemDao;
import fh.miltec.dao.UsuarioDao;
import fh.miltec.model.Mensagem;
import fh.miltec.model.Usuario;
import fh.miltec.parsejson.parseJSonMensagem;
import fh.miltec.parsejson.parseJSonUsuario;

public class AtualizarTabelas {

    private final Context context;
    private final ObjectMapper objectMapper;

    public AtualizarTabelas(ObjectMapper om, Context co){
        context = co;
        objectMapper = om;
    }



    public List<Usuario> processarAtualizacaoUsuario(String url) {
        String jsonString = getApi(url);

        List<Usuario> usuarioList = parseJSonUsuario.parse(objectMapper, jsonString),
                      listaUsuario = new ArrayList<Usuario>();

        boolean temRegistro = true, temMesmoRegistro = false,
                registroIncluido = false,  alterouRegistro = false;

        UsuarioDao udao = new UsuarioDao(context) ;

        // persistir no banco
        for(Usuario usuario: usuarioList){

            temRegistro = udao.consultaUsuario(usuario.getId());

            if(temRegistro){
                temMesmoRegistro = udao.consultaUsuario(usuario);

                if(!temMesmoRegistro) {
                    alterouRegistro =  udao.alterarUsuario(usuario);

                    if(!alterouRegistro)
                        Log.w("usuario", "Dado do usuário " + usuario.getNome()  + " nao foi alterado.");
                        //Toast.makeText(this.context, "Dado do usuário " + usuario.getNome()  + " nao foi alterado.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                registroIncluido = udao.incluirUsuario(usuario);
                if(!registroIncluido)
                    Log.w("usuario", "Dado do usuário " + usuario.getNome()  + " nao foi incluido.");
            }

            registroIncluido = false;
            temRegistro = true;
            temMesmoRegistro = false;
            alterouRegistro = false;
        }

        listaUsuario = udao.consultaUsuario();
        printListUsuairo(listaUsuario);

        return  listaUsuario;
    }

    public void processarAtualizacaoMensagem( String url) {
        String jsonString = getApi(url);

        List<Mensagem> mesageList = parseJSonMensagem.parse(objectMapper, jsonString),
                listaMensagem = new ArrayList<Mensagem>();

        boolean temRegistro = true, temMesmoRegistro = false,
                registroIncluido = false,  alterouRegistro = false;

        MensagemDao mdao = new MensagemDao(context) ;

        // persistir no banco
        for(Mensagem mensagem: mesageList){

            temRegistro = mdao.consultaMensagem(mensagem.getId());

            if(temRegistro){
                temMesmoRegistro = mdao.consultaMensagem(mensagem);

                if(!temMesmoRegistro) {
                    alterouRegistro =  mdao.alterarMensagem(mensagem);

                    if(!alterouRegistro)
                        Log.w("mensagem", "Dado da mensagem " +mensagem.getMensagem()  + " nao foi alterado.");
                }
            }
            else{
                registroIncluido = mdao.incluirMensagem(mensagem);
                if(!registroIncluido)
                    Log.w("mensagem", "Dado da mensagem " + mensagem.getMensagem()  + " nao foi incluido.");
            }

            registroIncluido = false;
            temRegistro = true;
            temMesmoRegistro = false;
            alterouRegistro = false;
        }

        listaMensagem = mdao.consultaMensagem();
        printListMensagem(listaMensagem);

    }

    private void printListMensagem(List<Mensagem> listaMensagem) {
        for( Mensagem m: listaMensagem ){
            System.out.println(m);
        }

    }


    /*tete de impressão da lista*/
    public void printListUsuairo(List<Usuario> listaUsuario) {

        for( Usuario u: listaUsuario ){
            System.out.println(u);
        }
    }

    public String getApi(String url){
        String retorno = "";
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(30000);
            conexao.setConnectTimeout(30000);

            conexao.connect();

            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
