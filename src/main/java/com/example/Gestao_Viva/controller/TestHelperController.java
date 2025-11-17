package com.example.Gestao_Viva.controller;

import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestHelperController {

    @Autowired
    private VisitaRepository visitaRepository;
    @GetMapping("/api/test/last-token")
    public String getLastToken() {
        List<Visita> visitas = visitaRepository.findAll(
            PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();

        if (visitas.isEmpty()) {
            return "NENHUMA_VISITA";
        }
        return visitas.get(0).getToken();
    }
}