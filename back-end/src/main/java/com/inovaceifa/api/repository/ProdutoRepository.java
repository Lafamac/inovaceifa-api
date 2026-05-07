package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /* ===============================
       ATIVOS (já existiam)
       =============================== */

    List<Produto> findByFazendaIdAndAtivoTrue(Long fazendaId);

    Page<Produto> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);

    /* ===============================
       🆕 INATIVOS (NOVOS)
       =============================== */

    List<Produto> findByFazendaIdAndAtivoFalse(Long fazendaId);

    Page<Produto> findByFazendaIdAndAtivoFalse(Long fazendaId, Pageable pageable);

    /* ===============================
       BUSCA SEGURA
       =============================== */

    Optional<Produto> findByIdAndFazendaId(Long id, Long fazendaId);
}