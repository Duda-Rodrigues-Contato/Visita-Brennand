package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitaManagementService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Transactional(readOnly = true)
    public Visita buscarPorToken(String token) {
        return visitaRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado ou token inválido."));
    }

  
    @Transactional
    public Visita confirmarVisita(String token) {
        Visita visita = buscarPorToken(token);

        if (!visita.getStatus().equals(StatusVisita.AGENDADO)) {
            throw new RuntimeException("Esta visita não pode ser confirmada (possivelmente já foi confirmada ou cancelada).");
        }

        visita.setStatus(StatusVisita.CONFIRMADO);
        return visitaRepository.save(visita);
    }

   
    @Transactional
    public Visita cancelarVisita(String token) {
        Visita visita = buscarPorToken(token);

        if (!visita.getStatus().equals(StatusVisita.AGENDADO)) {
            throw new RuntimeException("Esta visita não pode ser cancelada (possivelmente já foi confirmada ou cancelada).");
        }

        visita.setStatus(StatusVisita.CANCELADO);
        return visitaRepository.save(visita);
    }
}