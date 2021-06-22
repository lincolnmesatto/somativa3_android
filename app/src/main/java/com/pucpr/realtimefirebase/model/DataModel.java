package com.pucpr.realtimefirebase.model;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class DataModel {
    private static DataModel instance = new DataModel();

    private ArrayList<Colecao> colecoes;
    private ArrayList<Colecao> colecoesOriginal;
    private Context context;
    private int posicao;

    private DataModel(){
        colecoes = new ArrayList<>();
        colecoesOriginal = new ArrayList<>();
    }
    public static DataModel getInstance(){
        return instance;
    }

    public void setContext(Context context){
        this.context = context;

        if(colecoes == null || colecoes.size() <= 0)
            colecoes = new ArrayList<>();
    }

    public ArrayList<Colecao> getColecoes(){
        return colecoes;
    }

    public void setColecoes(ArrayList<Colecao> colecoes) {
        this.colecoes = colecoes;
    }

    public ArrayList<Colecao> getColecoesOriginal() {
        return colecoesOriginal;
    }

    public void setColecoesOriginal(ArrayList<Colecao> colecoesOriginal) {
        this.colecoesOriginal = colecoesOriginal;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public Context getContext() {
        return context;
    }
}
