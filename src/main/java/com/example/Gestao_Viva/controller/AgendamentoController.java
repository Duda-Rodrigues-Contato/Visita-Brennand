package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.dto.VisitaDTO;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.service.EmailService; 
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

    
    @Autowired
    private EmailService emailService;

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

        
        Visita novaVisita = visitaService.agendar(visitaDTO);

        
        try {
            enviarEmailConfirmacao(novaVisita);
        } catch (Exception e) {
           
            System.err.println("Falha ao enviar e-mail de confirmação: " + e.getMessage());
        }

        return "redirect:/agendamento/sucesso";
    }

    @GetMapping("/agendamento/sucesso")
    public String mostrarPaginaSucesso() {
        return "sucesso-agendamento";
    }

    
    private void enviarEmailConfirmacao(Visita visita) {
        String nome = visita.getNomeResponsavel();
        String email = visita.getEmailResponsavel();
        String token = visita.getToken();
        
       
        String urlGestao = "http://localhost:8080/gerenciar-visita?token=" + token;

        String assunto = "Gestão Viva: Confirme seu Agendamento";
        String texto = String.format(
            "Olá %s,\n\n" +
            "Seu pré-agendamento para o Parque de Esculturas foi recebido com sucesso!\n\n" +
            "Para garantir sua vaga, por favor, clique no link abaixo para confirmar ou, se necessário, cancelar sua visita:\n\n" +
            "%s\n\n" +
            "Atenciosamente,\nEquipe Gestão Viva",
            nome, urlGestao
        );
        
        emailService.enviarEmail(email, assunto, texto);
    }
}