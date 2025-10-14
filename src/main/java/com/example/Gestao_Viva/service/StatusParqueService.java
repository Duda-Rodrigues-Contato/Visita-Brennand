package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.model.enums.EstadoParque; 
import com.example.Gestao_Viva.repository.StatusParqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StatusParqueService {

    @Autowired
    private StatusParqueRepository statusParqueRepository;

    /**
     * @return 
     */
    @Transactional(readOnly = true)
    public StatusParque obterStatusAtual() {
        Optional<StatusParque> statusOpt = statusParqueRepository.findTopByOrderByUltimaAtualizacaoDesc();
        
        
        if (statusOpt.isEmpty()) {
            StatusParque statusInicial = new StatusParque(EstadoParque.ABERTO, "O parque está a operar normalmente.");
            return statusParqueRepository.save(statusInicial);
        }
        
        return statusOpt.get();
    }

    /**
     * @param novoEstado 
     * @param motivo
     * @return 
     */
    @Transactional
    public StatusParque atualizarStatus(EstadoParque novoEstado, String motivo) {
        if (novoEstado == null) {
            throw new IllegalArgumentException("O estado não pode ser nulo.");
        }
        
        StatusParque novoStatus = new StatusParque(novoEstado, motivo);
        return statusParqueRepository.save(novoStatus);
    }
}