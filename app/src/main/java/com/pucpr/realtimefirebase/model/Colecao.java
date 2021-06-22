package com.pucpr.realtimefirebase.model;

import java.util.ArrayList;

public class Colecao {
    private long id;
    private String titulo;
    private String caminhoImg;
    private int completo;
    private int volumeUnico;
    private int ultimoLido;
    private int ultimoComprado;
    private String genero;
    private String autor;

    private ArrayList<Volume> volumes;

    public Colecao(){}

    public Colecao(long id, String titulo, String caminhoImg, int completo, int volumeUnico, int ultimoLido, int ultimoComprado, String genero, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.caminhoImg = caminhoImg;
        this.completo = completo;
        this.volumeUnico = volumeUnico;
        this.ultimoLido = ultimoLido;
        this.ultimoComprado = ultimoComprado;
        this.genero = genero;
        this.autor = autor;
    }

    public Colecao(String titulo, String caminhoImg, int completo, int volumeUnico, int ultimoLido, int ultimoComprado, String genero, String autor) {
        this.titulo = titulo;
        this.caminhoImg = caminhoImg;
        this.completo = completo;
        this.volumeUnico = volumeUnico;
        this.ultimoLido = ultimoLido;
        this.ultimoComprado = ultimoComprado;
        this.genero = genero;
        this.autor = autor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCaminhoImg() {
        return caminhoImg;
    }

    public void setCaminhoImg(String caminhoImg) {
        this.caminhoImg = caminhoImg;
    }

    public int getCompleto() {
        return completo;
    }

    public void setCompleto(int completo) {
        this.completo = completo;
    }

    public int getVolumeUnico() {
        return volumeUnico;
    }

    public void setVolumeUnico(int volumeUnico) {
        this.volumeUnico = volumeUnico;
    }

    public int getUltimoLido() {
        return ultimoLido;
    }

    public void setUltimoLido(int ultimoLido) {
        this.ultimoLido = ultimoLido;
    }

    public int getUltimoComprado() {
        return ultimoComprado;
    }

    public void setUltimoComprado(int ultimoComprado) {
        this.ultimoComprado = ultimoComprado;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ArrayList<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(ArrayList<Volume> volumes) {
        this.volumes = volumes;
    }
}
