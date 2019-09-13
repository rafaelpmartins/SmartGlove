package com.example.smartglove;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Avalicao_Activity extends AppCompatActivity {

    private Button btnRatingBar;
    private Button btnCompartilhar;
    private TextView txtRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avalicao_layout);

        btnRatingBar = (Button) findViewById(R.id.id_BtnRatingBar);
        btnCompartilhar = (Button) findViewById(R.id.id_btnCompartilhar);
        txtRatingBar = (TextView) findViewById(R.id.id_TxtRatingBar);

        btnRatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withRatingBar();
            }
        });

        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String texto = "https://github.com/rafaelzero1/SmartGlove";
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private void withRatingBar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setTitle("Avalie");
        View dialogLayout = inflater.inflate(R.layout.rating_bar, null);
        final android.widget.RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtRatingBar.setText("Obrigado por nos avaliar com " + ratingBar.getRating() + " estrelas");
            }
        });
        builder.setNegativeButton("Mais Tarde", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Aguardaremos, aproveite a aplicação", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Nunca", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "É uma pena, obrigado pelo seu tempo", Toast.LENGTH_SHORT).show();
                btnRatingBar.setEnabled(false);
            }
        });
        builder.show();
    }

}
