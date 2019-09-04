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

    //Força valores
    double[] yAxisData = {50.7, 20.5, 15.9, 30.1, 20.2, 60.6, 15.5, 40.4, 45.3, 10.1, 18.8, 90.9};

    //Velocidade valores
    double[] zAxisData = {10, 30, 20, 40, 60, 40, 80, 25, 35, 60, 90, 70};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafico_layout);

        LineChartView lineChartView = findViewById(R.id.chart);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        List zAxisValues = new ArrayList();

        //linha 1 e 2
        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));//roxo Força
        Line line2 = new Line(zAxisValues).setColor(Color.parseColor("#00BFFF"));// azul velocidade

        //adiciona valores percorrendo o vetor
        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, (float) yAxisData[i]));
        }

        for (int i = 0; i < zAxisData.length; i++) {
            zAxisValues.add(new PointValue(i, (float) zAxisData[i]));
        }

        //declara as linhas como list
        List lines = new ArrayList();
        lines.add(line);
        lines.add(line2);

        //adicionando as linhas ao grafico
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //definiu local, tamanho(texto) e cor do eixo x
        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextColor(Color.parseColor("#B71C1C"));
        //axis.setTextSize(16);
        //axis.setName("Velocidade");
        data.setAxisXBottom(axis);

        //definiu local, tamanho(texto), cor e titulo do eixo y
        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#B71C1C"));
        //yAxis.setName("Força");
        //yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        //expecificou um teto e um chão para o eixo y
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        viewport.bottom = 0;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
