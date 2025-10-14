package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.repository.StatusParqueRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusParqueController {

    private final StatusParqueRepository repository;

    public StatusParqueController(StatusParqueRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/status-parque")
    public String mostrarStatusParque(Model model) {
        StatusParque status = repository.findTopByOrderByUltimaAtualizacaoDesc()
                .orElse(null);

        model.addAttribute("status", status);
        return "status-parque"; 
    }
}
