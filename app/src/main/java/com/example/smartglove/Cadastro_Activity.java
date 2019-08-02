package com.example.smartglove;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import static android.view.View.GONE;

public class Cadastro_Activity extends AppCompatActivity {

    Button botaoNasc;
    DatePicker dataNasc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_layout);

        botaoNasc = (Button) findViewById(R.id.botaoNasc);

        botaoNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataNasc = (DatePicker) findViewById(R.id.dataNasc);
                if (dataNasc.getVisibility() == GONE) {
                    dataNasc.setVisibility(View.VISIBLE);
                    botaoNasc.setText("Confirmar");
                } else if (botaoNasc.getText() == "Confirmar") {
                    botaoNasc.setText("Trocar");
                    dataNasc.setVisibility(View.GONE);
                }
            }
        });
    }
}
