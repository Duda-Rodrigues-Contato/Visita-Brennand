package com.example.Gestao_Viva.model;

import com.example.Gestao_Viva.model.enums.EstadoParque; 
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_parque")
public class StatusParque {

   

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoParque estado; 

    @Column(length = 500)
    private String motivo;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    public StatusParque() {}


    public StatusParque(EstadoParque estado, String motivo) {
        this.estado = estado;
        this.motivo = motivo;
    }

    @PrePersist
    @PreUpdate
    private void atualizarData() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EstadoParque getEstado() { return estado; }
    public void setEstado(EstadoParque estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }
}