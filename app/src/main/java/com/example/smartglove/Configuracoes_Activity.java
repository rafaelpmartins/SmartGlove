package com.example.smartglove;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class Configuracoes_Activity extends SairSystem {

    private Button btnEsporteupdate;
    private TextView txtNomeUpdate, txtPesoUpdate, txtEmailUpdate, txtSenhaUpdate;
    private LinearLayout linearNome, linearPeso, linearEmail, linearSenha;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String item, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText edtNomeUpdate, edtPesoUpdate, edtEmailUpdate, edtSenhaUpdate;
    private boolean validarPeso = false, validarEmail = false;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private String recebeEsporte;
    private List<User> userList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes_layout);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        readDats();
        alterUser();

        userList = new ArrayList<>();

        btnEsporteupdate = (Button) findViewById(R.id.id_btnEsporteUpdate);
        txtNomeUpdate = (TextView) findViewById(R.id.id_txtNomeUpdate);
        txtPesoUpdate = (TextView) findViewById(R.id.id_txtPesoUpdate);
        txtEmailUpdate = (TextView) findViewById(R.id.id_txtEmailUpdate);
        txtSenhaUpdate = (TextView) findViewById(R.id.id_txtSenhaUpdate);
        linearNome = (LinearLayout) findViewById(R.id.id_linearNome);
        linearPeso = (LinearLayout) findViewById(R.id.id_linearPeso);
        linearEmail = (LinearLayout) findViewById(R.id.id_linearEmail);
        linearSenha = (LinearLayout) findViewById(R.id.id_linearSenha);

        ToolbarBack();

        linearNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNome(v);
            }
        });

        linearPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPeso(v);
            }
        });

        linearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEmail(v);
            }
        });

        linearSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSenha(v);
            }
        });

        listItems = getResources().getStringArray(R.array.esporte_item);
        checkedItems = new boolean[listItems.length];

        btnEsporteupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Configuracoes_Activity.this, R.style.AlertDialogTheme);
                mBuilder.setTitle("Altere seu(s) esporte(s)");
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
                        btnEsporteupdate.setText(item);
                        if (btnEsporteupdate.getText() == "") {
                            btnEsporteupdate.setText(recebeEsporte);
                            Toast.makeText(getApplicationContext(), "Nada foi alterado", Toast.LENGTH_SHORT).show();
                        } else {
                            btnEsporteupdate.setTextColor(Color.parseColor("#B71C1C"));

                            HashMap<String, String> params = new HashMap<>();
                            params.put("id", String.valueOf(user.getId()));
                            params.put("esporte", item);

                            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ESPORTE, params, CODE_POST_REQUEST);
                            request.execute();
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
                            btnEsporteupdate.setText(recebeEsporte);
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

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
                    refreshUserList(object.getJSONArray("dats"));
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
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

    private void ToolbarBack() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbarBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_dp);
        getSupportActionBar().setTitle("Configurações");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Configuracoes_Activity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void DialogNome(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Alterar nome");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_nome, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtNomeUpdate = customLayout.findViewById(R.id.id_edtNomeUpdate);
                String nome = edtNomeUpdate.getText().toString().trim();
                if (TextUtils.isEmpty(nome) || nome.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Nome invalido", Toast.LENGTH_SHORT).show();
                } else {
                    txtNomeUpdate.setText(nome);
                    txtNomeUpdate.setTextColor(Color.parseColor("#B71C1C"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    params.put("nome", nome);

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_NOME, params, CODE_POST_REQUEST);
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

    public void DialogPeso(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Alterar peso");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_peso, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtPesoUpdate = customLayout.findViewById(R.id.id_edtPesoUpdate);
                String peso = edtPesoUpdate.getText().toString().trim();

                if (isValidweight(edtPesoUpdate.getText().toString().trim())) {
                    validarPeso = true;
                }

                if (TextUtils.isEmpty(peso) || peso.length() > 3 || peso.length() < 2 || validarPeso == false) {
                    Toast.makeText(getApplicationContext(), "Peso invalido", Toast.LENGTH_SHORT).show();
                } else {
                    txtPesoUpdate.setText(peso);
                    txtPesoUpdate.setTextColor(Color.parseColor("#B71C1C"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    params.put("peso", peso);

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_PESO, params, CODE_POST_REQUEST);
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

    public void DialogEmail(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Alterar email");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_email, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtEmailUpdate = customLayout.findViewById(R.id.id_edtEmailUpdate);
                String email = edtEmailUpdate.getText().toString().trim();

                if (edtEmailUpdate.getText().toString().trim().matches(emailPattern)) {
                    validarEmail = true;
                }

                if (TextUtils.isEmpty(email) || email.length() > 40 || !validarEmail) {
                    Toast.makeText(getApplicationContext(), "Email invalido", Toast.LENGTH_SHORT).show();
                } else {
                    txtEmailUpdate.setText(email);
                    txtEmailUpdate.setTextColor(Color.parseColor("#B71C1C"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("id", String.valueOf(user.getId()));

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_EMAIL, params, CODE_POST_REQUEST);
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

    public void DialogSenha(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Alterar senha");

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_senha, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtSenhaUpdate = customLayout.findViewById(R.id.id_edtSenhaUpdate);
                String senha = edtSenhaUpdate.getText().toString().trim();

                if (TextUtils.isEmpty(senha) || senha.length() < 8 || senha.length() > 30) {
                    Toast.makeText(getApplicationContext(), "Senha invalido", Toast.LENGTH_SHORT).show();
                } else {
                    txtSenhaUpdate.setText(senha);
                    txtSenhaUpdate.setTextColor(Color.parseColor("#B71C1C"));

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(user.getId()));
                    params.put("senha", senha);

                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_SENHA, params, CODE_POST_REQUEST);
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

    private void alterUser() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(user.getId()));

        Configuracoes_Activity.PerformNetworkRequest request = new Configuracoes_Activity.PerformNetworkRequest(Api.URL_ALTER_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readDats() {
        Configuracoes_Activity.PerformNetworkRequest request = new Configuracoes_Activity.PerformNetworkRequest(Api.URL_ALTER_USER, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshUserList(JSONArray dats) throws JSONException {
        userList.clear();

        for (int i = 0; i < dats.length(); i++) {
            JSONObject obj = dats.getJSONObject(i);

            user = new User(obj.getInt("id"),
                    obj.getString("nome"),
                    obj.getString("peso"),
                    obj.getString("email"),
                    obj.getString("esporte"));
            userList.add(user);
            recebeEsporte = (obj.getString("esporte"));
            txtNomeUpdate.setText(obj.getString("nome"));
            txtPesoUpdate.setText(obj.getString("peso"));
            txtEmailUpdate.setText(obj.getString("email"));
            btnEsporteupdate.setText(obj.getString("esporte"));
        }
    }
}
