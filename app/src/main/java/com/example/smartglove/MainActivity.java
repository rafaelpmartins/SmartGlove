package com.example.smartglove;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    List<Esportes> lstEsportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstEsportes = new ArrayList<>();
        lstEsportes.add(new Esportes(R.drawable.aikido, "Aikido"));
        lstEsportes.add(new Esportes(R.drawable.boxing_gloves, "Boxe"));
        lstEsportes.add(new Esportes(R.drawable.karate, "Caratê"));
        lstEsportes.add(new Esportes(R.drawable.jeet_kune_do, "Jeet Kune Do"));
        lstEsportes.add(new Esportes(R.drawable.jiu_jitsu, "Jiu-Jitsu"));
        lstEsportes.add(new Esportes(R.drawable.kicking_boxing, "Kick Boxing"));
        lstEsportes.add(new Esportes(R.drawable.muai_thay, "Muai Thay"));
        lstEsportes.add(new Esportes(R.drawable.wing_chun, "Wing Chun"));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(getApplicationContext(), lstEsportes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                Intent intent = new Intent(getApplicationContext(), Bluetooth_Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_item_two: {
                Intent intent = new Intent(getApplicationContext(), Grafico_Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_item_three: {
                Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_item_four: {
                Toast.makeText(this, "Menu 4", Toast.LENGTH_SHORT).show();
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
}
