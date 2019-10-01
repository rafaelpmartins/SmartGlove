package com.example.smartglove;

public class Esportes {
    private String titulo;
    private int imagem;

    public Esportes() {
    }

    Esportes(int imagem, String titulo) {
        this.imagem = imagem;
        this.titulo = titulo;
    }

    String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
