package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.OperacaoTalhao;
import com.inovaceifa.api.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    Page<OrdemServico> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    Optional<OrdemServico> findByPlanejamentoOperacaoId(Long planejamentoOperacaoId);

    boolean existsByPlanejamentoOperacaoIdAndStatusNot(Long planejamentoId, String status);
}