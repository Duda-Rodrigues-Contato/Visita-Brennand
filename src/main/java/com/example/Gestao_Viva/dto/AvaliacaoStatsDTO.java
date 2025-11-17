package com.example.Gestao_Viva.dto;

import java.util.Collections;
import java.util.List;

public class AvaliacaoStatsDTO {

    private long totalAvaliacoes;
    private double mediaAvaliacaoGeral;
    
    private double mediaRecomendacao;
    private double mediaQualidadeRecepcao;
    private double mediaLimpezaOrganizacao;
    private double mediaAcessibilidade;
    private double mediaConservacaoEsculturas;
    private double mediaSinalizacaoInformacoes;
    
    private List<String> comentariosRecentes = Collections.emptyList();

    
    public long getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(long totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }
    
    public double getMediaAvaliacaoGeral() { return mediaAvaliacaoGeral; }
    public void setMediaAvaliacaoGeral(double mediaAvaliacaoGeral) { this.mediaAvaliacaoGeral = mediaAvaliacaoGeral; }
    
    public double getMediaRecomendacao() { return mediaRecomendacao; }
    public void setMediaRecomendacao(double mediaRecomendacao) { this.mediaRecomendacao = mediaRecomendacao; }
    
    public double getMediaQualidadeRecepcao() { return mediaQualidadeRecepcao; }
    public void setMediaQualidadeRecepcao(double mediaQualidadeRecepcao) { this.mediaQualidadeRecepcao = mediaQualidadeRecepcao; }
    
    public double getMediaLimpezaOrganizacao() { return mediaLimpezaOrganizacao; }
    public void setMediaLimpezaOrganizacao(double mediaLimpezaOrganizacao) { this.mediaLimpezaOrganizacao = mediaLimpezaOrganizacao; }
    
    public double getMediaAcessibilidade() { return mediaAcessibilidade; }
    public void setMediaAcessibilidade(double mediaAcessibilidade) { this.mediaAcessibilidade = mediaAcessibilidade; }
    
    public double getMediaConservacaoEsculturas() { return mediaConservacaoEsculturas; }
    public void setMediaConservacaoEsculturas(double mediaConservacaoEsculturas) { this.mediaConservacaoEsculturas = mediaConservacaoEsculturas; }
    
    public double getMediaSinalizacaoInformacoes() { return mediaSinalizacaoInformacoes; }
    public void setMediaSinalizacaoInformacoes(double mediaSinalizacaoInformacoes) { this.mediaSinalizacaoInformacoes = mediaSinalizacaoInformacoes; }

    public List<String> getComentariosRecentes() { return comentariosRecentes; }
    public void setComentariosRecentes(List<String> comentariosRecentes) { this.comentariosRecentes = comentariosRecentes; }
}