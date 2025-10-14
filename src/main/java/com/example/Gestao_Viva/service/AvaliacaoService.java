package com.example.Gestao_Viva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Gestao_Viva.model.Avaliacao;

import com.example.Gestao_Viva.dto.AvaliacaoDTO;
import com.example.Gestao_Viva.repository.AvaliacaoRepository;
import com.example.Gestao_Viva.repository.VisitaRepository;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // Criar o "avaliar" colocado no "AvaliacaoController"
    
}
