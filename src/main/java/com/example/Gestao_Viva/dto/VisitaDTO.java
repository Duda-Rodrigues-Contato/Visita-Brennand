package com.example.Gestao_Viva.dto;

import java.util.List;

public class VisitaDTO {
    
    private String nomeInstituicao;
    private String cnpj;
    private String tipoInstituicao;
    private String nomeResponsavel;
    private String emailResponsavel;
    private String telefoneResponsavel;
    private String dataVisita; 
    private String horarioChegada;
    private Integer numeroVisitantes;
    //@SuppressWarnings("unused") -> testar se roda sem!
    private boolean incluiPcd;
    private List<String> membros;

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

    public String getDataVisita() {
        return dataVisita;
    }

    public void setDataVisita(String dataVisita) {
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
        return this.incluiPcd;
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