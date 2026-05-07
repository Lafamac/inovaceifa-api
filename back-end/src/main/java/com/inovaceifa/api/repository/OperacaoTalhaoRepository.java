package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.OperacaoTalhao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OperacaoTalhaoRepository extends JpaRepository<OperacaoTalhao, Long> {

    /* CONTEXTO */

    Page<OperacaoTalhao> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    /* ORDEM SERVIÇO */

    List<OperacaoTalhao> findByOrdemServico_Id(Long ordemServicoId);

    @Query("""
    SELECT COALESCE(SUM(o.custoTotal), 0)
    FROM OperacaoTalhao o
    WHERE o.safra.id = :safraId
""")
    BigDecimal sumCustoBySafra(Long safraId);

    @Query("""
    SELECT COALESCE(SUM(o.areaTrabalhada), 0)
    FROM OperacaoTalhao o
    WHERE o.safra.id = :safraId
""")
    BigDecimal sumAreaBySafra(Long safraId);

    boolean existsBySafraTalhao_IdAndOrdemServico_StatusNot(Long safraTalhaoId, String status);
}