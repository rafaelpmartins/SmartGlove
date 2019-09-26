package com.example.smartglove;

public class User {

    private int id;
    private String nome, data_nasc, sexo, email, senha, espore;

    public User() {
    }

    public User(int id, String nome, String data_nasc, String sexo, String email, String senha, String espore) {
        this.id = id;
        this.nome = nome;
        this.data_nasc = data_nasc;
        this.sexo = sexo;
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

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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
