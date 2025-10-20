package com.example.Gestao_Viva.repository;

import com.example.Gestao_Viva.model.Avaliacao;
import com.example.Gestao_Viva.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    boolean existsByVisita(Visita visita);

    
    @Query("SELECT a FROM Avaliacao a WHERE a.visita.dataVisita BETWEEN :dataInicio AND :dataFim ORDER BY a.dataAvaliacao DESC")
    List<Avaliacao> findByVisitaDataVisitaBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}