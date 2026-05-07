package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PlanejamentoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanejamentoMaquinaRepository extends JpaRepository<PlanejamentoMaquina, Long> {

    List<PlanejamentoMaquina> findByPlanejamentoOperacaoIdAndAtivoTrue(Long planejamentoOperacaoId);
}