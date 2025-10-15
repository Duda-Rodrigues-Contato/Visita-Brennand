package com.example.Gestao_Viva.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "visita_id", nullable = false, unique = true)
    private Visita visita;

    private Integer avaliacaoGeral;
    private Integer recomendacao;
    private Integer qualidadeRecepcao;
    private Integer limpezaOrganizacao;
    private Integer acessibilidade;
    private Integer conservacaoEsculturas;
    private Integer sinalizacaoInformacoes;

    @Column(length = 1000)
    private String comentariosSugestoes;

    private String perfilVisita;
    private LocalDateTime dataAvaliacao;

    @PrePersist
    protected void onCreate() {
        dataAvaliacao = LocalDateTime.now();
    }
    
    public Avaliacao() {}

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Visita getVisita() { return visita; }
    public void setVisita(Visita visita) { this.visita = visita; }
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
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
}