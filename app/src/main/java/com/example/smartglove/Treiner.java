package com.example.smartglove;

import java.io.Serializable;

public class Treiner implements Serializable {

    private int idTreino;
    private String tempo, data, titulo;
    private String forca, velocidade;

    public Treiner() {
    }

    public Treiner(int idTreino, String tempo, String data, String titulo) {
        this.idTreino = idTreino;
        this.tempo = tempo;
        this.data = data;
        this.titulo = titulo;
    }

    public int getIdTreino() {
        return idTreino;
    }

    public void setIdTreino(int idTreino) {
        this.idTreino = idTreino;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getForca() {
        return forca;
    }

    public void setForca(String forca) {
        this.forca = forca;
    }

    public String getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(String velocidade) {
        this.velocidade = velocidade;
    }
}
