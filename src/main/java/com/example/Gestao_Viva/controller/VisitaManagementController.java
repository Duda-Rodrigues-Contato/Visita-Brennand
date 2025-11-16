package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.service.QRCodeService;
import com.example.Gestao_Viva.service.VisitaManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VisitaManagementController {

    @Autowired
    private VisitaManagementService visitaManagementService;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/gerenciar-visita")
    public String exibirPaginaGestao(@RequestParam("token") String token, Model model) {
        
        if (model.containsAttribute("mensagem")) {
            return "gerenciar-visita";
        }

        try {
            Visita visita = visitaManagementService.buscarPorToken(token);
            
            if (visita.getStatus() == StatusVisita.CONFIRMADO) {
                return "redirect:/ticket?token=" + token;
            }
            
            model.addAttribute("visita", visita);

        } catch (RuntimeException e) {
            model.addAttribute("erro", "Token inválido ou agendamento não encontrado.");
        }
        
        return "gerenciar-visita";
    }

    @PostMapping("/gerenciar-visita/confirmar")
    public String confirmarVisita(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        try {
            visitaManagementService.confirmarVisita(token);
            return "redirect:/ticket?token=" + token; 

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/gerenciar-visita?token=" + token;
        }
    }

    @PostMapping("/gerenciar-visita/cancelar")
    public String cancelarVisita(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        try {
            visitaManagementService.cancelarVisita(token);
            redirectAttributes.addFlashAttribute("mensagem", "Sua visita foi cancelada com sucesso.");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        
        return "redirect:/gerenciar-visita?token=" + token;
    }

    @GetMapping("/ticket")
    public String exibirTicket(@RequestParam("token") String token, Model model) {
        try {
            Visita visita = visitaManagementService.buscarPorToken(token);

            if (visita.getStatus() != StatusVisita.CONFIRMADO) {
                model.addAttribute("mensagem", "Por favor, confirme a sua visita antes de gerar o ticket.");
                return "redirect:/gerenciar-visita?token=" + token;
            }

            
            String qrCodeBase64 = qrCodeService.gerarQRCodeBase64(String.valueOf(visita.getId()), 250, 250);

            model.addAttribute("visita", visita);
            model.addAttribute("qrCode", qrCodeBase64);
            
            return "ticket-visita";

        } catch (RuntimeException e) {
            model.addAttribute("erro", "Ticket não encontrado.");
            return "gerenciar-visita";
        }
    }
}