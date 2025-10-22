package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.dto.AvaliacaoDTO;
import com.example.Gestao_Viva.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping("/avaliacao")
    public String exibirFormularioAvaliacao(@RequestParam Long visitaId, Model model) {
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setVisitaId(visitaId);
        model.addAttribute("avaliacaoDTO", avaliacaoDTO);
        return "avaliacao";
    }

    @PostMapping("/avaliacao")
    public String processarFormularioAvaliacao(
            @Valid @ModelAttribute("avaliacaoDTO") AvaliacaoDTO avaliacaoDTO,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "avaliacao";
        }

        try {
            avaliacaoService.salvar(avaliacaoDTO);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "avaliacao-avaliacao";
        }

        return "redirect:/avaliacao-agradecimento";
    }

    @GetMapping("/avaliacao-agradecimento")
    public String exibirPaginaAgradecimento() {
        return "avaliacao-agradecimento";
    }
}