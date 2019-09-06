package com.example.smartglove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StarTreino_Activity extends AppCompatActivity {

    private TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_treino_layout);

        txtTitulo = (TextView) findViewById(R.id.txtIdTitulo);

        Intent intent = getIntent();
        String titulo = intent.getExtras().getString("Titulo");

        txtTitulo.setText(titulo);

//        if (titulo.equals("Aikido")){
//
//        }
//        if (titulo.equals("Boxe")){
//
//        }
//        if (titulo.equals("Caratê")){
//
//        }
//        if (titulo.equals("Jeet Kune Do")){
//
//        }
//        if (titulo.equals("Jiu Jitsu")){
//
//        }
//        if (titulo.equals("Kick Boxing")){
//
//        }
//        if (titulo.equals("Muay Thai")){
//
//        }
//        if (titulo.equals("Wing Chun")){
//
//        }
    }
}
