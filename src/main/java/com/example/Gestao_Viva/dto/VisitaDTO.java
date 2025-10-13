package com.example.Gestao_Viva.dto;

import com.example.Gestao_Viva.validation.NotOnMonday; 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class VisitaDTO {

    @NotEmpty(message = "O nome da instituição não pode estar vazio.")
    @Size(min = 3, max = 100, message = "O nome da instituição deve ter entre 3 e 100 caracteres.")
    private String nomeInstituicao;
    private String cnpj;
    @NotEmpty(message = "O tipo de instituição deve ser selecionado.")
    private String tipoInstituicao;
    @NotEmpty(message = "O nome do responsável não pode estar vazio.")
    private String nomeResponsavel;
    @NotEmpty(message = "O e-mail do responsável não pode estar vazio.")
    @Email(message = "Por favor, insira um e-mail válido.")
    private String emailResponsavel;
    @NotEmpty(message = "O telefone do responsável não pode estar vazio.")
    private String telefoneResponsavel;

    @NotEmpty(message = "A data da visita é obrigatória.")
    @NotOnMonday 
    private String dataVisita;

    @NotEmpty(message = "O horário de chegada é obrigatório.")
    private String horarioChegada;
    @NotNull(message = "O número de visitantes não pode estar vazio.")
    private Integer numeroVisitantes;
    private boolean incluiPcd;
    private List<String> membros;
    @NotEmpty(message = "A origem do visitante é obrigatória.")
    private String origemVisitante;
    private String dataNascimento;
    private String sexo;


    public String getNomeInstituicao() { return nomeInstituicao; }
    public void setNomeInstituicao(String nomeInstituicao) { this.nomeInstituicao = nomeInstituicao; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getTipoInstituicao() { return tipoInstituicao; }
    public void setTipoInstituicao(String tipoInstituicao) { this.tipoInstituicao = tipoInstituicao; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }
    public String getEmailResponsavel() { return emailResponsavel; }
    public void setEmailResponsavel(String emailResponsavel) { this.emailResponsavel = emailResponsavel; }
    public String getTelefoneResponsavel() { return telefoneResponsavel; }
    public void setTelefoneResponsavel(String telefoneResponsavel) { this.telefoneResponsavel = telefoneResponsavel; }
    public String getDataVisita() { return dataVisita; }
    public void setDataVisita(String dataVisita) { this.dataVisita = dataVisita; }
    public String getHorarioChegada() { return horarioChegada; }
    public void setHorarioChegada(String horarioChegada) { this.horarioChegada = horarioChegada; }
    public Integer getNumeroVisitantes() { return numeroVisitantes; }
    public void setNumeroVisitantes(Integer numeroVisitantes) { this.numeroVisitantes = numeroVisitantes; }
    public boolean isIncluiPcd() { return incluiPcd; }
    public void setIncluiPcd(boolean incluiPcd) { this.incluiPcd = incluiPcd; }
    public List<String> getMembros() { return membros; }
    public void setMembros(List<String> membros) { this.membros = membros; }
    public String getOrigemVisitante() { return origemVisitante; }
    public void setOrigemVisitante(String origemVisitante) { this.origemVisitante = origemVisitante; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
}