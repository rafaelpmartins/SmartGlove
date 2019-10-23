package com.example.smartglove;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends SairSystem implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String usuario;
    private TextView nomeMenu, emailMenu;
    private static boolean validarAikido = false, validarBoxe = false, validarCaratê = false, validarJeet = false,
            validarJiu = false, validarKick = false, validarMuay = false, validarWing = false;

    List<Esportes> lstEsportes;
    List<User> userList;

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.navView);
        View headerView = navigationView.getHeaderView(0);
        nomeMenu = (TextView) headerView.findViewById(R.id.id_nomeMenu);
        emailMenu = (TextView) headerView.findViewById(R.id.id_emailMenu);

//        loading();
//        readDatas();

        userList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.id_ToolbarMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Smart Glove");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_one: {
                startActivity(new Intent(getApplicationContext(), Grafico_Activity.class));
                break;
            }
            case R.id.nav_item_two: {
                startActivity(new Intent(getApplicationContext(), Configuracoes_Activity.class));
                break;
            }
            case R.id.nav_item_three: {
                Toast.makeText(this, "Manual", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_item_four: {
                startActivity(new Intent(getApplicationContext(), Avalicao_Activity.class));
                break;
            }
            default: {
                Toast.makeText(this, "Menu Default", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loading() {
        Intent intentRecebe = getIntent();
        Bundle infoNome = intentRecebe.getExtras();
        usuario = infoNome.getString("chave_email");

        HashMap<String, String> params = new HashMap<>();
        params.put("email", usuario);

        MainActivity.PerformNetworkRequest request = new MainActivity.PerformNetworkRequest(Api.URL_LOADING_USER, params, CODE_POST_REQUEST);
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
                refreshUserList(object.getJSONArray("datas"));

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

    private void readDatas() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_LOADING_USER, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshUserList(JSONArray datas) throws JSONException {
        userList.clear();

        for (int i = 0; i < datas.length(); i++) {
            JSONObject obj = datas.getJSONObject(i);

            userList.add(new User(
                    obj.getString("nome"),
                    obj.getString("email"),
                    obj.getString("esporte")
            ));
            nomeMenu.setText(obj.getString("nome"));
            emailMenu.setText(obj.getString("email"));

            if (obj.getString("esporte").contains("Aikido")) {
                validarAikido = true;
            }
            if (obj.getString("esporte").contains("Boxe")) {
                validarBoxe = true;
            }
            if (obj.getString("esporte").contains("Caratê")) {
                validarCaratê = true;
            }
            if (obj.getString("esporte").contains("Jeet Kune Do")) {
                validarJeet = true;
            }
            if (obj.getString("esporte").contains("Jiu Jitsu")) {
                validarJiu = true;
            }
            if (obj.getString("esporte").contains("Kick Boxing")) {
                validarKick = true;
            }
            if (obj.getString("esporte").contains("Muay Thai")) {
                validarMuay = true;
            }
            if (obj.getString("esporte").contains("Wing Chun")) {
                validarWing = true;
            }
            lista();
        }
    }

    private void lista() {
        lstEsportes = new ArrayList<>();

        if (validarAikido == true) {
            lstEsportes.add(new Esportes(R.drawable.aikido, "Aikido"));
        }
        if (validarBoxe == true) {
            lstEsportes.add(new Esportes(R.drawable.boxing_gloves, "Boxe"));
        }
        if (validarCaratê == true) {
            lstEsportes.add(new Esportes(R.drawable.karate, "Caratê"));
        }
        if (validarJeet == true) {
            lstEsportes.add(new Esportes(R.drawable.jeet_kune_do, "Jeet Kune Do"));
        }
        if (validarJiu == true) {
            lstEsportes.add(new Esportes(R.drawable.jiu_jitsu, "Jiu Jitsu"));
        }
        if (validarKick == true) {
            lstEsportes.add(new Esportes(R.drawable.kicking_boxing, "Kick Boxing"));
        }
        if (validarMuay == true) {
            lstEsportes.add(new Esportes(R.drawable.muai_thay, "Muay Thai"));
        }
        if (validarWing == true) {
            lstEsportes.add(new Esportes(R.drawable.wing_chun, "Wing Chun"));
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(getApplicationContext(), lstEsportes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
