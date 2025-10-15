package com.example.Gestao_Viva.dto;

import jakarta.validation.constraints.NotNull;

public class AvaliacaoDTO {

    @NotNull
    private Long visitaId;

    @NotNull(message = "A avaliação geral é obrigatória.")
    private Integer avaliacaoGeral;
    
    private Integer recomendacao;
    private Integer qualidadeRecepcao;
    private Integer limpezaOrganizacao;
    private Integer acessibilidade;
    private Integer conservacaoEsculturas;
    private Integer sinalizacaoInformacoes;
    private String comentariosSugestoes;
    private String perfilVisita;

  
    public Long getVisitaId() { return visitaId; }
    public void setVisitaId(Long visitaId) { this.visitaId = visitaId; }
    public Integer getAvaliacaoGeral() { return avaliacaoGeral; }
    public void setAvaliacaoGeral(Integer avaliacaoGeral) { this.avaliacaoGeral = avaliacaoGeral; }
    public Integer getRecomendacao() { return recomendacao; }
    public void setRecomendacao(Integer recomendacao) { this.recomendacao = recomendacao; }
    public Integer getQualidadeRecepcao() { return qualidadeRecepcao; }
    public void setQualidadeRecepcao(Integer qualidadeRecepcao) { this.qualidadeRecepcao = qualidadeRecepcao; }
    public Integer getLimpezaOrganizacao() { return limpezaOrganizacao; }
    public void setLimpezaOrganizacao(Integer limpezaOrganizacao) { this.limpezaOrganizacao = limpezaOrganizacao; }
    public Integer getAcessibilidade() { return acessibilidade; }
    public void setAcessibilidade(Integer acessibilidade) { this.acessibilidade = acessibilidade; }
    public Integer getConservacaoEsculturas() { return conservacaoEsculturas; }
    public void setConservacaoEsculturas(Integer conservacaoEsculturas) { this.conservacaoEsculturas = conservacaoEsculturas; }
    public Integer getSinalizacaoInformacoes() { return sinalizacaoInformacoes; }
    public void setSinalizacaoInformacoes(Integer sinalizacaoInformacoes) { this.sinalizacaoInformacoes = sinalizacaoInformacoes; }
    public String getComentariosSugestoes() { return comentariosSugestoes; }
    public void setComentariosSugestoes(String comentariosSugestoes) { this.comentariosSugestoes = comentariosSugestoes; }
    public String getPerfilVisita() { return perfilVisita; }
    public void setPerfilVisita(String perfilVisita) { this.perfilVisita = perfilVisita; }
}