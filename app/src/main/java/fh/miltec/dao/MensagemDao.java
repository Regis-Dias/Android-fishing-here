package fh.miltec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fh.miltec.Conexao;

import java.util.ArrayList;
import java.util.List;

import fh.miltec.model.Mensagem;

public class MensagemDao {


    private Conexao conexao;
    private SQLiteDatabase banco;

    public MensagemDao(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }


    public boolean incluirMensagem(Mensagem mensagem){

        ContentValues contentValues = new ContentValues();
        contentValues.put("cd_mensagem", mensagem.getId());
        contentValues.put("ds_mensagem", mensagem.getMensagem());


        long result = banco.insert("tb_mensagem", null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean alterarMensagem(Mensagem mensagem){

        ContentValues contentValues = new ContentValues();
        contentValues.put("cd_mensagem", mensagem.getId());
        contentValues.put("ds_mensagem", mensagem.getMensagem());


        long result = banco.update("tb_mensagem",  contentValues, "cd_mensagem = ?", new String[]{Integer.toString(mensagem.getId())});
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean consultaMensagem(int id){


        try {
            String sql = "select * from tb_mensagem where cd_mensagem = ?";

            String[] chave = new String[]{Integer.toString(id)};

            Cursor cursor = banco.rawQuery(sql, chave);

            return cursor.getCount() > 0;
        }
        catch (Exception ex){

            Log.w("mensagem", ex.getMessage());
            return false;

        }

    }

    public boolean consultaMensagem(Mensagem mensagem){

        Cursor cursor = banco.rawQuery("select * from tb_mensagem where cd_mensagem = ? and ds_mensagem = ?", new String[]{mensagem.getId().toString(), mensagem.getMensagem()});
        return cursor.getCount() > 0;
    }


    public List<Mensagem> consultaMensagem() {
        List<Mensagem> list =  new ArrayList<>();
        Cursor c = banco.rawQuery("select * from tb_mensagem" , null);
        while (c.moveToNext()) {
            Mensagem usu = new Mensagem();
            usu.setId(c.getInt(c. getColumnIndex("cd_mensagem")));
            usu.setMensagem(c.getString(c.getColumnIndex("ds_mensagem")));

            list.add(usu);
        }
        return list;
    }
}
