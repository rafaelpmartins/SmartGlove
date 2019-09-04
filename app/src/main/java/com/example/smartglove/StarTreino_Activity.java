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
    }
}
