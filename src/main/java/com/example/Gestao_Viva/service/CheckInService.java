package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Gestao_Viva.repository.VisitaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class CheckInService {

    @Autowired
    private VisitaRepository visitaRepository;


    public List<Visita> buscarAgendamentosParaHoje() {
        return visitaRepository.findByDataVisitaAndStatus(LocalDate.now(), StatusVisita.AGENDADO);
    }

    public List<Visita> buscarAgendamentoPorNome(String nome) {
        return visitaRepository.findByDataVisitaAndStatusAndNomeResponsavelContainingIgnoreCaseOrNomeInstituicaoContainingIgnoreCase(
            LocalDate.now(),
            StatusVisita.AGENDADO,
            nome,
            nome
        );
    }

    
    public Visita realizarCheckIn(Long visitaId) {

       
        Visita visita = visitaRepository.findById(visitaId)
            .orElseThrow(() -> new RuntimeException("Visita com ID " + visitaId + " não encontrada no sistema."));

       
        if (!visita.getDataVisita().equals(LocalDate.now())) {
            throw new RuntimeException("Check-in não permitido. Visita agendada para outra data.");
        }

        
        if (!visita.getStatus().equals(StatusVisita.AGENDADO)) {
            
            
            throw new RuntimeException("A visita já está com status: " + visita.getStatus());
        }

        
        visita.setStatus(StatusVisita.REALIZADO);
        return visitaRepository.save(visita);
    }
}