package com.pucpr.realtimefirebase.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pucpr.realtimefirebase.R;
import com.pucpr.realtimefirebase.model.Colecao;
import com.pucpr.realtimefirebase.model.DataModel;
import com.pucpr.realtimefirebase.model.Volume;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setTitle("Gráficos");

        ArrayList<Colecao> colecoes = DataModel.getInstance().getColecoes();
        ArrayList<PieEntry> lista = new ArrayList<>();
        lista.add(new PieEntry(0, "Lidos"));
        lista.add(new PieEntry(0, "Não Lidos"));
        lista.add(new PieEntry(0, "Faltantes"));

        ArrayList<PieEntry> listaColecao = new ArrayList<>();
        listaColecao.add(new PieEntry(0, "Completo"));
        listaColecao.add(new PieEntry(0, "Vol. Único"));
        listaColecao.add(new PieEntry(0, "Em andamento"));

        for (Colecao c: colecoes) {
            ArrayList<Volume> volumes = c.getVolumes();
            PieEntry pie;
            for(Volume v : volumes){
                switch (v.getStatus()){
                    case "Lido":
                        pie = new PieEntry(lista.get(0).getValue()+1, "Lidos");
                        lista.set(0, pie);
                        break;
                    case "Comprado":
                        pie = new PieEntry(lista.get(1).getValue()+1, "Não Lidos");
                        lista.set(1, pie);
                        break;
                    case "Não possui":
                        pie = new PieEntry(lista.get(2).getValue()+1, "Faltantes");
                        lista.set(2, pie);
                        break;
                    default:
                        break;
                }
            }

            if(c.getVolumeUnico() == 0 && c.getCompleto() == 0){
                pie = new PieEntry(listaColecao.get(2).getValue()+1, "Em andamento");
                listaColecao.set(2, pie);
            }else if(c.getVolumeUnico() == 1){
                pie = new PieEntry(listaColecao.get(1).getValue()+1, "Vol. Único");
                listaColecao.set(1, pie);
            }else {
                pie = new PieEntry(listaColecao.get(0).getValue()+1, "Completo");
                listaColecao.set(0, pie);
            }
        }

        //Chart volume
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);

        Description description = new Description();
        description.setText("Volumes Total: "+(lista.get(0).getValue() + lista.get(1).getValue()+lista.get(2).getValue()));
        description.setTextSize(16f);
        description.setPosition(585,450);

        pieChart.setDescription(description);

        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(30f);

        PieDataSet pieDataSet = new PieDataSet(lista, "");
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        pieChart.animateXY(1400, 1400);

        // chart colecao
        PieChart pieChartColecao = findViewById(R.id.pieChartColecao);
        pieChartColecao.setUsePercentValues(false);

        Description descriptionColecao = new Description();
        descriptionColecao.setText("Coleção Total: "+ DataModel.getInstance().getColecoes().size());
        descriptionColecao.setTextSize(16f);

        pieChartColecao.setDescription(descriptionColecao);

        pieChartColecao.setHoleRadius(30f);
        pieChartColecao.setTransparentCircleRadius(30f);

        PieDataSet pieDataSetColecao = new PieDataSet(listaColecao, "");
        PieData pieDataColecao = new PieData(pieDataSetColecao);

        pieChartColecao.setData(pieDataColecao);
        pieDataSetColecao.setColors(ColorTemplate.JOYFUL_COLORS);

        pieChartColecao.animateXY(1400, 1400);
    }
}