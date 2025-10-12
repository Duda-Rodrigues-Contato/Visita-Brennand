package com.example.Gestao_Viva.repository;

import com.example.Gestao_Viva.dto.VisitaDiariaDTO;
import com.example.Gestao_Viva.model.Visita;
import com.example.Gestao_Viva.model.enums.StatusVisita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.LocalDate;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    /**
     * @param dataInicio 
     * @param dataFim 
     * @param pageable 
     * @return 
     */
    Page<Visita> findByDataVisitaBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    /**
     * @param dataInicio 
     * @param dataFim 
     * @return 
     */
    long countByDataVisitaBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * @param dataInicio 
     * @param dataFim 
     * @param status
     * @return
     */
    long countByDataVisitaBetweenAndStatus(LocalDate dataInicio, LocalDate dataFim, StatusVisita status);

    /**
     * @param dataInicio 
     * @param dataFim 
     * @return 
     */
    @Query("SELECT new com.example.Gestao_Viva.dto.VisitaDiariaDTO(v.dataVisita, SUM(v.numeroVisitantes)) " +
           "FROM Visita v " +
           "WHERE v.dataVisita BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY v.dataVisita " +
           "ORDER BY v.dataVisita ASC")
    List<VisitaDiariaDTO> findTotalVisitantesPorDia(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    

}