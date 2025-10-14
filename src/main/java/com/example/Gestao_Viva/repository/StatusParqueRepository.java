package com.example.Gestao_Viva.repository;

import com.example.Gestao_Viva.model.StatusParque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusParqueRepository extends JpaRepository<StatusParque, Long> {
    Optional<StatusParque> findTopByOrderByUltimaAtualizacaoDesc();
}
