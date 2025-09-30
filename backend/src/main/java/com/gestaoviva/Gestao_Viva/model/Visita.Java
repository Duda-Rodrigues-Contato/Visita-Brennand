package com.gestaoviva.gestao_viva.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "visitas")
public class Visita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informações da Instituição
    @Column(nullable = false)
    private String nomeInstituicao;
    private String cnpj;
    @Column(nullable = false)
    private String tipoInstituicao;
    @Column(nullable = false)
    private String nomeResponsavel;
    @Column(nullable = false)
    private String emailResponsavel;
    @Column(nullable = false)
    private String telefoneResponsavel;

    // Detalhes da Visita
    @Column(nullable = false)
    private LocalDate dataVisita;
    @Column(nullable = false)
    private String horarioChegada;
    @Column(nullable = false)
    private Integer numeroVisitantes;
    private boolean incluiPcd;

    // Membros do Grupo
    @ElementCollection // Ideal para listas simples de strings
    @CollectionTable(name = "visita_membros", joinColumns = @JoinColumn(name = "visita_id"))
    @Column(name = "nome_membro")
    private List<String> membros;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeInstituicao() {
        return nomeInstituicao;
    }

    public void setNomeInstituicao(String nomeInstituicao) {
        this.nomeInstituicao = nomeInstituicao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTipoInstituicao() {
        return tipoInstituicao;
    }

    public void setTipoInstituicao(String tipoInstituicao) {
        this.tipoInstituicao = tipoInstituicao;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getEmailResponsavel() {
        return emailResponsavel;
    }

    public void setEmailResponsavel(String emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(LocalDate dataVisita) {
        this.dataVisita = dataVisita;
    }

    public String getHorarioChegada() {
        return horarioChegada;
    }

    public void setHorarioChegada(String horarioChegada) {
        this.horarioChegada = horarioChegada;
    }

    public Integer getNumeroVisitantes() {
        return numeroVisitantes;
    }

    public void setNumeroVisitantes(Integer numeroVisitantes) {
        this.numeroVisitantes = numeroVisitantes;
    }

    public boolean isIncluiPcd() {
        return incluiPcd;
    }

    public void setIncluiPcd(boolean incluiPcd) {
        this.incluiPcd = incluiPcd;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }
}
