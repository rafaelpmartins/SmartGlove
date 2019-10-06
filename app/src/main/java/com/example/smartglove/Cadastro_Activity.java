package com.example.smartglove;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cadastro_Activity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    @SuppressLint("StaticFieldLeak")
    private static EditText edtLogin = null;

    private TextView txt_irLogin;
    private Button btnEsporte, btnCadastrar;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String item, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText edtNome, edtEmail, edtSenha;
    private boolean validarEmail = false, validarCampos = false, validarSenha = false;
    private String nome, email, senha, esporte;

    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_layout);

        edtNome = (EditText) findViewById(R.id.id_edtNome);
        edtEmail = (EditText) findViewById(R.id.id_edtEmail);
        txt_irLogin = (TextView) findViewById(R.id.id_txtLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnEsporte = (Button) findViewById(R.id.id_btnEsporte);
        edtSenha = (EditText) findViewById(R.id.id_edtSenha);

        userList = new ArrayList<>();

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
        email = edtEmail.getText().toString().trim();
        senha = edtSenha.getText().toString().trim();
        esporte = item.trim();

        HashMap<String, String> params = new HashMap<>();
        params.put("nome", nome);
        params.put("email", email);
        params.put("senha", senha);
        params.put("esporte", esporte);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readEmail() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_SENHA, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshUserList(JSONArray users) throws JSONException {
        userList.clear();

        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = users.getJSONObject(i);

            userList.add(new User(
                    obj.getString("senha")
            ));

            String login = edtLogin.getText().toString().trim();

            if (obj.getString("senha").equals(login)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Bem vindo, de volta", Toast.LENGTH_SHORT).show();
            }
        }
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
                    refreshUserList(object.getJSONArray("users"));
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
        email = edtEmail.getText().toString().trim();
        senha = edtSenha.getText().toString().trim();

        if (edtEmail.getText().toString().trim().matches(emailPattern)) {
            validarEmail = true;
        }

        if (isValidPassword(edtSenha.getText().toString().trim())) {
            validarSenha = true;
        }

        if (TextUtils.isEmpty(nome) || nome.length() > 20 || nome.length() < 3) {
            edtNome.setError("Por favor insira um nome válido");
            edtNome.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || email.length() > 40 || !validarEmail) {
            edtEmail.setError("Por favor insira um email válido");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(senha) || senha.length() < 8 || senha.length() > 30 || validarSenha == false) {
            edtSenha.setError("A senha deve ter no minimo 8 caracteres, uma letra maiúscula, uma letra minúscula, um número e um caracter especial(@,$,%,&,#)");
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
        builder.setTitle("Senha");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtLogin = customLayout.findViewById(R.id.editText);
                String login = edtLogin.getText().toString().trim();

                if (TextUtils.isEmpty(login)) {
                    Toast.makeText(getApplicationContext(), "Está Vazio", Toast.LENGTH_SHORT).show();
                } else if (login.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Senha invalida", Toast.LENGTH_SHORT).show();
                } else {
                    readEmail();
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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
