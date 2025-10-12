package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Gestao_Viva.dto.VisitaDiariaDTO;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping
    public String exibirDashboard(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(name = "page", defaultValue = "0") int page, 
            Model model) {

        
        if (dataInicio == null || dataFim == null) {
            LocalDate hoje = LocalDate.now();
            dataInicio = hoje.with(TemporalAdjusters.firstDayOfMonth());
            dataFim = hoje.with(TemporalAdjusters.lastDayOfMonth());
        }

     
        Pageable pageable = PageRequest.of(page, 5);
        Page<Visita> paginaDeVisitas = visitaService.buscarVisitasPorPeriodo(dataInicio, dataFim, pageable);

       
        long totalVisitas = visitaService.contarTotalDeVisitasNoPeriodo(dataInicio, dataFim);
        long visitasConcluidas = visitaService.contarVisitasConcluidasNoPeriodo(dataInicio, dataFim);

    
        model.addAttribute("paginaDeVisitas", paginaDeVisitas);
        model.addAttribute("totalVisitas", totalVisitas);
        model.addAttribute("visitasConcluidas", visitasConcluidas);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        
        return "dashboard-relatorio";
    }
     @GetMapping("/api/chart-data")
    @ResponseBody 
    public List<VisitaDiariaDTO> getChartData(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return visitaService.getDadosGrafico(dataInicio, dataFim);
    }

}