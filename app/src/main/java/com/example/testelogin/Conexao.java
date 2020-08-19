package com.example.testelogin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexao extends SQLiteOpenHelper {

    private static final String name = "fishing.db";
    private static final int versao = 1;


    public Conexao(@Nullable Context context) {
        super(context, name, null, versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabelaUsuario(db);
        criarTabelaMensagem(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        criarTabelaUsuario(db);
        criarTabelaMensagem(db);
    }


    public void criarTabelaUsuario(SQLiteDatabase db) {

        String queryCreate ="CREATE TABLE IF NOT EXISTS tb_usuario(" +
                " cd_usuario integer primary key," +
                " nm_usuario varchar(100) not NULL, " +
                " nm_login varchar(100) not NULL, " +
                " ds_senha varchar(100) not NULL )";

        db.execSQL(queryCreate);
    }

    private void criarTabelaMensagem(SQLiteDatabase db) {
        String queryCreate ="CREATE TABLE IF NOT EXISTS tb_mensagem(" +
                " cd_mensagem integer primary key," +
                " ds_mensagem varchar(100) not NULL)";

        db.execSQL(queryCreate);
    }
}
