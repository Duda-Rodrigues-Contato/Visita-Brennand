package com.example.Gestao_Viva.service;

import com.example.Gestao_Viva.dto.AvaliacaoDTO;
import com.example.Gestao_Viva.dto.AvaliacaoStatsDTO;
import com.example.Gestao_Viva.model.Avaliacao;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.repository.AvaliacaoRepository;
import com.example.Gestao_Viva.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

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

    public AvaliacaoStatsDTO getStats(LocalDate dataInicio, LocalDate dataFim) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByVisitaDataVisitaBetween(dataInicio, dataFim);
        AvaliacaoStatsDTO stats = new AvaliacaoStatsDTO();

        if (avaliacoes.isEmpty()) {
            return stats; 
        }

        stats.setTotalAvaliacoes(avaliacoes.size());

        stats.setMediaAvaliacaoGeral(calcularMedia(avaliacoes, Avaliacao::getAvaliacaoGeral));
        stats.setMediaRecomendacao(calcularMedia(avaliacoes, Avaliacao::getRecomendacao));
        stats.setMediaQualidadeRecepcao(calcularMedia(avaliacoes, Avaliacao::getQualidadeRecepcao));
        stats.setMediaLimpezaOrganizacao(calcularMedia(avaliacoes, Avaliacao::getLimpezaOrganizacao));
        stats.setMediaAcessibilidade(calcularMedia(avaliacoes, Avaliacao::getAcessibilidade));
        stats.setMediaConservacaoEsculturas(calcularMedia(avaliacoes, Avaliacao::getConservacaoEsculturas));
        stats.setMediaSinalizacaoInformacoes(calcularMedia(avaliacoes, Avaliacao::getSinalizacaoInformacoes));

        List<String> comentarios = avaliacoes.stream()
                .map(Avaliacao::getComentariosSugestoes)
                .filter(c -> c != null && !c.trim().isEmpty())
                .limit(10) 
                .collect(Collectors.toList());
        stats.setComentariosRecentes(comentarios);

        return stats;
    }

    private double calcularMedia(List<Avaliacao> lista, ToIntFunction<Avaliacao> mapper) {
        return lista.stream()
                .filter(a -> {
                    try { return mapper.applyAsInt(a) > 0; } catch (Exception e) { return false; }
                })
                .mapToInt(mapper)
                .average()
                .orElse(0.0);
    }
}