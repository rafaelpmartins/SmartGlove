package com.example.smartglove;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StarTreino_Activity extends SairSystem {

    private TextView txtTitulo;
    private ProgressBar progressBar;
    private int pStatus = 0, valorCirculo;
    private Handler handler = new Handler();
    private ImageButton btnStart, btnPause, btnReset;
    private long timeWhenPaused = 0;
    private boolean isChronometerRunning;
    private Chronometer chrono;
    private String TempoTreino;
    private AlertDialog alertDialog;
    private CharSequence[] values = {"15 Minutos", "30 Minutos", "45 Minutos", "1 Hora", "1 Minuto (teste)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_treino_layout);

        txtTitulo = (TextView) findViewById(R.id.txtIdTitulo);
        progressBar = (ProgressBar) findViewById(R.id.id_progressBar);
        btnStart = (ImageButton) findViewById(R.id.id_start);
        btnPause = (ImageButton) findViewById(R.id.id_pause);
        btnReset = (ImageButton) findViewById(R.id.id_reset);
        chrono = (Chronometer) findViewById(R.id.id_chronometer);

        Intent intent = getIntent();
        String titulo = intent.getExtras().getString("Titulo");
        txtTitulo.setText(titulo);
//        if (titulo.equals("Aikido")){
//
//        }
//        if (titulo.equals("Boxe")){
//
//        }
//        if (titulo.equals("Caratê")){
//
//        }
//        if (titulo.equals("Jeet Kune Do")){
//
//        }
//        if (titulo.equals("Jiu Jitsu")){
//
//        }
//        if (titulo.equals("Kick Boxing")){
//
//        }
//        if (titulo.equals("Muay Thai")){
//
//        }
//        if (titulo.equals("Wing Chun")){
//
//        }

        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
        btnStart.setImageResource(R.drawable.ic_play_circle_outline_dp);

        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String t = (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
                chronometer.setText(t);

                if (chronometer.getText().toString().equalsIgnoreCase(TempoTreino)) {
                    chronometer.stop();
                    btnStart.setEnabled(false);
                    btnPause.setEnabled(false);
                    btnReset.setEnabled(false);
                    btnStart.setImageResource(R.drawable.ic_play_circle_outline_cinza_dp);
                    btnPause.setImageResource(R.drawable.ic_pause_circle_outline_cinza_dp);
                    btnReset.setImageResource(R.drawable.ic_replay_cinza_dp);
                    Toast.makeText(getApplicationContext(), "Treinou Acabou", Toast.LENGTH_SHORT).show();
                }
            }
        });
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.setText("00:00:00");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        progressBar();
        choiceTime();

        ToolbarBack();
    }

    private void ToolbarBack() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbarBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_dp);
        getSupportActionBar().setTitle("Treino");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void start() {
        btnStart.setEnabled(false);
        btnStart.setImageResource(R.drawable.ic_play_circle_outline_cinza_dp);
        btnPause.setEnabled(true);
        btnPause.setImageResource(R.drawable.ic_pause_circle_outline_dp);
        btnReset.setEnabled(true);
        btnReset.setImageResource(R.drawable.ic_replay_dp);

        isChronometerRunning = true;
        if (chrono.getText().toString().equalsIgnoreCase("00:00:00")) {
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.setText("00:00:00");
            chrono.start();
        } else {
            chrono.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            chrono.start();
        }
    }

    private void pause() {
        timeWhenPaused = chrono.getBase() - SystemClock.elapsedRealtime();
        chrono.stop();
        isChronometerRunning = false;

        btnStart.setEnabled(true);
        btnStart.setImageResource(R.drawable.ic_play_circle_outline_dp);
        btnPause.setEnabled(false);
        btnPause.setImageResource(R.drawable.ic_pause_circle_outline_cinza_dp);
    }

    private void reset() {
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.setText("00:00:00");
        chrono.stop();
        pStatus = 0;
        isChronometerRunning = false;

        btnStart.setEnabled(true);
        btnStart.setImageResource(R.drawable.ic_play_circle_outline_dp);
        btnPause.setEnabled(false);
        btnPause.setImageResource(R.drawable.ic_pause_circle_outline_cinza_dp);
        btnReset.setEnabled(false);
        btnReset.setImageResource(R.drawable.ic_replay_cinza_dp);
    }

    private void progressBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= valorCirculo) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                        }
                    });
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isChronometerRunning == true) {
                        pStatus++;
                    }
                }
            }
        }).start();
    }

    private void choiceTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StarTreino_Activity.this);
        builder.setCancelable(false);
        builder.setTitle("Tempo de treino");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        TempoTreino = "00:15:00";
                        valorCirculo = 1500;
                        progressBar.setMax(1500);
                        break;

                    case 1:
                        TempoTreino = "00:30:00";
                        valorCirculo = 3000;
                        progressBar.setMax(300);
                        break;

                    case 2:
                        TempoTreino = "00:45:00";
                        valorCirculo = 4500;
                        progressBar.setMax(4500);
                        break;
                    case 3:
                        TempoTreino = "01:00:00";
                        valorCirculo = 6000;
                        progressBar.setMax(6000);
                        break;
                    case 4:
                        TempoTreino = "00:01:00";
                        valorCirculo = 100;
                        progressBar.setMax(100);
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
