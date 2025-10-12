package com.example.Gestao_Viva.dto;

import java.time.LocalDate;

public class VisitaDiariaDTO {

    private LocalDate data;
    private Long totalVisitantes;

    public VisitaDiariaDTO(LocalDate data, Long totalVisitantes) {
        this.data = data;
        this.totalVisitantes = totalVisitantes;
    }

   
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Long getTotalVisitantes() {
        return totalVisitantes;
    }

    public void setTotalVisitantes(Long totalVisitantes) {
        this.totalVisitantes = totalVisitantes;
    }
}