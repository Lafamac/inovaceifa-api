package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Safra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafraRepository extends JpaRepository<Safra, Long> {

    /**
     * LEGADO (não paginado)
     */
    List<Safra> findByFazendaId(Long fazendaId);

    /**
     * PADRÃO NOVO (paginado)
     */
    Page<Safra> findByFazendaId(Long fazendaId, Pageable pageable);
}
