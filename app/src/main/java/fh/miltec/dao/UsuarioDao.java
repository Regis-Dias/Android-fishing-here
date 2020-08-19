package fh.miltec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testelogin.Conexao;

import java.util.ArrayList;
import java.util.List;

import fh.miltec.model.Usuario;

public class UsuarioDao {


    private Conexao conexao;
    private SQLiteDatabase banco;

    public UsuarioDao(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public Usuario validarUsuarioSenha(String login, String senha){

        Usuario u = new Usuario() ;
        Cursor cursor = banco.rawQuery("select cd_usuario, nm_usuario from tb_usuario where nm_login = ? and ds_senha = ?", new String[]{login,senha});
        if ( cursor.moveToNext()){
            u.setId( cursor.getInt(0));
            u.setNome( cursor.getString(1));
            //senha e login estavam comentados eu Regis descomentei 11/08/2020
            u.setDs_senha(senha);
            u.setNm_login(login);
            cursor.close();
            return u;
        } else {
            u.setId( 0 );
            return u;
        }

    }


    public boolean incluirUsuario(Usuario usuario){

        ContentValues contentValues = new ContentValues();
        contentValues.put("cd_usuario", usuario.getId());
        contentValues.put("nm_usuario", usuario.getNome());
        contentValues.put("nm_login", usuario.getNm_login());
        contentValues.put("ds_senha", usuario.getDs_senha());

        long result = banco.insert("tb_usuario", null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean alterarUsuario(Usuario usuario){

        ContentValues contentValues = new ContentValues();
        contentValues.put("cd_usuario", usuario.getId());
        contentValues.put("nm_usuario", usuario.getNome());
        contentValues.put("nm_login", usuario.getNm_login());
        contentValues.put("ds_senha", usuario.getDs_senha());

        long result = banco.update("tb_usuario",  contentValues, "cd_usuario = ?", new String[]{Integer.toString(usuario.getId())});
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean consultaUsuario(int id){

        Cursor cursor = banco.rawQuery("select * from tb_usuario where cd_usuario = ?", new String[]{Integer.toString(id)});
        return cursor.getCount() > 0;
    }

    public boolean consultaUsuario(Usuario usuario){

        Cursor cursor = banco.rawQuery("select * from tb_usuario where cd_usuario = ? and nm_usuario = ?", new String[]{usuario.getId().toString(), usuario.getNome()});
        return cursor.getCount() > 0;
    }


    public List<Usuario> consultaUsuario() {
       List<Usuario> list =  new ArrayList<>();
        Cursor c = banco.rawQuery("select * from tb_usuario" , null);
        while (c.moveToNext()) {
            Usuario usu = new Usuario();
            usu.setId(c.getColumnIndex("cd_usuario"));
            usu.setNome(c.getString(c.getColumnIndex("nm_usuario")));
            usu.setNm_login(c.getString(c.getColumnIndex("nm_login")));
            usu.setDs_senha(c.getString( c.getColumnIndex("ds_senha")));

            list.add(usu);
        }
        return list;
    }

}
