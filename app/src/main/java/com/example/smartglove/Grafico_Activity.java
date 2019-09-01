package com.example.smartglove;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Grafico_Activity extends AppCompatActivity {

    //eixo X
    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    //eixo Y
    double[] yAxisData = {50.7, 20.5, 15.9, 30.1, 20.2, 60.6, 15.5, 40.4, 45.3, 10.1, 18.8, 90.9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafico_layout);

        LineChartView lineChartView = findViewById(R.id.chart);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        //valores do eixo Y e Cor da linha
        Line line = new Line(yAxisValues).setColor(Color.parseColor("#B71C1C"));

        //adiciona dados no eixo x e y
        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, (float) yAxisData[i]));
        }

        //um list do tipo linha
        List lines = new ArrayList();
        lines.add(line);

        //adicionando a linha ao grafico
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //definiu local, tamanho(texto) e cor do eixo x
        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#B71C1C"));
        data.setAxisXBottom(axis);

        //definiu local, tamanho(texto), cor e titulo do eixo y
        Axis yAxis = new Axis();
        yAxis.setName("Força");
        yAxis.setTextColor(Color.parseColor("#B71C1C"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        //expecificou um teto para o eixo y
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
