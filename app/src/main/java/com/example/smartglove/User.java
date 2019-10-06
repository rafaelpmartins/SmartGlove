package com.example.smartglove;

public class User {

    private int id;
    private String nome, email, senha, espore;

    public User() {
    }

    User(String senha) {
        this.senha = senha;
    }

    public User(int id, String nome, String email, String senha, String espore) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.espore = espore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEspore() {
        return espore;
    }

    public void setEspore(String espore) {
        this.espore = espore;
    }
}