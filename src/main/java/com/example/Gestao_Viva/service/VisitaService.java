package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.dto.VisitaDTO;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Gestao_Viva.dto.VisitaDiariaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

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
        novaVisita.setDataVisita(LocalDate.parse(visitaDTO.getDataVisita(), formatter));

        return visitaRepository.save(novaVisita);
    }



    public Page<Visita> buscarVisitasPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return visitaRepository.findByDataVisitaBetween(dataInicio, dataFim, pageable);
    }

    public long contarTotalDeVisitasNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return visitaRepository.countByDataVisitaBetween(dataInicio, dataFim);
    }

    public long contarVisitasConcluidasNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        
        return visitaRepository.countByDataVisitaBetweenAndStatus(dataInicio, dataFim, StatusVisita.REALIZADO);
    }

    public List<VisitaDiariaDTO> getDadosGrafico(LocalDate dataInicio, LocalDate dataFim) {
        return visitaRepository.findTotalVisitantesPorDia(dataInicio, dataFim);
    }
}