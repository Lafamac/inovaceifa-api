package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PlanejamentoOperacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanejamentoOperacaoRepository extends JpaRepository<PlanejamentoOperacao, Long> {

    Page<PlanejamentoOperacao> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    List<PlanejamentoOperacao> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId
    );

    List<PlanejamentoOperacao> findBySafraTalhao_Safra_IdAndAtivoTrue(Long safraId);

    Optional<PlanejamentoOperacao> findBySafraTalhaoId(Long safraTalhaoId);
}