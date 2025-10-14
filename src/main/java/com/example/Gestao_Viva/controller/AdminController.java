package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.dto.StatusUpdateRequestDTO;
import com.example.Gestao_Viva.model.enums.EstadoParque;
import com.example.Gestao_Viva.service.StatusParqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") 
public class AdminController {

    @Autowired
    private StatusParqueService statusParqueService;

    
    @GetMapping("/status")
    public String exibirPainelStatus(Model model) {
        
        model.addAttribute("statusAtual", statusParqueService.obterStatusAtual());
       
        model.addAttribute("statusUpdateDTO", new StatusUpdateRequestDTO());
        
        model.addAttribute("todosOsEstados", EstadoParque.values());
        
        return "admin-status"; 
    }

   
    @PostMapping("/status")
    public String atualizarStatus(
            @Valid @ModelAttribute("statusUpdateDTO") StatusUpdateRequestDTO statusUpdateDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            
            model.addAttribute("statusAtual", statusParqueService.obterStatusAtual());
            model.addAttribute("todosOsEstados", EstadoParque.values());
            return "admin-status";
        }

        statusParqueService.atualizarStatus(statusUpdateDTO.getEstado(), statusUpdateDTO.getMotivo());
        
        
        return "redirect:/admin/status?sucesso";
    }
}