package com.gestaoviva.gestao_viva.repository;

import com.gestaoviva.gestao_viva.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findByDataVisitaOrderByHorarioChegada(LocalDate data);

    List<Visita> findByDataVisitaBetweenOrderByDataVisitaAscHorarioChegadaAsc(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(v.numeroVisitantes), 0) FROM Visita v WHERE v.dataVisita = :data")
    Integer sumNumeroVisitantesByDataVisita(LocalDate data);
}