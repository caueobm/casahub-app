package com.caueobm.casahub.model;

public class Cliente { // Ou ClienteInteressado
    private Long idInteresse;
    private Long idImovel;
    private String tipoImovel; // Adicione este campo
    private String enderecoCompleto; // Adicione este campo
    private String nomeCliente;
    private String emailCliente;
    private String telefoneCliente;

    // Getters e setters para todos os campos (incluindo os novos)
    public Long getIdInteresse() { return idInteresse; }
    public void setIdInteresse(Long idInteresse) { this.idInteresse = idInteresse; }
    public Long getIdImovel() { return idImovel; }
    public void setIdImovel(Long idImovel) { this.idImovel = idImovel; }
    public String getTipoImovel() { return tipoImovel; }
    public void setTipoImovel(String tipoImovel) { this.tipoImovel = tipoImovel; }
    public String getEnderecoCompleto() { return enderecoCompleto; }
    public void setEnderecoCompleto(String enderecoCompleto) { this.enderecoCompleto = enderecoCompleto; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }
}