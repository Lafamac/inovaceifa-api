package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.OperacaoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OperacaoProdutoRepository extends JpaRepository<OperacaoProduto, Long> {

    /* CONTEXTO */

    Page<OperacaoProduto> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    List<OperacaoProduto> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId
    );

    /* TALHÃO */

    List<OperacaoProduto> findByOperacaoTalhao_Id(Long operacaoTalhaoId);

    /* 🔥 SOMA POR OPERAÇÃO */

    @Query("""
        SELECT COALESCE(SUM(p.vlrTotal), 0)
        FROM OperacaoProduto p
        WHERE p.operacaoTalhao.id = :id
    """)
    BigDecimal sumByOperacao(Long id);

    /* 🔥 NOVO: SOMA POR SAFRA (ULTRA OTIMIZADO) */

    @Query("""
        SELECT COALESCE(SUM(p.vlrTotal), 0)
        FROM OperacaoProduto p
        WHERE p.safra.id = :safraId
    """)
    BigDecimal sumBySafra(Long safraId);

    /* 🔥 TOP PRODUTOS */

    @Query("""
        SELECT p.produto.nome, SUM(p.vlrTotal)
        FROM OperacaoProduto p
        WHERE p.safra.id = :safraId
        GROUP BY p.produto.nome
        ORDER BY SUM(p.vlrTotal) DESC
    """)
    List<Object[]> topProdutos(Long safraId);
}