package com.example.Gestao_Viva.dto;

import com.example.Gestao_Viva.model.enums.EstadoParque;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequestDTO {

    @NotNull(message = "O estado do parque é obrigatório.")
    private EstadoParque estado;

    private String motivo;

    
    public EstadoParque getEstado() {
        return estado;
    }

    public void setEstado(EstadoParque estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}