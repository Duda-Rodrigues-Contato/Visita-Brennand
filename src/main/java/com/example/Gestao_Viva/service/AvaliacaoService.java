package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.dto.AvaliacaoDTO;
import com.example.Gestao_Viva.model.Avaliacao;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.AvaliacaoRepository;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @Transactional
    public Avaliacao salvar(AvaliacaoDTO avaliacaoDTO) {
        Visita visita = visitaRepository.findById(avaliacaoDTO.getVisitaId())
                .orElseThrow(() -> new RuntimeException("Visita não encontrada!"));

        if (avaliacaoRepository.existsByVisita(visita)) {
            throw new RuntimeException("Esta visita já foi avaliada.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setVisita(visita);
        avaliacao.setAvaliacaoGeral(avaliacaoDTO.getAvaliacaoGeral());
        avaliacao.setRecomendacao(avaliacaoDTO.getRecomendacao());
        avaliacao.setQualidadeRecepcao(avaliacaoDTO.getQualidadeRecepcao());
        avaliacao.setLimpezaOrganizacao(avaliacaoDTO.getLimpezaOrganizacao());
        avaliacao.setAcessibilidade(avaliacaoDTO.getAcessibilidade());
        avaliacao.setConservacaoEsculturas(avaliacaoDTO.getConservacaoEsculturas());
        avaliacao.setSinalizacaoInformacoes(avaliacaoDTO.getSinalizacaoInformacoes());
        avaliacao.setComentariosSugestoes(avaliacaoDTO.getComentariosSugestoes());
        avaliacao.setPerfilVisita(avaliacaoDTO.getPerfilVisita());

        return avaliacaoRepository.save(avaliacao);
    }
}