package com.example.Gestao_Viva.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckinController {
    @GetMapping("/check-in")
    public String mostrarPaginaCheckIn(Model model) {
        
        // 3. (Opcional, mas importante para o futuro)
        // Aqui você buscaria as "Visitas Agendadas Hoje" do seu banco de dados
        // e as enviaria para a página.
        
        // Exemplo:
        // List<Visita> visitas = visitaService.buscarVisitasDeHoje();
        // model.addAttribute("visitasAgendadas", visitas); 

        // 4. Retorna o nome do arquivo HTML (sem o ".html") 
        // que está em 'templates/'
        return "check-in";
    }
}
