package com.gestaoviva.Gestao_Viva.dto; 

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
    private boolean incluiPcd;
    private List<String> membros;

    
}