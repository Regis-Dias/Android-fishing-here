package fh.miltec.parsejson;

import com.fh.miltec.RequestUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import fh.miltec.model.Usuario;

public class parseJSonUsuario {

    public static List<Usuario> parse(ObjectMapper objectMapper , String json) {

        RequestUsuario rq = new RequestUsuario();
        try {
            rq = objectMapper.readValue( json, RequestUsuario.class);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return rq.getUsuarioList();
    }
}

