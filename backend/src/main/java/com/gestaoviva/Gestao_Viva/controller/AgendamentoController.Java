package com.gestaoviva.gestao_viva.controller; 

import com.gestaoviva.gestao_viva.dto.VisitaDTO; 
import com.gestaoviva.gestao_viva.service.VisitaService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    @Autowired
    private VisitaService visitaService;

    
    @GetMapping
    public String exibirFormulario(Model model) {
        model.addAttribute("visitaDTO", new VisitaDTO());
        return "formulario-agendamento";
    }

    
    @PostMapping
    public String processarFormulario(@ModelAttribute VisitaDTO visitaDTO) {
        visitaService.agendar(visitaDTO);
        return "redirect:/agendamento/sucesso"; 
    }

    
    @GetMapping("/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso-agendamento";
    }
}