package com.example.smartglove;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuracoes_Activity extends AppCompatActivity {

    private Button btnEsporteupdate, btnAlterar;
    private TextView txtNomeUpdate, txtPesoUpdate, txtEmailUpdate, txtSenhaUpdate;
    private LinearLayout linearNome, linearPeso, linearEmail, linearSenha;
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String item, emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText edtNomeUpdate, edtPesoUpdate, edtEmailUpdate, edtSenhaUpdate;
    private boolean validarPeso = false, validarEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes_layout);

        btnEsporteupdate = (Button) findViewById(R.id.id_btnEsporteUpdate);
        btnAlterar = (Button) findViewById(R.id.id_btnAlterar);
        txtNomeUpdate = (TextView) findViewById(R.id.id_txtNomeUpdate);
        txtPesoUpdate = (TextView) findViewById(R.id.id_txtPesoUpdate);
        txtEmailUpdate = (TextView) findViewById(R.id.id_txtEmailUpdate);
        txtSenhaUpdate = (TextView) findViewById(R.id.id_txtSenhaUpdate);
        linearNome = (LinearLayout) findViewById(R.id.id_linearNome);
        linearPeso = (LinearLayout) findViewById(R.id.id_linearPeso);
        linearEmail = (LinearLayout) findViewById(R.id.id_linearEmail);
        linearSenha = (LinearLayout) findViewById(R.id.id_linearSenha);

        txtNomeUpdate.setText("Nome");
        txtPesoUpdate.setText("Peso");
        txtEmailUpdate.setText("Email");
        txtSenhaUpdate.setText("Senha");
        btnEsporteupdate.setText("Adicionar Esportes(s)");

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

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarAlterar();
            }
        });

        listItems = getResources().getStringArray(R.array.esporte_item);
        checkedItems = new boolean[listItems.length];

        btnEsporteupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Configuracoes_Activity.this, R.style.AlertDialogTheme);
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
                        btnEsporteupdate.setText(item);
                        btnEsporteupdate.setTextColor(Color.parseColor("#B71C1C"));
                        if (btnEsporteupdate.getText() == "") {
                            btnEsporteupdate.setText("Adicionar Esporte(s)");
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
                            btnEsporteupdate.setText("Adicionar Esporte(s)");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }

    private void ToolbarBack() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbarBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_dp);
        getSupportActionBar().setTitle("Configurações");
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

    private void validarAlterar() {
        if (txtNomeUpdate.getText() != "Nome") {
            Toast.makeText(getApplicationContext(), "Alterou nome", Toast.LENGTH_SHORT).show();
        }
        if (txtPesoUpdate.getText() != "Peso") {
            Toast.makeText(getApplicationContext(), "Alterou peso", Toast.LENGTH_SHORT).show();
        }
        if (txtEmailUpdate.getText() != "Email") {
            Toast.makeText(getApplicationContext(), "Alterou email", Toast.LENGTH_SHORT).show();
        }
        if (txtSenhaUpdate.getText() != "Senha") {
            Toast.makeText(getApplicationContext(), "Alterou senha", Toast.LENGTH_SHORT).show();
        }
        if (btnEsporteupdate.getText() != "Adicionar Esportes(s)") {
            Toast.makeText(getApplicationContext(), "Alterou esporte(s)", Toast.LENGTH_SHORT).show();
        }
        if (txtNomeUpdate.getText() == "Nome" && txtPesoUpdate.getText() == "Peso" && txtEmailUpdate.getText() == "Email"
                && txtSenhaUpdate.getText() == "Senha" && btnEsporteupdate.getText() == "Adicionar Esportes(s)") {
            Toast.makeText(getApplicationContext(), "Nada foi alterado", Toast.LENGTH_SHORT).show();
        }
    }
}
