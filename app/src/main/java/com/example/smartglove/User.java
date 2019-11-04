package com.example.smartglove;

public class User {

    private static int id;
    private String nome;
    private String peso;
    private static String email;
    private String senha;
    private String espore;

    public User() {
    }

    public User(int id, String nome, String peso, String email, String senha, String espore) {
        this.id = id;
        this.nome = nome;
        this.peso = peso;
        this.email = email;
        this.senha = senha;
        this.espore = espore;
    }

    public User(int id, String nome, String email, String espore) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.espore = espore;
    }

    public User(int id, String nome, String peso, String email, String espore) {
        this.id = id;
        this.nome = nome;
        this.peso = peso;
        this.email = email;
        this.espore = espore;
    }

    public static int getId() {
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

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public static String getEmail() {
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