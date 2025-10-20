package com.example.Gestao_Viva.dto;

import java.util.Collections;
import java.util.List;

public class AvaliacaoStatsDTO {

    private long totalAvaliacoes;
    private double mediaAvaliacaoGeral;
    private List<String> comentariosRecentes = Collections.emptyList();

   
    public long getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(long totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public double getMediaAvaliacaoGeral() {
        return mediaAvaliacaoGeral;
    }

    public void setMediaAvaliacaoGeral(double mediaAvaliacaoGeral) {
        this.mediaAvaliacaoGeral = mediaAvaliacaoGeral;
    }

    public List<String> getComentariosRecentes() {
        return comentariosRecentes;
    }

    public void setComentariosRecentes(List<String> comentariosRecentes) {
        this.comentariosRecentes = comentariosRecentes;
    }
}