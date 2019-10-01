package com.example.smartglove;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Activity extends SairSystem {

    private TextView txt_irCadastro;
    private Button btnLogin;
    private EditText edtEmailLogin, edtSenhaLogin;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String emailLogin, senhaLogin;
    private boolean validarEmail = false, validarCampos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        edtEmailLogin = (EditText)findViewById(R.id.id_edtEmailLogin);
        edtSenhaLogin = (EditText)findViewById(R.id.id_edtSenhaLogin);
        txt_irCadastro = (TextView) findViewById(R.id.irCadastro);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CamposLogin();
                if(validarCampos == true){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        txt_irCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Cadastro_Activity.class));
            }
        });
    }

    private void CamposLogin(){
        emailLogin = edtEmailLogin.getText().toString().trim();
        senhaLogin = edtSenhaLogin.getText().toString().trim();

        if (edtEmailLogin.getText().toString().trim().matches(emailPattern)) {
            validarEmail = true;
        }
        if (TextUtils.isEmpty(emailLogin) || emailLogin.length() > 50 || !validarEmail) {
            edtEmailLogin.setError("email incorreto");
            edtEmailLogin.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(senhaLogin) || senhaLogin.length() < 8 || senhaLogin.length() > 30) {
            edtSenhaLogin.setError("Senha Incorreta");
            edtSenhaLogin.requestFocus();
            return;
        }
        validarCampos = true;
    }

}

