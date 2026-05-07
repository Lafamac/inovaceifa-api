package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.OperacaoCombustivel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OperacaoCombustivelRepository extends JpaRepository<OperacaoCombustivel, Long> {

    Page<OperacaoCombustivel> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    List<OperacaoCombustivel> findByOperacaoTalhao_Id(Long operacaoTalhaoId);

    /* 🔥 SOMA POR OPERAÇÃO */

    @Query("""
        SELECT COALESCE(SUM(c.litros * c.valorUnitario), 0)
        FROM OperacaoCombustivel c
        WHERE c.operacaoTalhao.id = :id
    """)
    BigDecimal sumByOperacao(Long id);

    /* 🔥 NOVO: SOMA POR SAFRA */

    @Query("""
        SELECT COALESCE(SUM(c.litros * c.valorUnitario), 0)
        FROM OperacaoCombustivel c
        WHERE c.safra.id = :safraId
    """)
    BigDecimal sumBySafra(Long safraId);
}