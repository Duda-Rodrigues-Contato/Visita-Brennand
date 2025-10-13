package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.dto.VisitaDTO;
import com.example.Gestao_Viva.service.VisitaService;
import jakarta.validation.Valid; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AgendamentoController {

    @Autowired
    private VisitaService visitaService;

    @GetMapping("/agendamento")
    public String exibirFormulario(Model model) {
        model.addAttribute("visitaDTO", new VisitaDTO());
        return "formulario-agendamento";
    }

    @PostMapping("/agendamento")
    public String processarFormulario(
            @Valid @ModelAttribute("visitaDTO") VisitaDTO visitaDTO,
            BindingResult bindingResult) { 

        if (bindingResult.hasErrors()) {
            return "formulario-agendamento"; 
        } 

        visitaService.agendar(visitaDTO);
        return "redirect:/agendamento/sucesso";
    }

    @GetMapping("/agendamento/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso-agendamento";
    }
}