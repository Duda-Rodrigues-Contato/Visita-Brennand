package com.gestaoviva.gestao_viva.controller; // <-- CORRIGIDO

import com.gestaoviva.gestao_viva.dto.VisitaDTO; // <-- IMPORT CORRIGIDO
import com.gestaoviva.gestao_viva.service.VisitaService; // <-- IMPORT CORRIGIDO
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

    // Método para exibir o formulário
    @GetMapping
    public String exibirFormulario(Model model) {
        model.addAttribute("visitaDTO", new VisitaDTO());
        return "formulario-agendamento";
    }

    // Método para processar o envio do formulário
    @PostMapping
    public String processarFormulario(@ModelAttribute VisitaDTO visitaDTO) {
        visitaService.agendar(visitaDTO);
        return "redirect:/agendamento/sucesso"; // Redireciona para uma página de sucesso
    }

    // Método para exibir a página de sucesso
    @GetMapping("/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso-agendamento";
    }
}