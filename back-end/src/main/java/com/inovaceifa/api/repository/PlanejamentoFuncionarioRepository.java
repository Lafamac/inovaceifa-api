package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PlanejamentoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanejamentoFuncionarioRepository extends JpaRepository<PlanejamentoFuncionario, Long> {

    List<PlanejamentoFuncionario> findByPlanejamentoOperacaoIdAndAtivoTrue(Long planejamentoOperacaoId);

    // 🔥 NOVO (para comparação)
    List<PlanejamentoFuncionario> findByPlanejamentoOperacao_Safra_Id(Long safraId);
}