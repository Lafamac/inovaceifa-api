package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PedidoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    Page<PedidoCompra> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    List<PedidoCompra> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId
    );

    Page<PedidoCompra> findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoTrue(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    Page<PedidoCompra> findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoFalse(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    /* 🔥 NOVO: SOMA POR SAFRA */

    @Query("""
        SELECT COALESCE(SUM(p.valorTotal), 0)
        FROM PedidoCompra p
        WHERE p.safra.id = :safraId
    """)
    BigDecimal sumBySafra(Long safraId);
}