package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.StatusParque;
import com.example.Gestao_Viva.service.StatusParqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusParqueController {

    @Autowired
    private StatusParqueService statusParqueService;

    
    @GetMapping("/status-parque")
    public String mostrarStatusParquePage(Model model) {
        
        model.addAttribute("status", statusParqueService.obterStatusAtual());
        return "status-parque";
    }


    @GetMapping("/api/parque/status")
    @ResponseBody 
    public StatusParque getStatusAtual() {
        return statusParqueService.obterStatusAtual();
    }
}