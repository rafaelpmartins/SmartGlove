package com.example.smartglove;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grafico_Activity extends SairSystem {

    private User user;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private ListView listView;
    private List<Treiner> treinerList;
    private double[] forceData = {50.7, 20.5, 15.9, 30.1, 20.2, 60.6, 15.5, 40.4, 45.3, 10.1};//Força valores
    private double[] VelocityData = {10, 30, 20, 40, 60, 40, 80, 25, 35, 60};//Velocidade valores
    private Treiner treiner = new Treiner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafico_layout);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        listView = (ListView) findViewById(R.id.listViewTreiners);
        treinerList = new ArrayList<>();
        ToolbarBack();
        readTreiner();
        loadingTreiner();

        ForceGraphic();
        VelocityGraphic();
    }

    private void ForceGraphic() {
        LineChart chart = (LineChart) findViewById(R.id.id_ForceChart);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry((float) 1, 800.8f));
        entries.add(new Entry((float) 2, 100.3f));
        entries.add(new Entry((float) 3, 700.6f));
        entries.add(new Entry((float) 4, 600.8f));
        entries.add(new Entry((float) 5, 900.1f));
        entries.add(new Entry((float) 6, 200.1f));
        entries.add(new Entry((float) 7, 300.1f));
        entries.add(new Entry((float) 8, 1000.1f));
        entries.add(new Entry((float) 9, 700.1f));
        entries.add(new Entry((float) 10, 400.1f));

        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.setNoDataText("Você precisa fornecer dados para o gráfico.");

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getApplicationContext(), e.getY() +" "+ "Kg", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        LineDataSet dataSet = new LineDataSet(entries, "Força em Kg.");
        //dando preenchimento debaixo da linha
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.RED);
        dataSet.setDrawValues(false);

        //editando a linha
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        // Desativa o ZOOM do Touch
        chart.setDoubleTapToZoomEnabled(false);

        // Efeito de animação
        chart.animateXY(3000, 3000);
        chart.invalidate();
    }

    private void VelocityGraphic() {
        LineChart chart = (LineChart) findViewById(R.id.id_VelocityChart);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry((float) 1, 30.1f));
        entries.add(new Entry((float) 2, 100.1f));
        entries.add(new Entry((float) 3, 70.1f));
        entries.add(new Entry((float) 4, 40.1f));
        entries.add(new Entry((float) 5, 80.8f));
        entries.add(new Entry((float) 6, 10.3f));
        entries.add(new Entry((float) 7, 70.6f));
        entries.add(new Entry((float) 8, 60.8f));
        entries.add(new Entry((float) 9, 90.1f));
        entries.add(new Entry((float) 10, 20.1f));

        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.setNoDataText("Você precisa fornecer dados para o gráfico.");

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getApplicationContext(), e.getY() +" "+ "M/s", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        LineDataSet dataSet = new LineDataSet(entries, "Velocidade em M/s");
        //dando preenchimento debaixo da linha
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.BLACK);
        dataSet.setDrawValues(false);

        //editando a linha
        dataSet.setCircleRadius(3f);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(Color.BLACK);
        dataSet.setCircleColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        // Desativa o ZOOM do Touch
        chart.setDoubleTapToZoomEnabled(false);

        // Efeito de animação
        chart.animateXY(3000, 3000);
        chart.invalidate();
    }

    private void ToolbarBack() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbarBack);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_dp);
        getSupportActionBar().setTitle("Graficos");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Grafico_Activity.this, MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadingTreiner() {
        HashMap<String, String> params = new HashMap<>();
        params.put("fk_id_user", String.valueOf(user.getId()));

        Grafico_Activity.PerformNetworkRequest request = new Grafico_Activity.PerformNetworkRequest(Api.URL_LOADING_TREINER, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readTreiner() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_LOADING_TREINER, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshTreinerList(JSONArray treinos) throws JSONException {
        treinerList.clear();

        for (int i = 0; i < treinos.length(); i++) {
            JSONObject obj = treinos.getJSONObject(i);

            treiner = new Treiner(
                    obj.getInt("id_treino"),
                    obj.getString("tempo"),
                    obj.getString("data"),
                    obj.getString("titulo"));
            treinerList.add(treiner);
        }

        TreinerAdapter adapter = new TreinerAdapter(treinerList);
        listView.setAdapter(adapter);
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
                    refreshTreinerList(object.getJSONArray("treinos"));
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

    private void deleteTeiner(int id_treino) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_TREINER + id_treino, null, CODE_GET_REQUEST);
        request.execute();
    }

    class TreinerAdapter extends ArrayAdapter<Treiner> {
        List<Treiner> treinerList;

        public TreinerAdapter(List<Treiner> treinerList) {
            super(Grafico_Activity.this, R.layout.treiner_list, treinerList);
            this.treinerList = treinerList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.treiner_list, null, true);

            TextView txtName = listViewItem.findViewById(R.id.id_txtName);
            TextView txtTime = listViewItem.findViewById(R.id.id_txtTime);
            TextView txtDate = listViewItem.findViewById(R.id.id_txtDate);

            final Treiner treiner = treinerList.get(position);

            txtName.setText(treiner.getTitulo());
            txtTime.setText(treiner.getTempo());
            txtDate.setText(treiner.getData());

            listViewItem.setOnTouchListener(new View.OnTouchListener() {
                private long firstTouchTS = 0;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            this.firstTouchTS = System.currentTimeMillis();
                            listViewItem.setBackgroundResource(R.drawable.botao_nasc);
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            listViewItem.setBackgroundResource(R.drawable.botao_list);

                            if ((System.currentTimeMillis() - this.firstTouchTS) / 1000 >= 1) {
                                listViewItem.setBackgroundResource(R.drawable.botao_list_apagar);

                                AlertDialog.Builder builder = new AlertDialog.Builder(Grafico_Activity.this);
                                builder.setTitle("Apagar " + treiner.getData()).setMessage("Tem certeza que deseja exluir?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteTeiner(treiner.getIdTreino());
                                                Intent intent = new Intent(Grafico_Activity.this, Grafico_Activity.class);
                                                intent.putExtra("user", user);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                listViewItem.setBackgroundResource(R.drawable.botao_list);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            break;
                        }
                    }
                    return true;
                }
            });

            return listViewItem;
        }
    }
}
