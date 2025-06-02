package com.caueobm.casahub.model;

public class Imovel {
    private int id;
    private String tipo;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private Double valor;
    private Double valorAluguel;
    private int numeroQuartos;
    private int numeroBanheiros;
    private int metragem;
    private boolean mobiliado;
    private String descricao;
    private boolean disponivel;
    private String dataCadastro; // Pode usar String para simplificar o parse


    public Imovel(int id, String tipo, String endereco, String cidade, String estado, String cep,
                  Double valor, Double valorAluguel, int numeroQuartos, int numeroBanheiros, int metragem,
                  boolean mobiliado, String descricao, boolean disponivel, String dataCadastro) {
        this.id = id;
        this.tipo = tipo;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.valor = valor;
        this.valorAluguel = valorAluguel;
        this.numeroQuartos = numeroQuartos;
        this.numeroBanheiros = numeroBanheiros;
        this.metragem = metragem;
        this.mobiliado = mobiliado;
        this.descricao = descricao;
        this.disponivel = disponivel;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public int getNumeroQuartos() {
        return numeroQuartos;
    }

    public void setNumeroQuartos(int numeroQuartos) {
        this.numeroQuartos = numeroQuartos;
    }

    public int getNumeroBanheiros() {
        return numeroBanheiros;
    }

    public void setNumeroBanheiros(int numeroBanheiros) {
        this.numeroBanheiros = numeroBanheiros;
    }

    public int getMetragem() {
        return metragem;
    }

    public void setMetragem(int metragem) {
        this.metragem = metragem;
    }

    public boolean isMobiliado() {
        return mobiliado;
    }

    public void setMobiliado(boolean mobiliado) {
        this.mobiliado = mobiliado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
