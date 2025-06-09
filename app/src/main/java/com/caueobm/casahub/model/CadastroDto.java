package com.caueobm.casahub.model;

import com.google.gson.annotations.SerializedName;

public class CadastroDto {

    @SerializedName("nome") // O nome do campo como esperado pelo seu backend (ex: "nome", "fullName", etc.)
    private String nome;

    @SerializedName("email") // O nome do campo como esperado pelo seu backend (ex: "email", "username", etc.)
    private String email;

    @SerializedName("password") // O nome do campo como esperado pelo seu backend (ex: "senha", "password")
    private String senha;

    // Adicione outros campos se o seu backend os exigir para o cadastro,
    // por exemplo, confirmação de senha, telefone, etc.
    // Exemplo:
    // @SerializedName("confirmar_senha")
    // private String confirmarSenha;

    // Construtor
    public CadastroDto(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters (e Setters, se necessário, embora para um request DTO, getters podem ser suficientes)
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

    // Exemplo de getter para o campo opcional:
    // public String getConfirmarSenha() {
    //     return confirmarSenha;
    // }
    //
    // public void setConfirmarSenha(String confirmarSenha) {
    //     this.confirmarSenha = confirmarSenha;
    // }
}