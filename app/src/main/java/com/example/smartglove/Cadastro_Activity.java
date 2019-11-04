package com.example.smartglove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cadastro_Activity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private TextView txt_irLogin;
    private Button btnEsporte, btnCadastrar;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String item, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText edtNome, edtPeso, edtEmail, edtSenha, edtEmailLogin, edtSenhaLogin;
    private boolean validarEmail = false, validarCampos = false, validarPeso = false;
    private String nome, peso, email, senha, esporte, EmailLogin;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_layout);

        edtNome = (EditText) findViewById(R.id.id_edtNome);
        edtPeso = (EditText) findViewById(R.id.id_edtPeso);
        edtEmail = (EditText) findViewById(R.id.id_edtEmail);
        txt_irLogin = (TextView) findViewById(R.id.id_txtLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnEsporte = (Button) findViewById(R.id.id_btnEsporte);
        edtSenha = (EditText) findViewById(R.id.id_edtSenha);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campos();
                if (validarCampos == true) {
                    createUser();
                }
            }
        });

        txt_irLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(v);
            }
        });

        listItems = getResources().getStringArray(R.array.esporte_item);
        checkedItems = new boolean[listItems.length];

        btnEsporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Cadastro_Activity.this, R.style.AlertDialogTheme);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        btnEsporte.setText(item);
                        if (btnEsporte.getText() == "") {
                            btnEsporte.setText("Adicionar Esporte(s)");
                        }
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            btnEsporte.setText("Adicionar Esporte(s)");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    private void createUser() {
        nome = edtNome.getText().toString().trim();
        peso = edtPeso.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        senha = edtSenha.getText().toString().trim();
        esporte = item.trim();

        HashMap<String, String> params = new HashMap<>();
        params.put("nome", nome);
        params.put("peso", peso);
        params.put("email", email);
        params.put("senha", senha);
        params.put("esporte", esporte);

        user.setEmail(email);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    if (object.getString("message").equals("cadastro realizado com sucesso")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }

                    if (object.getString("message").equals("logado")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Sem conexão Internet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void campos() {
        nome = edtNome.getText().toString().trim();
        peso = edtPeso.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        senha = edtSenha.getText().toString().trim();

        if (edtEmail.getText().toString().trim().matches(emailPattern)) {
            validarEmail = true;
        }

        if (isValidweight(edtPeso.getText().toString().trim())) {
            validarPeso = true;
        }

        if (TextUtils.isEmpty(nome) || nome.length() > 20 || nome.length() < 3) {
            edtNome.setError("Por favor insira um nome válido");
            edtNome.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(peso) || peso.length() > 3 || peso.length() < 2 || validarPeso == false) {
            edtPeso.setError("Por favor insira um peso válido");
            edtPeso.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || email.length() > 40 || !validarEmail) {
            edtEmail.setError("Por favor insira um email válido");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(senha) || senha.length() < 8 || senha.length() > 30) {
            edtSenha.setError("A senha deve ter no minimo 8 caracteres");
            edtSenha.requestFocus();
            return;
        }
        if (btnEsporte.getText().equals("Adicionar Esporte(s)")) {
            btnEsporte.setError("Por favor insira pelo menos um esporte");
            btnEsporte.requestFocusFromTouch();
            return;
        }
        validarCampos = true;
    }

    public void showAlertDialogButtonClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Email & Senha");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_login, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtEmailLogin = customLayout.findViewById(R.id.id_edtEmailLogin);
                edtSenhaLogin = customLayout.findViewById(R.id.id_edtSenhaLogin);

                EmailLogin = edtEmailLogin.getText().toString().trim();
                String SenhaLogin = edtSenhaLogin.getText().toString().trim();

                if (TextUtils.isEmpty(EmailLogin)) {
                    Toast.makeText(getApplicationContext(), "Email vazio", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(SenhaLogin)) {
                    Toast.makeText(getApplicationContext(), "Senha vazio", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", EmailLogin);
                    params.put("senha", SenhaLogin);

                    user.setEmail(EmailLogin);

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_LOGIN_USER, params, CODE_POST_REQUEST);
                    request.execute();
                }
            }
        });

        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isValidweight(final String weight) {

        Pattern pattern;
        Matcher matcher;

        final String WEIGHT_PATTERN = "^([1-9][0-9]?[0-9])$";

        pattern = Pattern.compile(WEIGHT_PATTERN);
        matcher = pattern.matcher(weight);

        return matcher.matches();
    }
}
