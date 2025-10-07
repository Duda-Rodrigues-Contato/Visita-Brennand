package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.dto.VisitaDTO;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public List<Visita> buscarVisitasPorData(LocalDate data) {
        return visitaRepository.findByDataVisitaOrderByHorarioChegada(data);
    }

    public List<Visita> buscarVisitasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return visitaRepository.findByDataVisitaBetweenOrderByDataVisitaAscHorarioChegadaAsc(dataInicio, dataFim);
    }

    public Integer calcularTotalVisitantesPorData(LocalDate data) {
        return visitaRepository.sumNumeroVisitantesByDataVisita(data);
    }
}