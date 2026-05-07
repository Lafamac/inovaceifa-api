package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Administrativo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {

    Page<Administrativo> findByFazendaIdAndSafraId(
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    Optional<Administrativo> findByIdAndFazendaIdAndSafraId(
            Long id,
            Long fazendaId,
            Long safraId
    );

    /* 🔥 SOMA REALIZADO */
    @Query("""
        SELECT COALESCE(SUM(a.valorTotalRealizado), 0)
        FROM Administrativo a
        WHERE a.fazenda.id = :fazendaId
        AND a.safra.id = :safraId
    """)
    BigDecimal sumTotalRealizado(Long fazendaId, Long safraId);

    List<Administrativo> findByFazenda_IdAndSafra_Id(Long fazendaId, Long safraId);
}