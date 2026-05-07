package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PlanejamentoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanejamentoInsumoRepository extends JpaRepository<PlanejamentoInsumo, Long> {

    List<PlanejamentoInsumo> findByPlanejamentoOperacaoIdAndAtivoTrue(Long planejamentoOperacaoId);

    // 🔥 NOVO (para comparação)
    List<PlanejamentoInsumo> findByPlanejamentoOperacao_Safra_Id(Long safraId);
}