package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.LancamentoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LancamentoDespesaRepository
        extends JpaRepository<LancamentoDespesa, Long> {

    /* =========================================================
       FILTRO COMPLETO (STATUS + PERÍODO)
       ========================================================= */
    @Query("""
        SELECT l FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
          AND (:status IS NULL OR l.statusPagamento = :status)
          AND (:dataInicio IS NULL OR l.data >= :dataInicio)
          AND (:dataFim IS NULL OR l.data <= :dataFim)
    """)
    List<LancamentoDespesa> filtrar(
            @Param("safraId") Long safraId,
            @Param("status") String status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    /* =========================================================
       RESUMO TOTAL
       ========================================================= */
    @Query("""
        SELECT COALESCE(SUM(l.valor), 0)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
    """)
    BigDecimal somarTotalDespesas(Long safraId);

    @Query("""
        SELECT COALESCE(SUM(l.valor), 0)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.statusPagamento = 'PAGO'
          AND l.ativo = true
    """)
    BigDecimal somarTotalPago(Long safraId);

    /* =========================================================
       RESUMO POR PERÍODO
       ========================================================= */
    @Query("""
        SELECT COALESCE(SUM(l.valor), 0)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
          AND (:dataInicio IS NULL OR l.data >= :dataInicio)
          AND (:dataFim IS NULL OR l.data <= :dataFim)
    """)
    BigDecimal somarTotalDespesasPeriodo(
            @Param("safraId") Long safraId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("""
        SELECT COALESCE(SUM(l.valor), 0)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.statusPagamento = 'PAGO'
          AND l.ativo = true
          AND (:dataInicio IS NULL OR l.data >= :dataInicio)
          AND (:dataFim IS NULL OR l.data <= :dataFim)
    """)
    BigDecimal somarTotalPagoPeriodo(
            @Param("safraId") Long safraId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("""
        SELECT COUNT(l)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
          AND (:dataInicio IS NULL OR l.data >= :dataInicio)
          AND (:dataFim IS NULL OR l.data <= :dataFim)
    """)
    Long contarLancamentosPeriodo(
            @Param("safraId") Long safraId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    /* =========================================================
       DASHBOARD POR CATEGORIA
       ========================================================= */
    @Query("""
        SELECT l.refDespesa.id,
               l.refDespesa.descricao,
               SUM(l.valor)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
        GROUP BY l.refDespesa.id, l.refDespesa.descricao
        ORDER BY SUM(l.valor) DESC
    """)
    List<Object[]> agruparPorCategoria(@Param("safraId") Long safraId);

    /* =========================================================
       DASHBOARD MENSAL
       ========================================================= */
    @Query("""
        SELECT YEAR(l.data),
               MONTH(l.data),
               SUM(l.valor)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
        GROUP BY YEAR(l.data), MONTH(l.data)
        ORDER BY YEAR(l.data), MONTH(l.data)
    """)
    List<Object[]> agruparPorMes(@Param("safraId") Long safraId);

    /* =========================================================
       🔵 NOVOS MÉTODOS FINANCEIROS AVANÇADOS
       ========================================================= */

    @Query("""
        SELECT COALESCE(SUM(l.valor), 0)
        FROM LancamentoDespesa l
        WHERE l.safra.id = :safraId
          AND l.ativo = true
    """)
    BigDecimal somarRealizado(@Param("safraId") Long safraId);

}