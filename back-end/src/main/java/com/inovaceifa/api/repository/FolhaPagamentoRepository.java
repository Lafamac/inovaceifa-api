package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.FolhaPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, Long> {

    Page<FolhaPagamento> findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoTrue(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    Page<FolhaPagamento> findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoFalse(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    List<FolhaPagamento> findBySafra_Id(Long safraId);

    /* 🔥 SOMA TOTAL (USADO NO CUSTO) */
    @Query("""
        SELECT COALESCE(SUM(f.total), 0)
        FROM FolhaPagamento f
        WHERE f.proprietario.id = :proprietarioId
        AND f.fazenda.id = :fazendaId
        AND f.safra.id = :safraId
        AND f.ativo = true
    """)
    BigDecimal sumTotal(Long proprietarioId, Long fazendaId, Long safraId);

    @Query("""
    SELECT COALESCE(SUM(f.total), 0)
    FROM FolhaPagamento f
    WHERE f.safra.id = :safraId
""")
    BigDecimal sumBySafra(Long safraId);
}