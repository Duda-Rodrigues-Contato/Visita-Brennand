package com.example.Gestao_Viva.repository;

import com.example.Gestao_Viva.model.Avaliacao;
import com.example.Gestao_Viva.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByVisita(Visita visita);

}