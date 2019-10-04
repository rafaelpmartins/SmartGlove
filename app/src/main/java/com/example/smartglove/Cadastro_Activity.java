package com.example.smartglove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Cadastro_Activity extends SairSystem {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    private TextView txt_irLogin;
    private Button btnEsporte, btnCadastrar;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String item, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText edtNome, edtEmail;
    private boolean validarEmail = false, validarCampos = false;
    private String nome, email, esporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_layout);

        edtNome = (EditText) findViewById(R.id.id_edtNome);
        edtEmail = (EditText) findViewById(R.id.id_edtEmail);
        txt_irLogin = (TextView) findViewById(R.id.id_txtLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnEsporte = (Button) findViewById(R.id.id_btnEsporte);


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
        esporte = item.trim();

        HashMap<String, String> params = new HashMap<>();
        params.put("nome", nome);
        params.put("email", email);
        params.put("esporte", esporte);

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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Ocorreu algum erro tente mais tarde", Toast.LENGTH_SHORT).show();
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

        if (edtEmail.getText().toString().trim().matches(emailPattern)) {
            validarEmail = true;
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
        if (btnEsporte.getText().equals("Adicionar Esporte(s)")) {
            btnEsporte.setError("Por favor insira pelo menos um esporte");
            btnEsporte.requestFocusFromTouch();
            return;
        }
        validarCampos = true;
    }

    public void showAlertDialogButtonClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Email");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = customLayout.findViewById(R.id.editText);
                String login = editText.getText().toString().trim();
                if (TextUtils.isEmpty(login)) {
                    Toast.makeText(getApplicationContext(), "Está Vazio", Toast.LENGTH_SHORT).show();
                } else {
                    sendDialogDataToActivity(editText.getText().toString().trim());
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void sendDialogDataToActivity(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}
