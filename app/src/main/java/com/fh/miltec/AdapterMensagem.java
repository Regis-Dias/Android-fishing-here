package com.fh.miltec;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fh.miltec.model.Mensagem;

public class AdapterMensagem extends ArrayAdapter<Mensagem> {

    public Context context;
    public TypedArray imagens;
    public ArrayList<Mensagem> mensagens;

    AdapterMensagem(Context c, ArrayList<Mensagem> msgs, TypedArray imgs){
        super(c, R.layout.activity_item_mensagem, R.id.txt1, msgs);
        this.context = c;
        this.imagens = imgs;
        this.mensagens = msgs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.activity_item_mensagem,parent,false);
        ImageView images = (ImageView) row.findViewById(R.id.icon);
        TextView textoMensagem = (TextView) row.findViewById(R.id.txt1);
        images.setImageResource(imagens.getResourceId(position, - 1));
        textoMensagem.setText(mensagens.get(position).getMensagem());
        return row;
    }

}
