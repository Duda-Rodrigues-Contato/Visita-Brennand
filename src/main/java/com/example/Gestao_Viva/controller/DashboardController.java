package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.service.VisitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping
    public String exibirDashboard(
            @RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataSelecionada,
            Model model) {

        if (dataSelecionada == null) {
            dataSelecionada = LocalDate.now();
        }

        List<Visita> visitasAgendadas = visitaService.buscarVisitasPorData(dataSelecionada);
        Integer totalVisitantes = visitaService.calcularTotalVisitantesPorData(dataSelecionada);

        model.addAttribute("visitas", visitasAgendadas);
        model.addAttribute("totalVisitantes", totalVisitantes);
        model.addAttribute("dataSelecionada", dataSelecionada);

        return "dashboard";
    }
}