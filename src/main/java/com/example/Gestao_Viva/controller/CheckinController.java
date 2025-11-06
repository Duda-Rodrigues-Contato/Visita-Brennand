package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.service.CheckInService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.ResponseBody; 

import java.util.List;

@Controller
public class CheckinController {
    
    
    @Autowired
    private CheckInService checkInService;

    
    @GetMapping("/check-in")
    public String mostrarPaginaCheckIn(Model model) {
        
        return "check-in"; 
    }
    
  

    /**
     * * @param nome
     * @return 
     */
    @GetMapping("/api/check-in/buscar")
    @ResponseBody 
    public List<Visita> apiBuscarAgendamentos(@RequestParam(name = "nome", required = false) String nome) {
        if (nome == null || nome.trim().isEmpty()) {
           
            return checkInService.buscarAgendamentosParaHoje(); //
        } else {
           
            return checkInService.buscarAgendamentoPorNome(nome); 
        }
    }

    /**
     * @param visitaId 
     * @return 
     */
    @PostMapping("/api/check-in/{id}")
    @ResponseBody 
    public ResponseEntity<?> apiRealizarCheckIn(@PathVariable("id") Long visitaId) {
        try {
            
            Visita visitaAtualizada = checkInService.realizarCheckIn(visitaId); //
            
            return ResponseEntity.ok(visitaAtualizada);
        } catch (RuntimeException e) {
            
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}