package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.ContaPagar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @EntityGraph(attributePaths = {"fazenda", "safra"})
    Page<ContaPagar> findByFazendaIdAndSafraId(
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    // NOVO: listagem geral da fazenda
    @EntityGraph(attributePaths = {"fazenda", "safra"})
    Page<ContaPagar> findByFazendaId(
            Long fazendaId,
            Pageable pageable
    );
}
