package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledTasksService {

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private EmailService emailService;

   
    @Scheduled(cron = "0 0 8 * * *")
    public void enviarEmailDePesquisa() {
        System.out.println("Executando tarefa agendada: Envio de e-mails de pesquisa...");
        LocalDate ontem = LocalDate.now().minusDays(1);

       
        List<Visita> visitasRealizadas = visitaRepository.findByDataVisitaAndStatus(ontem, StatusVisita.REALIZADO);

        for (Visita visita : visitasRealizadas) {
            String emailDestinatario = visita.getEmailResponsavel();
            String assunto = "Conte-nos sobre sua visita ao Parque de Esculturas!";
            String urlPesquisa = "http://localhost:8080/avaliacao?visitaId=" + visita.getId();
            String texto = String.format(
                "Olá %s,\n\nObrigado por visitar o Parque de Esculturas Francisco Brennand!\n\n" +
                "Gostaríamos de saber como foi a sua experiência. Por favor, dedique um momento para preencher a nossa pesquisa de satisfação clicando no link abaixo:\n\n" +
                "%s\n\nSua opinião é muito importante para nós.\n\nAtenciosamente,\nEquipa Gestão Viva",
                visita.getNomeResponsavel(), urlPesquisa
            );

            emailService.enviarEmail(emailDestinatario, assunto, texto);
            System.out.println("E-mail de pesquisa enviado para: " + emailDestinatario);
        }
    }
}