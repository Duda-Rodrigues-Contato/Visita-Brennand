package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitaManagementService {

    @Autowired
    private VisitaRepository visitaRepository;
    
    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public Visita buscarPorToken(String token) {
        return visitaRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado ou token inválido."));
    }

    @Transactional
    public Visita confirmarVisita(String token) {
        Visita visita = buscarPorToken(token);

        if (!visita.getStatus().equals(StatusVisita.AGENDADO)) {
            throw new RuntimeException("Esta visita não pode ser confirmada (possivelmente já foi confirmada ou cancelada).");
        }

        visita.setStatus(StatusVisita.CONFIRMADO);
        Visita visitaSalva = visitaRepository.save(visita);

        enviarEmailTicket(visitaSalva);

        return visitaSalva;
    }

    private void enviarEmailTicket(Visita visita) {
        String linkTicket = "http://localhost:8080/ticket?token=" + visita.getToken();
        
        String assunto = "Sua Visita foi Confirmada! Aqui está o seu Ticket.";
        String texto = String.format(
            "Olá %s,\n\n" +
            "Sua visita ao Parque de Esculturas Francisco Brennand está CONFIRMADA para o dia %s às %s.\n\n" +
            "Acesse o seu Ticket Digital e QR Code no link abaixo:\n" +
            "%s\n\n" +
            "Apresente este código na entrada. Esperamos por si!",
            visita.getNomeResponsavel(),
            visita.getDataVisita(),
            visita.getHorarioChegada(),
            linkTicket
        );

        emailService.enviarEmail(visita.getEmailResponsavel(), assunto, texto);
    }

    @Transactional
    public Visita cancelarVisita(String token) {
        Visita visita = buscarPorToken(token);

        if (!visita.getStatus().equals(StatusVisita.AGENDADO)) {
            throw new RuntimeException("Esta visita não pode ser cancelada (possivelmente já foi confirmada ou cancelada).");
        }

        visita.setStatus(StatusVisita.CANCELADO);
        return visitaRepository.save(visita);
    }
}