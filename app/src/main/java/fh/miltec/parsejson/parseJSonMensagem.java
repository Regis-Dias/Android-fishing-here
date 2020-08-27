package fh.miltec.parsejson;

import com.fh.miltec.RequestMensagem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import fh.miltec.model.Mensagem;

public class parseJSonMensagem {

    public static List<Mensagem> parse(ObjectMapper objectMapper , String json) {

        RequestMensagem rq = new RequestMensagem();
        try {
            rq = objectMapper.readValue( json, RequestMensagem.class);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return rq.getMensagemList();
    }



}
