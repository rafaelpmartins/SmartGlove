package com.example.smartglove;

public class Esportes {
    private String titulo;
    private int imagem;

    public Esportes() {
    }

    public Esportes(int imagem, String titulo) {
        this.imagem = imagem;
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
