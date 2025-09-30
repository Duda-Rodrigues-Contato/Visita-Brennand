package com.gestaoviva.gestao_viva.service; // <-- CORRIGIDO

import com.gestaoviva.gestao_viva.dto.VisitaDTO; // <-- IMPORT CORRIGIDO
import com.gestaoviva.gestao_viva.model.Visita; // <-- IMPORT CORRIGIDO
import com.gestaoviva.gestao_viva.repository.VisitaRepository; // <-- IMPORT CORRIGIDO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class VisitaService {

    @Autowired
    private VisitaRepository visitaRepository;

    public Visita agendar(VisitaDTO visitaDTO) {
        
        Visita novaVisita = new Visita();
        novaVisita.setNomeInstituicao(visitaDTO.getNomeInstituicao());
        novaVisita.setCnpj(visitaDTO.getCnpj());
        novaVisita.setTipoInstituicao(visitaDTO.getTipoInstituicao());
        novaVisita.setNomeResponsavel(visitaDTO.getNomeResponsavel());
        novaVisita.setEmailResponsavel(visitaDTO.getEmailResponsavel());
        novaVisita.setTelefoneResponsavel(visitaDTO.getTelefoneResponsavel());
        novaVisita.setHorarioChegada(visitaDTO.getHorarioChegada());
        novaVisita.setNumeroVisitantes(visitaDTO.getNumeroVisitantes());
        novaVisita.setIncluiPcd(visitaDTO.isIncluiPcd());
        novaVisita.setMembros(visitaDTO.getMembros());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        novaVisita.setNomeInstituicao(visitaDTO.getNomeInstituicao());
        // ... (cole aqui o restante do código do método que enviei anteriormente)

        return visitaRepository.save(novaVisita);
    }
}