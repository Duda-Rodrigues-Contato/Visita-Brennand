package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.EstadoParque;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import com.example.Gestao_Viva.service.EmailService;
import com.example.Gestao_Viva.service.StatusParqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StatusParqueService statusParqueService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VisitaRepository visitaRepository;

    @GetMapping("/status")
    public String exibirPainelStatus(Model model) {
        model.addAttribute("statusAtual", statusParqueService.obterStatusAtual());
        model.addAttribute("todosOsEstados", EstadoParque.values());
        model.addAttribute("statusUpdateDTO", new com.example.Gestao_Viva.dto.StatusUpdateRequestDTO());
        return "admin-status";
    }

    @PostMapping("/status")
    public String atualizarStatus(
            @RequestParam("estado") EstadoParque estado,
            @RequestParam(name = "motivo", required = false) String motivo,
            Model model) {

        statusParqueService.atualizarStatus(estado, motivo);

        if (estado == EstadoParque.FECHADO) {
            notificarVisitantesSobreFechamento(motivo);
        }
        
        return "redirect:/admin/status?sucesso";
    }

    private void notificarVisitantesSobreFechamento(String motivo) {
        final String motivoReal = (motivo != null && !motivo.trim().isEmpty()) ? motivo : "motivo não especificado";

        List<Visita> visitasDeHoje = visitaRepository.findByDataVisitaAndStatus(LocalDate.now(), StatusVisita.AGENDADO);

        System.out.println("Encontradas " + visitasDeHoje.size() + " visitas para notificar sobre o fechamento.");

        for (Visita visita : visitasDeHoje) {
            String email = visita.getEmailResponsavel();
            String assunto = "AVISO IMPORTANTE: Visita ao Parque de Esculturas Cancelada";
            String texto = String.format(
                "Olá %s,\n\nInformamos que, devido a '%s', o Parque de Esculturas Francisco Brennand encontra-se fechado hoje.\n\n" +
                "Consequentemente, a sua visita agendada foi cancelada. Pedimos desculpa por qualquer inconveniente.\n\n" +
                "Atenciosamente,\nEquipa Gestão Viva",
                visita.getNomeResponsavel(), motivoReal
            );
            emailService.enviarEmail(email, assunto, texto);
            System.out.println("Notificação de fechamento enviada para: " + email);
        }
    }
}