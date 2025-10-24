package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.dto.AvaliacaoStatsDTO;
import com.example.Gestao_Viva.model.Avaliacao; // Importe este
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.AvaliacaoRepository; // Importe este
import com.example.Gestao_Viva.service.AvaliacaoService;
import com.example.Gestao_Viva.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Gestao_Viva.dto.VisitaDiariaDTO;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard") // Mapeamento base para o controller
public class DashboardController {

    @Autowired
    private VisitaService visitaService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    // NOVO: Precisamos do repositório de avaliações para buscar com paginação
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // --- Rota Principal do Dashboard (Relatório de Visitas) ---
    @GetMapping // Mapeia para /dashboard
    public String exibirDashboard(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 5, sort = "dataVisita", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        // ... (código existente para buscar visitas, stats, etc.) ...
        if (dataInicio == null || dataFim == null) {
            LocalDate hoje = LocalDate.now();
            dataInicio = hoje.with(TemporalAdjusters.firstDayOfMonth());
            dataFim = hoje.with(TemporalAdjusters.lastDayOfMonth());
        }
        Page<Visita> paginaDeVisitas = visitaService.buscarVisitasPorPeriodo(dataInicio, dataFim, pageable);
        long totalVisitas = visitaService.contarTotalDeVisitasNoPeriodo(dataInicio, dataFim);
        long visitasConcluidas = visitaService.contarVisitasConcluidasNoPeriodo(dataInicio, dataFim);
        AvaliacaoStatsDTO avaliacaoStats = avaliacaoService.getStats(dataInicio, dataFim);

        model.addAttribute("paginaDeVisitas", paginaDeVisitas);
        model.addAttribute("totalVisitas", totalVisitas);
        model.addAttribute("visitasConcluidas", visitasConcluidas);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("avaliacaoStats", avaliacaoStats);
        model.addAttribute("size", pageable.getPageSize());
        String sort = pageable.getSort().get()
                            .map(order -> order.getProperty() + "," + order.getDirection().name())
                            .collect(Collectors.joining());
        model.addAttribute("sort", sort);

        return "dashboard-relatorio"; // Nome do HTML do relatório
    }

    // --- Rota para o Gráfico (Existente) ---
    @GetMapping("/api/chart-data")
    @ResponseBody
    public List<VisitaDiariaDTO> getChartData(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return visitaService.getDadosGrafico(dataInicio, dataFim);
    }


    // --- NOVA ROTA PARA EXIBIR AVALIAÇÕES ---
    @GetMapping("/avaliacoes") // Mapeia para /dashboard/avaliacoes
    public String exibirAvaliacoes(
            // PageableDefault define o padrão: 5 por página, ordenado pela data da avaliação (mais recente primeiro)
            @PageableDefault(size = 5, sort = "dataAvaliacao", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        // Busca as avaliações do banco de dados usando o repositório com paginação
        Page<Avaliacao> paginaDeAvaliacoes = avaliacaoRepository.findAll(pageable);

        // Envia os dados para o HTML
        model.addAttribute("paginaDeAvaliacoes", paginaDeAvaliacoes);

        // Envia 'size' e 'sort' para o HTML (igual fizemos no dashboard)
        model.addAttribute("size", pageable.getPageSize());
        String sort = pageable.getSort().get()
                .map(order -> order.getProperty() + "," + order.getDirection().name())
                .collect(Collectors.joining());
        model.addAttribute("sort", sort);

        return "avaliacoes"; // Nome do NOVO arquivo HTML que vamos criar
    }
}