package com.gestaoviva.gestao_viva.repository; // <-- CORRIGIDO

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestaoviva.gestao_viva.model.Visita; 

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
}