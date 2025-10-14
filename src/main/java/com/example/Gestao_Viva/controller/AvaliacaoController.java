package com.example.Gestao_Viva.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Gestao_Viva.dto.AvaliacaoDTO;
import com.example.Gestao_Viva.dto.VisitaDTO;
import com.example.Gestao_Viva.service.AvaliacaoService;

import jakarta.validation.Valid;

import com.example.Gestao_Viva.service.AvaliacaoService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping("/avaliacao")
    public String exibirAvaliacao(Model model) {
        model.addAttribute("avaliacaoDTO", new AvaliacaoDTO());
        return "avaliacao";
    }

    @PostMapping("/avaliacao/sucesso")
    public String processarFormulario(
            @Valid @ModelAttribute("avaliacaoDTO") AvaliacaoDTO avaliacaoDTO,
            BindingResult bindingResult) { 

        if (bindingResult.hasErrors()) {
            return "formulario-agendamento"; 
        } 

        avaliacaoService.avaliar(avaliacaoDTO); // "avaliar" NO SERVICE
        return "redirect:/avaliacao/agradecimento";
    
    }

}
