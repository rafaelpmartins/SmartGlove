package com.example.smartglove;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class StarTreino_Activity extends SairSystem {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

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
    private CharSequence[] values = {"15 Minutos", "30 Minutos", "45 Minutos", "1 Hora", "30 segundos (teste)"};
    private double[] force = {50.7, 20.5, 15.9, 30.1, 20.2, 60.6, 15.5, 40.4, 45.3, 10.1};//Força valores
    private double[] velocity = {10, 30, 20, 40, 60, 40, 80, 25, 35, 60};//Velocidade valores
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_treino_layout);

        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        txtTitulo = (TextView) findViewById(R.id.txtIdTitulo);
        progressBar = (ProgressBar) findViewById(R.id.id_progressBar);
        btnStart = (ImageButton) findViewById(R.id.id_start);
        btnPause = (ImageButton) findViewById(R.id.id_pause);
        btnReset = (ImageButton) findViewById(R.id.id_reset);
        chrono = (Chronometer) findViewById(R.id.id_chronometer);

        Intent intent2 = getIntent();
        final String titulo = intent2.getExtras().getString("Titulo");
        txtTitulo.setText(titulo);

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

                    HashMap<String, String> params = new HashMap<>();
                    params.put("tempo", TempoTreino);
                    params.put("data", getDateTime());
                    params.put("titulo", titulo);
                    params.put("forca", Arrays.toString(force));
                    params.put("velocity", Arrays.toString(velocity));
                    params.put("fk_id_user", String.valueOf(user.getId()));

                    StarTreino_Activity.PerformNetworkRequest request = new StarTreino_Activity.PerformNetworkRequest(Api.URL_CREATE_TREINO, params, CODE_POST_REQUEST);
                    request.execute();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(1500);
                    }
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

                Intent intent = new Intent(StarTreino_Activity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
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
                        TempoTreino = "00:00:30";
                        valorCirculo = 50;
                        progressBar.setMax(50);
                        break;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
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
                    if (object.getString("message").equals("dados salvos")) {
                        Intent intent = new Intent(StarTreino_Activity.this, Grafico_Activity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    if (object.getString("message").equals("Algum erro ocorreu. seus dados se perderam")) {
                        Intent intent = new Intent(StarTreino_Activity.this, MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
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
}
